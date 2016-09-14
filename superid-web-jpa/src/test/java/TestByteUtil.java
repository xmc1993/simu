import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.util.ByteUtil;
import cn.superid.jpa.util.ObjectSizeFetcher;
import cn.superid.jpa.util.SerializeUtil;
import com.alibaba.druid.pool.DruidDataSource;
import junit.framework.TestCase;
import model.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by xmc1993 on 16/9/5.
 */
public class TestByteUtil extends TestCase {

//    @Test
//    public void testLong2Byte(){
//        long k = 123L;
//        byte[] bytes = ByteUtil.basicType2Bytes(k);
//        long l = (Long) ByteUtil.bytesToLong(bytes);
//        Assert.assertEquals(k, l);
//    }

    public static DruidDataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("superid");
        dataSource.setPassword("superid");
        dataSource.setUrl("jdbc:mysql://rm-bp1943x791y4e3z21.mysql.rds.aliyuncs.com/jpa");
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(1000);
        return dataSource;
    }

    static {
        JdbcSessionFactory jdbcSessionFactory = new JdbcSessionFactory(getDataSource());
    }


    public static long getByteSize(Object o){
        System.gc();
        long begin = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
         o=null;
        System.gc();
        long end = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(begin-end);
        return begin-end;
    }

    public void testSize(){

        System.out.println(ObjectSizeFetcher.getObjectSize(new User()));


    }
}
