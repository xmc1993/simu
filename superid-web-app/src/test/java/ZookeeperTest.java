import cn.superid.webapp.zookeeper.Zookeeper;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by xmc1993 on 16/10/18.
 */
public class ZookeeperTest {

    @Test
    public void testZookeeper() throws IOException, KeeperException, InterruptedException {
        Zookeeper zookeeper = new Zookeeper();
        zookeeper.connect();
        zookeeper.watchBackendNodeChange();
        zookeeper.watchConnectorNodeChange();
        zookeeper.close();
    }
}
