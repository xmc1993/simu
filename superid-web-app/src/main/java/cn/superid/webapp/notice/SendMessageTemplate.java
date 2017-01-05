package cn.superid.webapp.notice;

import cn.superid.webapp.notice.thrift.C2c;
import cn.superid.webapp.notice.thrift.NoticeService;
import cn.superid.webapp.notice.zookeeper.NodeUtil;
import com.google.gson.Gson;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

/**
 * Created by xmc1993 on 16/12/19.
 */
public class SendMessageTemplate {
    private static final Integer PORT_DISTANCE = 7; //shrift-server相对于backend-server的端口值差
    private static final int UPDATE_CACHE = 15;//更新缓存(数据类型)
    private static final int MSG = 0;//系统通知(消息类型)

    /**
     * 向eternal模块发送C2c消息
     * @param c2c
     * @return
     * @throws TException
     */
    public static boolean sendNotice(C2c c2c) {
        //首先提取出消息中的affairId
        long affairId = 0L;
        if(c2c.getType() == UPDATE_CACHE){
            ParamVo paramVo = new Gson().fromJson(c2c.getParams(), ParamVo.class);
            affairId = paramVo.getAffairId();
        }else if (c2c.getType() == MSG){
            affairId = c2c.getChat().getAid();
        }
        System.out.println(c2c.getChat().getAid() + "---------------");
        //根据hash算法得到要取得连接的url
        String url;
        try {
            url = NodeUtil.getBackendNodeByKey(affairId);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        String[] params = url.split(":");
        String host = params[0];
        Integer port = Integer.valueOf(params[1]);
        port -= PORT_DISTANCE;

        //获取到相应的连接并发送C2c消息
        NoticeService.Client client = null;
        try {
            client = ThriftPool.getClient(host + ":" + port);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (TTransportException e) {
            e.printStackTrace();
            return false;
        } catch (KeeperException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if(client == null){
            throw new IllegalArgumentException("不存在与参数中host&port对应的连接。");
        }
        synchronized (client){//对于使用同一个client的请求要进行同步
            try {
                return client.sendNotice(c2c);
            } catch (TException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
