import cn.superid.jpa.core.impl.JdbcSessionFactory;
import cn.superid.jpa.util.SerializeUtil;
import cn.superid.jpa.util.StringUtil;
import com.alibaba.druid.pool.DruidDataSource;
import junit.framework.TestCase;
import model.User;
import org.github.jamm.MemoryMeter;

import java.sql.Time;
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


    /**
     * -javaagent:/Users/xiaofengxu/.m2/repository/com/github/jbellis/jamm/0.2.6/jamm-0.2.6.jar
     * @throws Exception
     */

    public void testSize() throws Exception{
        MemoryMeter memoryMeter = new MemoryMeter();


        User user = new User();
        user.setName("src/test");
        user.setAge(18);
        user.save();
        System.out.println(memoryMeter.measureDeep(user));

        HashMap<String, byte[]> hashMap = user.generateHashByteMap();
        System.out.println(memoryMeter.measureDeep(hashMap));
        System.out.println(memoryMeter.measureDeep(SerializeUtil.serialize(hashMap)));


        byte[] serialize = SerializeUtil.serialize(user);
        System.out.println(serialize.length);
        System.out.println(memoryMeter.measureDeep(serialize));
        System.out.println(memoryMeter.measure(serialize));


    }

    public void testUtil(){
        Timer timer = new Timer();
        String test = "allianceMember.id";
        System.out.print(StringUtil.underscoreName1(test));
//        for(int i=0;i<100000;i++){
//            StringUtil.underscoreName1(test);
//        }
//        timer.end();
//
//        Timer timer1 = new Timer();
//        for(int i=0;i<100000;i++){
//            StringUtil.underscoreName1(test);
//        }
//        timer1.end();
    }

}
