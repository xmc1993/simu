import cn.superid.webapp.notice.zookeeper.ZookeeperService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by xmc1993 on 16/10/18.
 */
public class ZookeeperServiceTest {

    @Test
    public void testZookeeper() throws IOException, KeeperException, InterruptedException {
        JSONObject connectorsInfo = ZookeeperService.getConnectorsInfo();
        System.out.println(connectorsInfo);
        System.out.println("");

    }

    @Test
    public void testZookeeperClient() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper  = new ZooKeeper("192.168.1.100:2182,192.168.1.100:2183,192.168.1.100:2184",
                2000, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
        System.out.println(new String(zooKeeper.getData("/connectors",false,null)));
        System.out.println("--------------");
//        System.out.println(new String(zooKeeper.getData("/backends",false,null)));
    }
}
