package cn.superid.utils.id_generator.beans.decoders;

import cn.superid.utils.id_generator.beans.DbTableInfo;
import org.jsoup.nodes.Element;

/**
 * Created by zoowii on 2014/9/3.
 */
public interface IDbTableDecoder {
    DbTableInfo decodeFromServerConfig(Element ele);
}
