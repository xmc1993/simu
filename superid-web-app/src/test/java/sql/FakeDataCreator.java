package sql;

import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.utils.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by jizhenya on 16/12/7.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class FakeDataCreator {

    @Test
    public void createFakeAnnouncement(){
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        announcementEntity.setTitle("事务1-1-7公告");
        announcementEntity.setAffairId(7622);
        announcementEntity.setContent("");
        announcementEntity.setThumbContent("小东是我见过最碧浪的人");
        announcementEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setCreatorId(3528);
        announcementEntity.setModifierId(3528);
        announcementEntity.setAllianceId(2198);
        announcementEntity.save();


    }
}
