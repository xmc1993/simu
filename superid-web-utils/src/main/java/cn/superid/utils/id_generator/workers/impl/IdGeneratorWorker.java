package cn.superid.utils.id_generator.workers.impl;

import cn.superid.utils.IncrementCircleNumber;
import cn.superid.utils.id_generator.beans.ServerGroup;
import cn.superid.utils.id_generator.beans.ServerState;
import cn.superid.utils.id_generator.exceptions.*;
import cn.superid.utils.id_generator.services.*;
import cn.superid.utils.id_generator.workers.IIdGeneratorWorker;
import com.google.common.base.Function;
import com.google.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by zoowii on 2014/8/29.
 */
@Component
public class IdGeneratorWorker implements IIdGeneratorWorker {
    @Autowired
    private IIdMerger idMerger;
    @Autowired
    private IMachineClusterStateManager machineClusterStateManager;
    @Autowired
    private IServerGroupService serverGroupService;
    @Autowired
    private IDbRouter dbRouter;

    private static final IncrementCircleNumber firstPartGenerator = new IncrementCircleNumber(0L, Long.MAX_VALUE); // FIXME

    public IdGeneratorWorker() {
    }

    @Inject
    public IdGeneratorWorker(IIdMerger idMerger, IMachineClusterStateManager machineClusterStateManager, IServerGroupService serverGroupService, IDbRouter dbRouter) {
        this.idMerger = idMerger;
        this.machineClusterStateManager = machineClusterStateManager;
        this.serverGroupService = serverGroupService;
        this.dbRouter = dbRouter;
    }

    @Override
    public String nextId(final String tableName, final Map<String, Object> record) throws IdGeneratorException {
        // 判断表是否多维
        ServerGroup group;
        if (serverGroupService.getTableSplitDimensions(tableName) > 1) {
            // 如果是多维分表，调用路由服务获取到机器编号，进而获取到机器所在group
            long serverId = dbRouter.route(tableName, record);
            group = serverGroupService.findGroupByServerId(serverId);
        } else {
            // 如果是单位分表，从元信息服务中获取到表对应的group
            group = serverGroupService.getDefaultGroupOfTable(tableName);
        }
        if (group == null) {
            throw new NoGroupOfTableException("can't find group of table " + tableName);
        }
        return nextId(group.getName());
    }

    @Override
    public String nextId(String groupName) throws IdGeneratorException {
        if (groupName == null) {
            throw new NullPointerException("group name can't be null");
        }
        ServerGroup group = machineClusterStateManager.getServerGroup(groupName);
        if (group == null) {
            throw new ServerGroupNotFoundException(groupName);
        }
        // 判断服务器组负载情况
        if (serverGroupService.isGroupEmpty(group)) {
            throw new ServerGroupEmptyException(String.format("group %s is empty(no online servers)", groupName));
        }
        if (serverGroupService.isGroupFull(group)) {
            throw new ServerGroupFullException(String.format("group %s is full(all online servers is full)", groupName));
        }
        // 选择一个数据库服务器
        ServerState serverState = serverGroupService.selectBestServerFromGroup(group);
        if (serverState == null) {
            throw new NoAvailableServerException(String.format("group %s has no available servers", groupName));
        }
        // 判断数据库服务器迁移情况，如果有迁移，就换成新的，需要递归执行
        serverState = serverGroupService.selectFinalServerIfMigrated(machineClusterStateManager.getClusterMetaInfo(), serverState);

        // 分配一个唯一的ID前缀，从数据库中获取
        Function<Void, Long> firstPartIdGenerator = new Function<Void, Long>() {
            @Override
            public Long apply(Void aVoid) {
                return firstPartGenerator.getAndIncrement();
            }
        };
        // 合并ID前缀和机器编号，生成最终ID并返回
        String finalId = idMerger.generateFinalId(firstPartIdGenerator, groupName, serverState.getId());
//        Logger.info(String.format("server id: %d, final id: %s", serverState.getId(), finalId));
        // 更新本地的数据库集群元信息缓存
        serverGroupService.incrementServerStateSize(serverState);
        return finalId;
    }
}
