package cn.superid.utils.id_generator.services.impl;

import cn.superid.utils.id_generator.beans.ClusterMetaInfo;
import cn.superid.utils.id_generator.beans.*;
import cn.superid.utils.id_generator.beans.decoders.IDbTableDecoder;
import cn.superid.utils.id_generator.beans.decoders.IServerGroupXmlDecoder;
import cn.superid.utils.id_generator.beans.decoders.IServerMigrationDecoder;
import cn.superid.utils.id_generator.services.IMachineClusterStateManager;
import cn.superid.utils.ConfigUtil;
import cn.superid.utils.FileUtil;
import cn.superid.utils.ListUtil;
import cn.superid.utils.StringUtil;
import com.google.common.base.Function;
import com.google.inject.Inject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zoowii on 2014/8/29.
 */
@Component
public class MachineClusterStateManager implements IMachineClusterStateManager {
    protected ClusterMetaInfo clusterMetaInfo;
    /**
     * 本地缓存的最后修改时间
     */
    protected Date lastUpdateCacheTime = null;
    @Autowired
    protected IServerGroupXmlDecoder serverGroupXmlDecoder;
    @Autowired
    protected IServerMigrationDecoder serverMigrationDecoder;
    @Autowired
    protected IDbTableDecoder dbTableDecoder;

    public MachineClusterStateManager() {
    }

    @Inject
    public MachineClusterStateManager(IServerGroupXmlDecoder serverGroupXmlDecoder, IServerMigrationDecoder serverMigrationDecoder, IDbTableDecoder dbTableDecoder) {
        this.serverGroupXmlDecoder = serverGroupXmlDecoder;
        this.serverMigrationDecoder = serverMigrationDecoder;
        this.dbTableDecoder = dbTableDecoder;
    }

    private CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private byte[] utf8Bytes(String str) {
        if (str == null) {
            return null;
        }
        return str.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public ClusterMetaInfo getClusterMetaInfoFromRemote() {
        // 从元信息服务中获取
        try {
            Document doc = FileUtil.loadXmlResource("id_generator_servers.xml"); // FIXME: for test
//            if (!existsPath(path)) {
//                Document doc = FileUtil.loadXmlResource("servers.xml");
//                createPath(path, doc.html());
//            }
//            String configXml = readData(path);
//            Document doc = Jsoup.parse(configXml, "", Parser.xmlParser());
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

    @Override
    public List<ServerGroup> getServerGroups() {
        ClusterMetaInfo clusterMetaInfo = this.getClusterMetaInfo();
        if (clusterMetaInfo == null) {
            return null;
        }
        return clusterMetaInfo.getGroups();
    }

    @Override
    public ClusterMetaInfo getClusterMetaInfo() {
        if (clusterMetaInfo == null) {
            this.reloadClusterMetaInfoCache(getClusterMetaInfoFromRemote());
        }
        return this.clusterMetaInfo;
    }

    @Override
    public List<DbTableInfo> getDbTableInfos() {
        ClusterMetaInfo clusterMetaInfo = this.getClusterMetaInfo();
        if (clusterMetaInfo == null) {
            return null;
        }
        return clusterMetaInfo.getTables();
    }

    @Override
    public ServerGroup getServerGroup(final String name) {
        return ListUtil.first(getServerGroups(), new Function<ServerGroup, Boolean>() {
            @Override
            public Boolean apply(ServerGroup serverGroup) {
                return serverGroup != null && StringUtil.isEqualWithTrim(serverGroup.getName(), name);
            }
        });
    }

    @Override
    public synchronized void reloadClusterMetaInfoCache(ClusterMetaInfo clusterMetaInfo) {
        this.clusterMetaInfo = clusterMetaInfo;
        this.lastUpdateCacheTime = new Date();
    }

}
