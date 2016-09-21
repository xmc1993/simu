import cn.superid.webapp.service.IAffairService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by njuTms on 16/9/8.
 */

@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class AffairServiceTest {
    @Autowired
    private IAffairService affairService;
    @Test
    public void applyForAffairTest(){
        try {
            affairService.applyForEnterAffair(5L,8L,4L);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void rejectApplicationTest(){
        try{
            affairService.rejectAffairMemberApplication(5L,8L,4L,2L,"不爽");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void agreeApplicationTest() {
        try {
            affairService.agreeAffairMemberApplication(5L, 8L, 5L, 2L, "看你爽");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createRootAffairTest(){
        try{
            affairService.createRootAffair(7L,"三号七年",5L,1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
