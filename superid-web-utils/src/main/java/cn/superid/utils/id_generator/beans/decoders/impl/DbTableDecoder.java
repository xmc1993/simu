package cn.superid.utils.id_generator.beans.decoders.impl;

import cn.superid.utils.id_generator.beans.DbTableInfo;
import cn.superid.utils.id_generator.beans.decoders.IDbTableDecoder;
import cn.superid.utils.ListUtil;
import com.google.common.base.Function;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;


/**
 * Created by 维 on 2014/9/3.
 */
@Component
public class DbTableDecoder implements IDbTableDecoder {
    public DbTableDecoder() {
    }

    @Override
    public DbTableInfo decodeFromServerConfig(Element ele) {
        if (ele == null) {
            return null;
        }
        DbTableInfo tableInfo = new DbTableInfo();
        tableInfo.setName(ele.select("table_name").text());
        tableInfo.setGroup(ele.select("master_group").text());
        tableInfo.setSplitColumns(ListUtil.map(ele.select("split_columns column"), new Function<Element, String>() {
            @Override
            public String apply(Element element) {
                if (element == null) {
                    return null;
                }
                return element.text();
            }
        }));
        return tableInfo;
    }
}
