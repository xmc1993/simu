package cn.superid.webapp.utils;

import com.aliyun.openservices.ots.*;
import com.aliyun.openservices.ots.model.*;
import org.omg.CORBA.ORB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by njuTms on 16/9/26.
 */
public class AliOTSDao {
    private static final Logger LOG = LoggerFactory.getLogger(AliOTSDao.class);
    private final static String endpoint ="http://superid2-0.cn-shanghai.ots.aliyuncs.com";
    private final static String accessId ="LTAIJ1mKhbLQyBnK";
    private final static String accessKey="DHnYFEI9gF1CMhh1fzSlwj8MkjjPnK";
    private final static String instanceName = "superid2-0";


    public final static OTSClient otsClient = new OTSClient(endpoint,accessId,accessKey,instanceName);

    public static void createTable(OTSClient client, String tableName)
            throws ServiceException, ClientException{
        TableMeta tableMeta = new TableMeta(tableName);
        tableMeta.addPrimaryKeyColumn("pid0", PrimaryKeyType.INTEGER);
        tableMeta.addPrimaryKeyColumn("pid1", PrimaryKeyType.INTEGER);
        // 将该表的读写CU都设置为0
        CapacityUnit capacityUnit = new CapacityUnit(0, 0);

        CreateTableRequest request = new CreateTableRequest();
        request.setTableMeta(tableMeta);
        request.setReservedThroughput(capacityUnit);
        client.createTable(request);

        System.out.println("表已创建");
    }

    public static void deleteTable(OTSClient client, String tableName)
            throws ServiceException, ClientException{
        DeleteTableRequest request = new DeleteTableRequest();
        request.setTableName(tableName);
        client.deleteTable(request);

        System.out.println("表已删除");
    }

    public static void shutDownClient(OTSClient client)
            throws ServiceException, ClientException{
        client.shutdown();
    }
    /*
    public static void main(String args[]) {
        final String tableName = "sampleTable";

        try{
            // 创建表
            createTable(otsClient, tableName);

            // 注意：创建表只是提交请求，OTS创建表需要一段时间。
            // 这里简单地等待2秒，请根据您的实际逻辑修改。
            Thread.sleep(2000);
        }catch(ServiceException e){
            System.err.println("操作失败，详情：" + e.getMessage());
            // 可以根据错误代码做出处理， OTS的ErrorCode定义在OTSErrorCode中。
            if (OTSErrorCode.QUOTA_EXHAUSTED.equals(e.getErrorCode())){
                System.err.println("超出存储配额。");
            }
            // Request ID可以用于有问题时联系客服诊断异常。
            System.err.println("Request ID:" + e.getRequestId());
        }catch(ClientException e){
            // 可能是网络不好或者是返回结果有问题
            System.err.println("请求失败，详情：" + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        finally{
            // 不留垃圾。
            try {
                deleteTable(otsClient, tableName);
            } catch (ServiceException e) {
                System.err.println("删除表格失败，原因：" + e.getMessage());
                e.printStackTrace();
            } catch (ClientException e) {
                System.err.println("删除表格请求失败，原因：" + e.getMessage());
                e.printStackTrace();
            }
            otsClient.shutdown();
        }
    }


    */

}
