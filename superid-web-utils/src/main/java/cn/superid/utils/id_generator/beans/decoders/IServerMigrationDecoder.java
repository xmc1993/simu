package cn.superid.utils.id_generator.beans.decoders;

import cn.superid.utils.id_generator.beans.ServerMigration;
import org.jsoup.nodes.Element;

/**
 * Created by zoowii on 2014/9/2.
 */
public interface IServerMigrationDecoder {
    ServerMigration decodeFromServerConfig(Element ele);
}
