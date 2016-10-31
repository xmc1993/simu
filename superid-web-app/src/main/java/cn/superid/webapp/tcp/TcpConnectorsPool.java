package cn.superid.webapp.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xmc1993 on 16/10/26.
 */
public final class TcpConnectorsPool {
    private static Map<String, Socket> tcpConnectorsPool = new HashMap<>();//key值为<host:port>

    private TcpConnectorsPool() {

    }

    public static Socket getTcpConnectorByKey(String key){
        Socket socket = tcpConnectorsPool.get(key);
        if(socket.isConnected()){
            return socket;
        }
        return null;
    }

    /**
     * 使用主机地址和端口号获得tcp连接
     * @param host
     * @param port
     * @return
     */
    public static Socket getTcpConnectorByHostAndPort(String host, int port){
        String key = host + ":" + port;
        return getTcpConnectorByKey(key);
    }

    /**
     * 使用主机号和端口号创建新的tcp连接
     * @param host
     * @param port
     * @return
     */
    public static synchronized Socket newTcpConnector(String host, int port){
        String key = host + ":" + port;
        Socket socket = tcpConnectorsPool.get(key);
        if(socket.isConnected()){
            return socket;
        }else {

        }
        Socket client;
        try {
            client = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        tcpConnectorsPool.put(key, client);
        return client;
    }
}
