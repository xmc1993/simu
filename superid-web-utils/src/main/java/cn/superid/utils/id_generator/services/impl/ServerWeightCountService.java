package cn.superid.utils.id_generator.services.impl;

import cn.superid.utils.id_generator.beans.ServerGroup;
import cn.superid.utils.id_generator.beans.ServerState;
import cn.superid.utils.id_generator.services.IServerWeightCountService;
import cn.superid.utils.ListUtil;
import com.google.common.base.Function;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zoowii on 2014/9/22.
 */
@Component
public class ServerWeightCountService implements IServerWeightCountService {
    /**
     * 数据库服务器的权重的最大值
     */
    private static final double MAX_SERVER_WEIGHT = 10;

    public ServerWeightCountService() {
    }

    @Override
    public void countServerWeight(ServerGroup group, List<ServerState> serverStates) {
        if (group == null || serverStates == null || ListUtil.size(serverStates) < 1) {
            return;
        }
        serverStates = ListUtil.filter(serverStates, new Function<ServerState, Boolean>() {
            @Override
            public Boolean apply(ServerState serverState) {
                return serverState != null;
            }
        });
        ListUtil.map(serverStates, new Function<ServerState, Object>() {
            @Override
            public Object apply(ServerState serverState) {
                if (serverState.getSize() < 1) {
                    return MAX_SERVER_WEIGHT;
                }
                double weight = serverState.getCapacity() * 1.0 / serverState.getSize();
                if (weight > MAX_SERVER_WEIGHT) {
                    weight = MAX_SERVER_WEIGHT;
                } else if (weight < 1) {
                    weight = 1;
                }
                serverState.setWeight(weight);
                return weight;
            }
        });

    }

    /**
     * FIXME: 暂时把超过最大容量90%的数据库服务器当做满载或者接近满载,在选择用来存储的服务器时不做考虑
     *
     * @param serverState
     * @return
     */
    @Override
    public boolean canServerAcceptMore(ServerState serverState) {
        if (serverState == null) {
            return false;
        }
        return serverState.getSize() * 1.0 / serverState.getCapacity() < 0.9;
    }

    @Override
    public List<ServerState> splitFullServers(List<ServerState> serverStates) {
        return ListUtil.filter(serverStates, new Function<ServerState, Boolean>() {
            @Override
            public Boolean apply(ServerState serverState) {
                return serverState != null && canServerAcceptMore(serverState);
            }
        });
    }
}
