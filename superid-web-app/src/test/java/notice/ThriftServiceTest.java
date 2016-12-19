package notice;

import cn.superid.webapp.notice.thrift.NoticeService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by xmc1993 on 16/12/13.
 */
public class ThriftServiceTest {

    public static void main(String[] args) throws TException {
        TTransport transport = new TSocket("localhost", 9799);
        TProtocol protocol = new TBinaryProtocol(transport);
        NoticeService.Client client = new NoticeService.Client(protocol);
        transport.open();
//        C2c c2c = new C2c();
//        c2c.setRequestId("test123");
//        c2c.setType(10);
//        client.atSomeone(c2c);
//        System.out.println(client.systemNotice(c2c));
//        transport.close();

    }
}
