package cn.superid.utils.id_generator.services.impl;

import cn.superid.utils.id_generator.beans.*;
import cn.superid.utils.FileUtil;
import cn.superid.utils.ListUtil;
import cn.superid.utils.id_generator.beans.decoders.*;
import com.google.common.base.Function;
import com.google.inject.Inject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * 通过配置文件XML管理的服务器集群状态
 * Created by zoowii on 2014/9/9.
 */
public class XmlMachineClusterStateManager extends MachineClusterStateManager {
    public XmlMachineClusterStateManager() {
    }

    @Inject
    public XmlMachineClusterStateManager(IServerGroupXmlDecoder serverGroupXmlDecoder, IServerMigrationDecoder serverMigrationDecoder, IDbTableDecoder dbTableDecoder) {
        super(serverGroupXmlDecoder, serverMigrationDecoder, dbTableDecoder);
    }

    @Override
    public ClusterMetaInfo getClusterMetaInfoFromRemote() {
        // 从配置文件中读取
        try {
            Document doc = FileUtil.loadXmlResource("id_generator_servers.xml");
            List<ServerGroup> groups = ListUtil.map(doc.select("groups group"), new Function<Element, ServerGroup>() {
                @Override
                public ServerGroup apply(Element groupEle) {
                    return serverGroupXmlDecoder.decodeFromServerConfig(groupEle);
                }
            });
            List<ServerMigration> migrations = ListUtil.map(doc.select("migrations migration"), new Function<Element, ServerMigration>() {
                @Override
                public ServerMigration apply(Element element) {
                    return serverMigrationDecoder.decodeFromServerConfig(element);
                }
            });
            List<DbTableInfo> tables = ListUtil.map(doc.select("tables table"), new Function<Element, DbTableInfo>() {
                @Override
                public DbTableInfo apply(Element element) {
                    return dbTableDecoder.decodeFromServerConfig(element);
                }
            });
            ClusterMetaInfo clusterMetaInfo = new ClusterMetaInfo();
            clusterMetaInfo.setGroups(groups);
            clusterMetaInfo.setMigrations(migrations);
            clusterMetaInfo.setTables(tables);
            return clusterMetaInfo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
