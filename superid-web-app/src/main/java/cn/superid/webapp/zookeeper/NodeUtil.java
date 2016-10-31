package cn.superid.webapp.zookeeper;

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
     * 根据affairId获取相应的服务器地址
     *
     * @param key
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    public static String getNodeByKey(long key) throws InterruptedException, IOException, KeeperException {
        JSONObject connectorsInfo = ZookeeperService.getConnectorsInfo();
        int serverCount = connectorsInfo.length();
        Iterator keys = connectorsInfo.keys();
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
            return (String) connectorsInfo.get(String.valueOf(last));
        }
        for (int j = 0; j < indexArray.length - 1; j++) {
            if (indexArray[j + 1] > index) {
                return (String) connectorsInfo.get(String.valueOf(j));
            }
        }

        return null;
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        String nodeByKey = getNodeByKey(100L);
        System.out.println(nodeByKey);
    }
}
