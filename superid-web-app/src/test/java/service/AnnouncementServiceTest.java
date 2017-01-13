package service;

import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.forms.ContentState;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jizhenya on 16/9/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class AnnouncementServiceTest {

    @Autowired
    private IAnnouncementService announcementService;

    @Test
    public void testCalucateBlocks(){
        AnnouncementEntity _announcement = AnnouncementEntity.dao.findById(33, 2237);
        String present = _announcement.getContent();
        AnnouncementEntity __announcement = AnnouncementEntity.dao.findById(68, 2298);
        String history = __announcement.getContent();

        ContentState p = JSON.parseObject(present,ContentState.class);
        ContentState h = JSON.parseObject(history,ContentState.class);

        EditDistanceForm result = announcementService.compareTwoPapers(p,h);
        String operations = JSONObject.toJSONString(result);
        System.out.println(operations);
    }





}
