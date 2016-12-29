package service;

import cn.superid.utils.PingYinUtil;
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
        List<UserEntity> userEntities = UserEntity.dao.findList("Select * from user");
        for (UserEntity entity : userEntities) {
            String abbr = PingYinUtil.getFirstSpell(entity.getUsername());
            entity.setNameAbbr(abbr);
            entity.update();
        }
    }

    @Test
    public void updateRoleTitleAbbr() {
        List<RoleEntity> entities = RoleEntity.dao.findList("Select * from role");
        for (RoleEntity entity : entities) {
            String abbr = PingYinUtil.getFirstSpell(entity.getTitle());
            entity.setTitleAbbr(abbr);
            entity.update();
        }
    }

    @Test
    public void updateAffairNameAbbr() {
        List<AffairEntity> entities = AffairEntity.dao.findList("Select * from affair");
        for (AffairEntity entity : entities) {
            String abbr = PingYinUtil.getFirstSpell(entity.getName());
            entity.setNameAbbr(abbr);
            entity.update();
        }
    }


}
