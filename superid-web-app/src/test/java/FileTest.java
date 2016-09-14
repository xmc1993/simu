import cn.superid.webapp.service.IFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jizhenya on 16/9/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class FileTest {

    @Autowired
    IFileService fileService;

    @Test
    public void testAddFolder(){

        boolean result = fileService.addFolder(1,"第二个文件夹",1,1,0);
        System.out.println(result);


    }


}
