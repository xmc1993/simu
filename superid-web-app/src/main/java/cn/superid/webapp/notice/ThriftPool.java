package cn.superid.webapp.notice;

import cn.superid.webapp.notice.thrift.NoticeService;
import cn.superid.webapp.notice.zookeeper.ZookeeperService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.zookeeper.KeeperException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * Created by xmc1993 on 17/1/3.
 */
public class ThriftPool {

    private static Map<String, NoticeService.Client> _pool;
    private static Map<String, TTransport> _channel;
    private static final Integer POOL_SEMAPHORE = 0;//同步信号
    private static final Integer PORT_DISTANCE = 7;//shrift-server相对于backend-server的端口差

    private ThriftPool() {
    }

    /**
     * 初始化连接池
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     * @throws TTransportException
     */
    private static void init() throws InterruptedException, IOException, KeeperException, TTransportException {
        synchronized (POOL_SEMAPHORE) {
            if (_pool != null) return;
            _pool = new HashMap<>();
            _channel = new HashMap<>();
            JSONObject backEndsInfo = ZookeeperService.getBackEndsInfo();
            Iterator keys = backEndsInfo.keys();
            while (keys.hasNext()) {
                //对从zookeeper中拿到的URL信息进行处理获得各个thrift-server的host和port
                String url = (String) backEndsInfo.get((String) keys.next());
                String[] info = url.split(":");
                String host = info[0];
                Integer port = Integer.valueOf(info[1]);
                port -= PORT_DISTANCE;

                //对于每一个开启thrift-server开启一个连接,并保存到_pool中
                TTransport transport = new TSocket(host, port);
                TProtocol protocol = new TBinaryProtocol(transport);
                NoticeService.Client _client = new NoticeService.Client(protocol);
                transport.open();
                _pool.put(host + ":" + port, _client);
                _channel.put(host + ":" + port, transport);
            }
        }
    }


    public static NoticeService.Client getClient(String key) throws InterruptedException, TTransportException, KeeperException, IOException {
        if (_pool == null) init();
        return _pool.get(key);
    }

    /**
     * 根据zookeeper中节点数据的变化更新连接池
     *
     * @throws InterruptedException
     * @throws TTransportException
     * @throws KeeperException
     * @throws IOException
     */
    public static void updatePool() throws InterruptedException, TTransportException, KeeperException, IOException {
        if (_pool == null) {
            init();
            return;
        }
        synchronized (POOL_SEMAPHORE) {
            JSONObject backEndsInfo = ZookeeperService.getBackEndsInfo();
            Iterator keys = backEndsInfo.keys();
            List<String> urls = new ArrayList<>();

            //更新连接池中的连接
            while (keys.hasNext()) {
                //对从zookeeper中拿到的URL信息进行处理获得各个thrift-server的host和port
                String url = (String) backEndsInfo.get((String) keys.next());
                urls.add(url);
                String[] info = url.split(":");
                String host = info[0];
                Integer port = Integer.valueOf(info[1]);
                port -= PORT_DISTANCE;
                NoticeService.Client client = _pool.get(host + ":" + port);
                TTransport tTransport = _channel.get(host + ":" + port);

                //如果连接池中的对应连接状态正常,则不做任何处理
                if (client != null && tTransport != null && tTransport.isOpen()) continue;

                if (client == null || tTransport == null) {//如果不存在对应的连接
                    TTransport transport = new TSocket(host, port);
                    TProtocol protocol = new TBinaryProtocol(transport);
                    NoticeService.Client _client = new NoticeService.Client(protocol);
                    transport.open();
                    _pool.put(host + ":" + port, _client);
                    _channel.put(host + ":" + port, transport);
                } else if (!tTransport.isOpen()) {//如果通道被关闭 尝试重新打开 TODO 三次重连?
                    tTransport.open();
                }

            }

            //移除不再需要的连接
            Set<String> localKeys = _pool.keySet();
            for (String key : localKeys) {
                if(!urls.contains(key)){
                    _pool.remove(key);
                    _channel.get(key).close();
                    _channel.remove(key);
                }
            }

        }
    }
}
