package cn.superid.utils.id_generator.services.impl;

import cn.superid.utils.id_generator.beans.*;
import cn.superid.utils.id_generator.exceptions.IdGeneratorException;
import cn.superid.utils.id_generator.exceptions.ServerNotInGroupException;
import cn.superid.utils.id_generator.exceptions.TableNotFoundInMetaInfoException;
import cn.superid.utils.id_generator.services.IMachineClusterStateManager;
import cn.superid.utils.id_generator.services.IServerGroupService;
import cn.superid.utils.id_generator.services.IServerWeightCountService;
import cn.superid.utils.MathUtil;
import cn.superid.utils.ListUtil;
import cn.superid.utils.StringUtil;
import com.google.common.base.Function;
import com.google.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zoowii on 2014/8/30.
 */
@Component
public class ServerGroupService implements IServerGroupService {
    @Autowired
    private IMachineClusterStateManager machineClusterStateManager;
    @Autowired
    private IServerWeightCountService serverWeightCountService;

    @Inject
    public ServerGroupService(IMachineClusterStateManager machineClusterStateManager, IServerWeightCountService serverWeightCountService) {
        this.machineClusterStateManager = machineClusterStateManager;
        this.serverWeightCountService = serverWeightCountService;
    }

    @Override
    public boolean isGroupEmpty(ServerGroup group) {
        if (group == null) {
            throw new NullPointerException();
        }
        return getOnlineServersInGroup(group).size() <= 0;
    }

    @Override
    public boolean isGroupFull(ServerGroup group) {
        if (group == null) {
            throw new NullPointerException();
        }
        return ListUtil.all(getOnlineServersInGroup(group), new Function<ServerState, Boolean>() {
            @Override
            public Boolean apply(ServerState serverState) {
                return serverState != null && serverState.getSize() >= serverState.getCapacity();
            }
        });
    }

    @Override
    public ServerState selectBestServerFromGroup(ServerGroup group) {
        if (group == null) {
            throw new NullPointerException();
        }
        // 根据容量/权重,按比例随机,达到或接近的上限的机器不再存储新数据

        // 判断是否达到上限,从选项中移除掉
        List<ServerState> serverStates = serverWeightCountService.splitFullServers(group.getServers());
        serverWeightCountService.countServerWeight(group, serverStates);
        // 按几何分布产生随机数(一段数值区间按权重划分线段长,随机数进入不同线段区间)
        return MathUtil.randomWithWeight(ListUtil.map(serverStates, new Function<ServerState, Pair<ServerState, Double>>() {
            @Override
            public Pair<ServerState, Double> apply(ServerState serverState) {
                return Pair.of(serverState, serverState.getWeight());
            }
        }));

//        return ListUtil.max(getOnlineServersInGroup(group), new Comparator<ServerState>() {
//            @Override
//            public int compare(ServerState o1, ServerState o2) {
//                if (o1 == null) {
//                    return -1;
//                }
//                if (o2 == null) {
//                    return 1;
//                }
//                // 按照 当前容量 / 最大容量评分，选出最“宽裕”的服务器
//                double score1 = o1.getSize() * 1.0 / o1.getCapacity();
//                double score2 = o2.getSize() * 1.0 / o2.getCapacity();
//                return score1 > score2 ? 1 : -1;
//            }
//        });
    }

    @Override
    public List<ServerState> getOnlineServersInGroup(ServerGroup group) {
        if (group == null) {
            throw new NullPointerException();
        }
        return ListUtil.filter(group.getServers(), new Function<ServerState, Boolean>() {
            @Override
            public Boolean apply(ServerState serverState) {
                return serverState != null && serverState.isOnline();
            }
        });
    }

    @Override
    public void incrementServerStateSize(ServerState serverState) {
        serverState.incrementSize();
    }

