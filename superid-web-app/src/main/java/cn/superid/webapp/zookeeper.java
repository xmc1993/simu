package cn.superid.webapp;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * Created by xmc1993 on 16/10/17.
 */
public class zookeeper {
    public static void watch() throws Exception{
        final ZooKeeper zk = new ZooKeeper("localhost:" + 8080,
                20000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                System.out.println("已经触发了" + event.getType() + "事件！");
            }
        });
        zk.exists("/connectors", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.EventType type = event.getType();
                if(type == Event.EventType.NodeDataChanged){
                    System.out.println("connectors的数据发生变化");
                }
            }
        });

        zk.exists("/backends", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                Event.EventType type = event.getType();
                if(type == Event.EventType.NodeDataChanged){
                    System.out.println("backends的数据发生变化");
                }
            }
        });
        zk.close();
    }
}
