package service;

import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAllianceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by xiaofengxu on 16/9/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class AllianceServiceTest {
    @Autowired
    private IAllianceService allianceService;

    @Test
    public void testIsINSame(){
        GlobalValue.currentAffairId();
    }
}
