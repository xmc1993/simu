import cn.superid.jpa.orm.SQLDao;
import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.forms.AffairInfo;
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
//            affairService.createRootAffair(7L,"三号七年",5L,1);
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

    @Test
    public void disableAffairTest(){
        try {
            affairService.disableAffair(43L,1070L);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void validAffairTest(){
        try {
            affairService.validAffair(43L,1070L);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void modifyAffairInfoTest(){
        try {
            //affairService.modifyAffairInfo(43L,1070L,1,1);
            //affairService.modifyAffairInfo(43L,1070L,2,"peng");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void isChildTest(){

        System.out.println(affairService.isChildAffair(1185L,5573L,5572L));
    }
    @Test
    public void test(){
        /*
        //String mobile = "+86 15358381990";
        String mobile = "999912";
        String[] strs = mobile.split("\\s+");
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(mobile);
        System.out.println(matcher.matches());
        */
        StringBuilder sb = new StringBuilder("");
        List<Expr> exprs = new ArrayList<>();
        String path = "/1-1-2-11";
        int level = 4;
        Object[] paths = new Object[level];
        String[] indexs = path.split("-");
        paths[0] = indexs[0];
        sb.append(indexs[0]);
        if(indexs.length>1){
            for(int i=1;i<level;i++){
                sb.append("-"+indexs[i]);
                paths[i] = sb.toString();
            }
        }

        List<AffairEntity> affairEntities = AffairEntity.dao.partitionId(1185).in("path",paths).selectList("id","path");
        System.out.println();
    }

    @Test
    public void testGetOutAllianceAffairs(){
        List<AffairInfo> result = new ArrayList<>();
        long userId = 1899;
        //先从角色表中找到该用户的所有角色所在的盟,然后在affairMember表中找到不在之前盟中的角色的affairMember的affairId,最后从Affair中取
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(userId);
        p.addIndexBinding(userId);
        long startTime = System.currentTimeMillis();
        result = AffairEntity.getSession().findList(AffairEntity.class, SQLDao.GET_OUT_ALLIANCE_AFFAIRS,p);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime-startTime);
    }
}