    @Override
    public ServerState selectFinalServerIfMigrated(ClusterMetaInfo clusterMetaInfo, ServerState serverState) throws ServerNotInGroupException {
        if (serverState == null) {
            return null;
        }
        if (clusterMetaInfo == null) {
            return serverState;
        }
        final ServerMigration migration = findServerMigration(clusterMetaInfo.getMigrations(), serverState.getId());
        if (migration == null) {
            return serverState;
        }
        ServerGroup group = ListUtil.first(clusterMetaInfo.getGroups(), new Function<ServerGroup, Boolean>() {
            @Override
            public Boolean apply(ServerGroup serverGroup) {
                return serverGroup != null && StringUtil.isEqualWithTrim(serverGroup.getName(), migration.getGroupName());
            }
        });
        if (group == null) {
            return serverState;
        }
        // 判断mgiration的from和to是否都是这个group中的
        if (migration.getFrom() != serverState.getId()) {
            return serverState;
        }
        ServerState toServer = findServerById(group, migration.getTo());
        if (toServer == null) {
            throw new ServerNotInGroupException(String.format("%d not in group %s", migration.getTo(), group.getName()));
        }
        return selectFinalServerIfMigrated(clusterMetaInfo, serverState);
    }

    @Override
    public ServerMigration findServerMigration(List<ServerMigration> migrations, final long fromServerId) {
        if (migrations == null) {
            return null;
        }
        return ListUtil.first(migrations, new Function<ServerMigration, Boolean>() {
            @Override
            public Boolean apply(ServerMigration serverMigration) {
                return serverMigration != null && serverMigration.getFrom() == fromServerId;
            }
        });
    }

    @Override
    public ServerState findServerById(ServerGroup group, final long id) {
        if (group == null) {
            return null;
        }
        return ListUtil.first(group.getServers(), new Function<ServerState, Boolean>() {
            @Override
            public Boolean apply(ServerState serverState) {
                return serverState != null && serverState.isOnline() && serverState.getId() == id;
            }
        });
    }

    @Override
    public DbTableInfo findDbTableInfo(final String tableName) {
        return ListUtil.first(machineClusterStateManager.getDbTableInfos(), new Function<DbTableInfo, Boolean>() {
            @Override
            public Boolean apply(DbTableInfo dbTableInfo) {
                return dbTableInfo != null && StringUtil.isEqualWithTrim(tableName, dbTableInfo.getName());
            }
        });
    }

    public ServerGroupService() {
    }

    @Override
    public int getTableSplitDimensions(final String tableName) throws TableNotFoundInMetaInfoException {
        DbTableInfo table = findDbTableInfo(tableName);
        if (table == null) {
            throw new TableNotFoundInMetaInfoException("table " + tableName + " not found in meta info");
        }
        return ListUtil.size(table.getSplitColumns());
    }

    @Override
    public ServerGroup findGroupByServerId(final long serverId) throws IdGeneratorException {
        return ListUtil.first(machineClusterStateManager.getServerGroups(), new Function<ServerGroup, Boolean>() {
            @Override
            public Boolean apply(ServerGroup group) {
                return group != null && ListUtil.any(getOnlineServersInGroup(group), new Function<ServerState, Boolean>() {
                    @Override
                    public Boolean apply(ServerState serverState) {
                        return serverState != null && serverState.getId() == serverId;
                    }
                });
            }
        });
    }

    @Override
    public DbTableInfo getDbTableInfoByTableName(final String tableName) {
        return ListUtil.first(machineClusterStateManager.getDbTableInfos(), new Function<DbTableInfo, Boolean>() {
            @Override
            public Boolean apply(DbTableInfo table) {
                return table != null && StringUtil.isEqualWithTrim(table.getName(), tableName);
            }
        });
    }

    @Override
    public ServerGroup getDefaultGroupOfTable(final String tableName) throws IdGeneratorException {
        DbTableInfo table = getDbTableInfoByTableName(tableName);
        if (table == null) {
            return null;
        }
        String groupName = table.getGroup();
        return machineClusterStateManager.getServerGroup(groupName);
    }
}
