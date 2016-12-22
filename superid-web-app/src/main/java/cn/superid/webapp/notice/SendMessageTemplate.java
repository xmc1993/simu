package cn.superid.webapp.notice;

import cn.superid.webapp.notice.thrift.NoticeService;
import cn.superid.webapp.notice.thrift.S2c;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by xmc1993 on 16/12/19.
 */
public class SendMessageTemplate {
    private static final String HOST = "localhost";
    private static final Integer PORT = 9799;
    //多线程下使用Thrift的client会出现包接收错乱的问题因此采用ThreadLocal
    private static ThreadLocal<NoticeService.Client> clients = new ThreadLocal<>();

    private static NoticeService.Client getClient() throws TException {
        NoticeService.Client client = clients.get();
        if (client == null) {
            TTransport transport = new TSocket(HOST, PORT);
            TProtocol protocol = new TBinaryProtocol(transport);
            NoticeService.Client _client = new NoticeService.Client(protocol);
            transport.open();
            clients.set(_client);
        }
        return clients.get();
    }

    public static boolean sendNotice(S2c s2c) throws TException {
        return getClient().sendNotice(s2c);
    }

}
