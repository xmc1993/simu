import cn.superid.webapp.utils.AliOssDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by jizhenya on 16/9/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class OSSTest {

    @Test
    public void testOSS(){
        Map<String,String> result = AliOssDao.generateToken("user/101");
        System.out.println(result.get("signature"));
        System.out.println(result.get("expire"));

    }
}
