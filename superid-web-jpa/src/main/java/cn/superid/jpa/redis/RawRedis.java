package cn.superid.jpa.redis;

import redis.clients.jedis.*;
import redis.clients.util.RedisOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.net.Socket;
import java.io.IOException;
import java.net.URI;


/**
 * Created by xiaofengxu on 16/9/20.
 */
public class RawRedis extends Jedis {
    private static RedisOutputStream redisOutputStream;

    public RawRedis() {
    }

    public RawRedis(String host) {
        super(host);
    }

    public RawRedis(String host, int port) {
        super(host, port);
    }

    public RawRedis(String host, int port, boolean ssl) {
        super(host, port, ssl);
    }

    public RawRedis(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public RawRedis(String host, int port, int timeout) {
        super(host, port, timeout);
    }

    public RawRedis(String host, int port, int timeout, boolean ssl) {
        super(host, port, timeout, ssl);
    }

    public RawRedis(String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, timeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public RawRedis(String host, int port, int connectionTimeout, int soTimeout) {
        super(host, port, connectionTimeout, soTimeout);
    }

    public RawRedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl) {
        super(host, port, connectionTimeout, soTimeout, ssl);
    }

    public RawRedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, connectionTimeout, soTimeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public RawRedis(JedisShardInfo shardInfo) {
        super(shardInfo);
    }

    public RawRedis(URI uri) {
        super(uri);
    }

    public RawRedis(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public RawRedis(URI uri, int timeout) {
        super(uri, timeout);
    }

    public RawRedis(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public RawRedis(URI uri, int connectionTimeout, int soTimeout) {
        super(uri, connectionTimeout, soTimeout);
    }

    public RawRedis(URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, connectionTimeout, soTimeout, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public void hmset(final byte[][] hash){

        try {
            Client client = this.getClient();
            client.connect();
            Socket socket =this.getClient().getSocket();
            RedisOutputStream redisOutputStream = new RedisOutputStream(socket.getOutputStream());
            Protocol.sendCommand(redisOutputStream, Protocol.Command.HMSET,hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
