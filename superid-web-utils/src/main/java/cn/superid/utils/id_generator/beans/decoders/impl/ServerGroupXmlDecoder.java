package cn.superid.utils.id_generator.beans.decoders.impl;

import cn.superid.utils.id_generator.beans.*;
import cn.superid.utils.ListUtil;
import cn.superid.utils.id_generator.beans.decoders.IServerGroupXmlDecoder;
import cn.superid.utils.id_generator.beans.decoders.IServerStateXmlDecoder;
import com.google.inject.Inject;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by zoowii on 2014/8/30.
 */
@Component
public class ServerGroupXmlDecoder implements IServerGroupXmlDecoder {
    @Autowired
    private IServerStateXmlDecoder serverStateXmlDecoder;

    @Inject
    public ServerGroupXmlDecoder(IServerStateXmlDecoder serverStateXmlDecoder) {
        this.serverStateXmlDecoder = serverStateXmlDecoder;
    }

    @Override
    public ServerGroup decodeFromServerConfig(Element ele) {
        if (ele == null) {
            return null;
        }
        ServerGroup group = new ServerGroup();
        group.setName(ele.select("name").text());
        group.setServers(ListUtil.map(ele.select("servers server"), new com.google.common.base.Function<Element, ServerState>() {
            @Override
            public ServerState apply(Element serverEle) {
                return serverStateXmlDecoder.decodeFromServerConfig(serverEle);
            }
        }));
        return group;
    }

    public ServerGroupXmlDecoder() {
    }
}
