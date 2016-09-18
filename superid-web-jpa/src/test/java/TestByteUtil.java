import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.util.SerializeUtil;
import com.alibaba.druid.pool.DruidDataSource;
import junit.framework.TestCase;
import model.User;
import org.github.jamm.MemoryMeter;

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



    public void testSize() throws Exception{
        MemoryMeter memoryMeter = new MemoryMeter();


        User user = new User();
        user.setName("src/test");
        user.setAge(18);
        user.save();
        System.out.println(memoryMeter.measureDeep(user));

        HashMap<String, byte[]> hashMap = user.generateHashByteMap();
        System.out.println(memoryMeter.measureDeep(hashMap));

        byte[] serialize = SerializeUtil.serialize(user);
        System.out.println(memoryMeter.measureDeep(serialize));



    }

}
