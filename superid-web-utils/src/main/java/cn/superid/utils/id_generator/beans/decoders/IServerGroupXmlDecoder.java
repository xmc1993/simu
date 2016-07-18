package cn.superid.utils.id_generator.beans.decoders;

import cn.superid.utils.id_generator.beans.ServerGroup;
import org.jsoup.nodes.Element;

/**
 * Created by zoowii on 2014/8/30.
 */
public interface IServerGroupXmlDecoder {
    ServerGroup decodeFromServerConfig(Element ele);
}
