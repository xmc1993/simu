package cn.superid.webapp.notice.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by xmc1993 on 16/10/28.
 */
public class NodeUtil {
    private static final long CAPACITY = 1000L;

    private NodeUtil() {

    }

    /**
     * 根据affairId获取相应的backend服务器地址
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    public static String getBackendNodeByKey(long key) throws InterruptedException, IOException, KeeperException {
        JSONObject backEndsInfo = ZookeeperService.getBackEndsInfo();
        return getNodeByKey(key, backEndsInfo);
    }

    /**
     * 根据affairId获取相应的connector服务器地址
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    public static String getConnectorNodeByKey(long key) throws InterruptedException, IOException, KeeperException {
        JSONObject connectorsInfo = ZookeeperService.getConnectorsInfo();
        return getNodeByKey(key, connectorsInfo);
    }

    /**
     * 以affairId为基础的hash算法
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    private static String getNodeByKey(long key, JSONObject info) throws InterruptedException, IOException, KeeperException {
        int serverCount = info.length();
        Iterator keys = info.keys();
        long[] indexArray = new long[serverCount];
        int i = 0;
        while (keys.hasNext()) {
            indexArray[i++] = Long.valueOf((String) keys.next());
        }
        //预处理key值 保证更好的hash
        if (key < CAPACITY) {
            key = key * (CAPACITY / serverCount - 1);
        }
        Long index = key % CAPACITY;

        //hash处理
        long first = indexArray[0];
        long last = indexArray[indexArray.length - 1];
        if (index < first || index > last || index == last) {
            return (String) info.get(String.valueOf(last));
        }
        for (int j = 0; j < indexArray.length - 1; j++) {
            if (indexArray[j + 1] > index) {
                return (String) info.get(String.valueOf(j));
            }
        }

        return null;
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        String nodeByKey = getConnectorNodeByKey(100L);
        System.out.println(nodeByKey);
    }
}

