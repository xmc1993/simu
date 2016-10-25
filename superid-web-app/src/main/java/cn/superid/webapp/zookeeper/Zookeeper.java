package cn.superid.webapp.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by xmc1993 on 16/10/17.
 */
public class Zookeeper {

    private static final int SESSION_TIMEOUT = 20000;
    private static final String CONNECTOR_URL = "/connectors";
    private static final String BACKEND_URL = "/backends";

    private ZooKeeper zooKeeper;

    public void connect() throws IOException {
        zooKeeper  = new ZooKeeper("192.168.1.100:2182,192.168.1.100:2183,192.168.1.100:2184",
                SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
    }

    public void close() {
        try {
            if(zooKeeper != null) {
                zooKeeper.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给指定路径设定watcher
     * @param nodeUrl
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void watchNodeChange(final String nodeUrl) throws KeeperException, InterruptedException {
        zooKeeper.exists(nodeUrl, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.EventType type = event.getType();
                if (type == Event.EventType.NodeDataChanged) {
                    System.out.println("节点" + nodeUrl + "的数据发生变化");
                }
            }
        });
    }

    public void watchConnectorNodeChange() throws KeeperException, InterruptedException {
        watchNodeChange(CONNECTOR_URL);
    }

    public void watchBackendNodeChange() throws KeeperException, InterruptedException {
        watchNodeChange(BACKEND_URL);
    }



}
