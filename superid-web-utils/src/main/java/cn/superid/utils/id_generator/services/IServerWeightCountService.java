package cn.superid.utils.id_generator.services;

import cn.superid.utils.id_generator.beans.*;

import java.util.List;

/**
 * Created by 维 on 2014/9/22.
 */
public interface IServerWeightCountService {
    /**
     * 计算group中某些servers的权重,不考虑其他server,也就是这些servers的权重和为1
     *
     * @param group
     * @param serverStates
     */
    void countServerWeight(ServerGroup group, List<ServerState> serverStates);

    /**
     * 数据库服务器是否满载或者接近满载
     *
     * @param serverState
     * @return
     */
    boolean canServerAcceptMore(ServerState serverState);

    /**
     * 排除掉满载或者接近满载的数据库服务器
     *
     * @param serverStates
     * @return
     */
    List<ServerState> splitFullServers(List<ServerState> serverStates);
}
