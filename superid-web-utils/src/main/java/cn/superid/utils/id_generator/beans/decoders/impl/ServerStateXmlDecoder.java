package cn.superid.utils.id_generator.beans.decoders.impl;

import cn.superid.utils.id_generator.beans.ServerState;
import cn.superid.utils.id_generator.beans.decoders.IServerStateXmlDecoder;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * Created by 维 on 2014/8/30.
 */
@Component
public class ServerStateXmlDecoder implements IServerStateXmlDecoder {
    @Override
    public ServerState decodeFromServerConfig(Element ele) {
        if (ele == null) {
            return null;
        }
        ServerState serverState = new ServerState();
        serverState.setId(Long.valueOf(ele.select("id").text()));
        serverState.setDescription(ele.select("description").text());
        serverState.setHost(ele.select("host").text());
        serverState.setPort(Integer.valueOf(ele.select("port").text()));
        serverState.setDbName(ele.select("db_name").text());
        serverState.setUser(ele.select("user").text());
        serverState.setPassword(ele.select("password").text());
        serverState.setOnline(Boolean.valueOf(ele.select("online").text()));
        serverState.setCapacity(Long.valueOf(ele.select("capacity").text()));
        serverState.setSize(Long.valueOf(ele.select("size").text()));
        return serverState;
    }
}
