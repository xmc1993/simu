package service;

import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.controller.AllianceController;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAllianceService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.MockClient;

/**
 * Created by xiaofengxu on 16/9/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class AllianceTest {
    @Autowired
    private IAllianceService allianceService;
    @Autowired
    private AllianceController allianceController;
//
//    @Test
//    public void testIsINSame(){
//        GlobalValue.currentAffairId();
//    }

    @Test
    public void testCreateAlliance() {
        String code = StringUtil.randomString(6);
        SimpleResponse simpleResponse = allianceController.createAlliance("SIMU", "a,b,c","xxx");
        AllianceEntity allianceEntity = (AllianceEntity) simpleResponse.getData();

        Assert.assertTrue(allianceEntity.getName().equals("SIMU"));

        SimpleResponse simpleResponse1 = allianceController.createAlliance("repeat", "asa,c","ssss");
        Assert.assertTrue(simpleResponse1.getData().equals("repeat_code"));

    }



}
