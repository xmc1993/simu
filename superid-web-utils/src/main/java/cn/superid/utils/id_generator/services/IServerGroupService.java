package cn.superid.utils.id_generator.services;

import cn.superid.utils.id_generator.beans.*;
import cn.superid.utils.id_generator.exceptions.IdGeneratorException;
import cn.superid.utils.id_generator.exceptions.ServerNotInGroupException;
import cn.superid.utils.id_generator.exceptions.TableNotFoundInMetaInfoException;

import java.util.List;

/**
 * Created by zoowii on 2014/8/30.
 */
public interface IServerGroupService {
    /**
     * 判断服务器组是否没有在线服务器
     *
     * @param group
     * @return
     */
    boolean isGroupEmpty(ServerGroup group);

    /**
     * 判断服务器组是否满载
     *
     * @param group
     * @return
     */
    boolean isGroupFull(ServerGroup group);

    /**
     * 从机组中选择一台最合适的服务器
     * 选择策略，排除掉不在线的，满载的
     * 然后按负载比例
     *
     * @param group
     * @return
     */
    ServerState selectBestServerFromGroup(ServerGroup group);

    /**
     * 获取一个机器组中在线的服务器列表
     *
     * @param group
     * @return
     */
    List<ServerState> getOnlineServersInGroup(ServerGroup group);

    /**
     * 变更本地机器元信息缓存
     *
     * @param serverState
     */
    void incrementServerStateSize(ServerState serverState);

    /**
     * 如果数据库服务器有迁移，就返回迁移到的，递归执行
     *
     * @param clusterMetaInfo
     * @param serverState
     * @return
     */
    ServerState selectFinalServerIfMigrated(ClusterMetaInfo clusterMetaInfo, ServerState serverState) throws ServerNotInGroupException;

    ServerMigration findServerMigration(List<ServerMigration> migrations, long fromServerId);

    ServerState findServerById(ServerGroup group, long id);

    DbTableInfo findDbTableInfo(final String tableName);

    /**
     * 获取表的分表维度（按多少维分表）
     *
     * @param tableName
     * @return
     * @throws TableNotFoundInMetaInfoException
     */
    int getTableSplitDimensions(final String tableName) throws TableNotFoundInMetaInfoException;

    /**
     * 按机器编号找到所载组，需要serverId唯一
     *
     * @param serverId
     * @return
     * @throws IdGeneratorException
     */
    ServerGroup findGroupByServerId(final long serverId) throws IdGeneratorException;

    /**
     * 找到数据表的元信息
     *
     * @param tableName
     * @return
     */
    DbTableInfo getDbTableInfoByTableName(final String tableName);

    /**
     * 获取数据库的默认group
     *
     * @param tableName
     * @return
     * @throws IdGeneratorException
     */
    ServerGroup getDefaultGroupOfTable(final String tableName) throws IdGeneratorException;
}
