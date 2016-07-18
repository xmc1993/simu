package cn.superid.utils.id_generator.beans.decoders;

import cn.superid.utils.id_generator.beans.ServerState;
import org.jsoup.nodes.Element;

/**
 * Created by zoowii on 2014/8/29.
 */
public interface IServerStateXmlDecoder {
    /**
     * @return
     */
    ServerState decodeFromServerConfig(Element ele);
}
