package cn.superid.webapp.notice.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by xmc1993 on 16/10/17.
 */
public class ZookeeperService {

    private static final int SESSION_TIMEOUT = 20000;
    private static final String CONNECTOR_URL = "/connectors";
    private static final String BACKEND_URL = "/backends";
    private static final String ZOOKEEPER_URL = "192.168.1.100:2182,192.168.1.100:2183,192.168.1.100:2184";

    private static ZooKeeper zooKeeper;

//    static {
//        try {
//            init();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private ZookeeperService() throws IOException {
        init();
    }


    public static synchronized void init() throws IOException {
        if (zooKeeper != null) {
            return;
        }
        zooKeeper = new ZooKeeper(ZOOKEEPER_URL,
                SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
    }

    /**
     * 返回NodeJs那边所有的活跃的connector的信息
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws IOException
     */
    public static JSONObject getConnectorsInfo() throws KeeperException, InterruptedException, IOException {
        if (zooKeeper == null) {
            init();
        }
        String result = new String(zooKeeper.getData(CONNECTOR_URL, false, null));
        return new JSONObject(result);
    }

    /**
     * 返回NodeJs那边所有的活跃的backend的信息
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws IOException
     */
    public static JSONObject getBackendsInfo() throws KeeperException, InterruptedException, IOException {
        if (zooKeeper == null) {
            init();
        }
        String result = new String(zooKeeper.getData(BACKEND_URL, false, null));
        return new JSONObject(result);
    }


//    /**
//     * 给指定路径设定watcher
//     * @param nodeUrl
//     * @throws KeeperException
//     * @throws InterruptedException
//     */
//    public void watchNodeChange(final String nodeUrl) throws KeeperException, InterruptedException {
//        zooKeeper.exists(nodeUrl, new Watcher() {
//            @Override
//            public void process(WatchedEvent event) {
//                Event.EventType type = event.getType();
//                if (type == Event.EventType.NodeDataChanged) {
//                    System.out.println("节点" + nodeUrl + "的数据发生变化");
//                }
//            }
//        });
//    }
//
//    public void watchConnectorNodeChange() throws KeeperException, InterruptedException {
//        watchNodeChange(CONNECTOR_URL);
//    }
//
//    public void watchBackendNodeChange() throws KeeperException, InterruptedException {
//        watchNodeChange(BACKEND_URL);
//    }


}
