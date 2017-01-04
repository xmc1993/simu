package cn.superid.webapp.notice.zookeeper;

import cn.superid.webapp.notice.ThriftPool;
import org.apache.thrift.transport.TTransportException;
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
    private static JSONObject connectorsInfo;
    private static JSONObject backEndsInfo;
    private static final Integer CLIENT_SEMAPHORE = 0;//同步信号
    private static final Integer CONNECTOR_SEMAPHORE = 0;//同步信号
    private static final Integer BACKEND_SEMAPHORE = 0;//同步信号

    private static ZooKeeper zooKeeper;

    static {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    private ZookeeperService() {
    }


    public static void init() throws IOException, KeeperException, InterruptedException {
        if (zooKeeper != null) return;

        synchronized (CLIENT_SEMAPHORE) {
            if (zooKeeper != null) return;
            zooKeeper = new ZooKeeper(ZOOKEEPER_URL,
                    SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("已经触发了" + event.getType() + "事件！");
                }
            });

            //设置节点监控
            try {
                watchConnectorNodeChange();
                watchBackendNodeChange();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回NodeJs那边所有的活跃的connector的信息
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws IOException
     */
    public static JSONObject getConnectorsInfo() throws KeeperException, InterruptedException, IOException {
        if (connectorsInfo != null) return connectorsInfo;
        return initConnectorsInfo();
    }

    /**
     * 更新connector信息
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    private static JSONObject updateConnectorsInfo() throws InterruptedException, IOException, KeeperException {

        synchronized (CONNECTOR_SEMAPHORE) {
            if (zooKeeper == null) init();
            String result = new String(zooKeeper.getData(CONNECTOR_URL, false, null));
            connectorsInfo = new JSONObject(result);
            return connectorsInfo;
        }
    }

    /**
     * 初始化connector信息
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    private static JSONObject initConnectorsInfo() throws InterruptedException, IOException, KeeperException {

        synchronized (CONNECTOR_SEMAPHORE) {
            if(connectorsInfo != null) return connectorsInfo;
            if (zooKeeper == null) init();
            String result = new String(zooKeeper.getData(CONNECTOR_URL, false, null));
            connectorsInfo = new JSONObject(result);
            return connectorsInfo;
        }
    }

    /**
     * 更新connector信息
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    private static JSONObject updateBackendsInfo() throws InterruptedException, IOException, KeeperException {

        synchronized (BACKEND_SEMAPHORE) {
            if (zooKeeper == null){
                init();
            }
            String result = new String(zooKeeper.getData(BACKEND_URL, false, null));
            backEndsInfo = new JSONObject(result);
            return backEndsInfo;
        }
    }

    /**
     * 初始化backend信息
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    private static JSONObject initBackendsInfo() throws InterruptedException, IOException, KeeperException {

        synchronized (BACKEND_SEMAPHORE) {
            if(backEndsInfo !=null){
                return backEndsInfo;
            }
            if (zooKeeper == null) init();
            String result = new String(zooKeeper.getData(BACKEND_URL, false, null));
            backEndsInfo = new JSONObject(result);
            return backEndsInfo;
        }
    }

    /**
     * 返回NodeJs那边所有的活跃的backend的信息
     *
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws IOException
     */

    public static JSONObject getBackEndsInfo() throws KeeperException, InterruptedException, IOException {
        if (backEndsInfo != null) return backEndsInfo;
        return initBackendsInfo();
    }


    /**
     * 给指定路径设定watcher
     *
     * @param nodeUrl
     * @throws KeeperException
     * @throws InterruptedException
     */
    private static void watchNodeChange(final String nodeUrl, final UpdateNodeDataService service) throws KeeperException, InterruptedException {
        zooKeeper.exists(nodeUrl, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //如果是NodeDataChange事件 更新本地缓存数据
                Event.EventType type = event.getType();
                if (type == Event.EventType.NodeDataChanged) {
                    try {
                        service.updateCache();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("节点" + nodeUrl + "的数据发生变化");
                    //调用ThriftPool的更新连接池的方法
                    try {
                        ThriftPool.updatePool();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TTransportException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 注册的watcher在触发一次事件后会失效 因为要重注册watcher
                    try {
                        watchConnectorNodeChange();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 监控Connectors节点的变化
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    private static void watchConnectorNodeChange() throws KeeperException, InterruptedException {
        watchNodeChange(CONNECTOR_URL, new UpdateNodeDataService() {
            @Override
            public void updateCache() throws Exception {
                updateConnectorsInfo();
            }
        });
    }

    /**
     * 监控Backends节点的变化
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    public static void watchBackendNodeChange() throws KeeperException, InterruptedException {
        watchNodeChange(BACKEND_URL, new UpdateNodeDataService() {
            @Override
            public void updateCache() throws Exception {
                updateBackendsInfo();
            }
        });
    }




}
