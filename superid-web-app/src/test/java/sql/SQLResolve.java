package sql;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.service.IAffairService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by jizhenya on 16/11/25.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class SQLResolve {

    @Test
    public void fixUserSuperid(){
        StringBuilder sb = new StringBuilder("select * from user");
        ParameterBindings p = new ParameterBindings();
        List<UserEntity> userList = UserEntity.dao.findList(sb.toString(),p);
        for(UserEntity u : userList){
            long id = u.getId();
            String superid = id+"";
            int length = 8 - superid.length();
            for(int i = 0 ; i<length ; i++){
                superid = "0"+superid;
            }
            u.setSuperid(superid);
            u.update();
        }
    }

    @Test
    public void cleanDirtyData(){
        //用法:打开阿里云sql,直接按顺序复制运行,条件自己变
        //第一步,清除脏affair_user
        StringBuilder cleanAffairUser = new StringBuilder("delete from affair_user where affair_id in (select id from affair where alliance_id in (select id from alliance where name = '12'))");
        //第二步,清除脏affair_member
        StringBuilder cleanAffairMember = new StringBuilder("delete from affair_member where affair_id in (select id from affair where alliance_id in (select id from alliance where name = '12'))");
        //第三步,清除脏的affair
        StringBuilder cleanAffair = new StringBuilder("delete from affair  where alliance_id in (select id from alliance where name = '12')");
        //第四步,清除脏alliance
        StringBuilder cleanAlliance = new StringBuilder("delete from alliance where name = '12'");

    }


}
