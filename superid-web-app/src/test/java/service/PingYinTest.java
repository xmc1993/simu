package service;

import cn.superid.utils.PingYinUtil;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by jessiechen on 2016/12/29.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class PingYinTest {
    @Test
    public void updateUserNameAbbr() {
        List<UserEntity> userEntities = UserEntity.dao.findListByNativeSql("Select * from user");
        for (UserEntity entity : userEntities) {
            String abbr = PingYinUtil.getFirstSpell(entity.getRealname());
            entity.setNameAbbr(abbr);
            entity.update();
        }
    }

    @Test
    public void updateRoleTitleAbbr() {
        List<RoleEntity> entities = RoleEntity.dao.findListByNativeSql("Select * from role");
        for (RoleEntity entity : entities) {
            String abbr = PingYinUtil.getFirstSpell(entity.getTitle());
            entity.setTitleAbbr(abbr);
            entity.update();
        }
    }

    @Test
    public void updateAffairNameAbbr() {
        List<AffairEntity> entities = AffairEntity.dao.findListByNativeSql("Select * from affair");
        for (AffairEntity entity : entities) {
            if(StringUtil.isEmpty(entity.getName())){
                continue;
            }
            String abbr = PingYinUtil.getFirstSpell(entity.getName());
            entity.setNameAbbr(abbr);
            entity.update();
        }
    }


}
