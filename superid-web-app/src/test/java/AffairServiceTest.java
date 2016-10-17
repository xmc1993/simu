import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.service.IAffairService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by njuTms on 16/9/8.
 */

@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class AffairServiceTest {
    @Autowired
    private IAffairService affairService;

    @Test
    public void createRootAffairTest(){
        try{
            affairService.createRootAffair(7L,"三号七年",5L,1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getAllChildAffairTest(){
        try{
            affairService.getAllDirectChildAffair(7L,44L);
            String test = "0-1-2-1";
            System.out.println(test.substring(3));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void moveAffairTest(){
        try{
            //affairService.moveAffair(43L,1070L,1050L);
            /*
            List<AffairEntity> result_1 = new ArrayList<>();


            long st = System.currentTimeMillis();
            result_1 = affairService.getAllChildAffairs(result_1,43L,1050L);
            long et = System.currentTimeMillis();
            System.out.println("递归时间"+(et-st));
*/
            List<AffairEntity> result_2 = new ArrayList<>();
            long st = System.currentTimeMillis();
            result_2 = affairService.getAllChildAffairs(43L,1050L,"level","path");
            long et = System.currentTimeMillis();
            System.out.println("方法时间"+(et-st));

            System.out.println();
        }catch (Exception e){

        }
    }
}
