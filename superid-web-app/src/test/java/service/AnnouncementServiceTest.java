package service;

import cn.superid.webapp.controller.forms.EasyBlock;
import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.controller.forms.InsertForm;
import cn.superid.webapp.controller.forms.ReplaceForm;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.forms.Block;
import cn.superid.webapp.service.forms.ContentState;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class AnnouncementServiceTest {

    @Autowired
    private IAnnouncementService announcementService;

    @Test
    public void testJson(){
        String str = AnnouncementEntity.dao.findById(1,1).getContent();
        JSONObject j = JSON.parseObject(str);
        JSONArray jj = j.getJSONArray("blocks");
        for(int i = 0 ; i < jj.size() ; i++){
            JSONObject b = jj.getJSONObject(i);
            Block bs = new Block(b.getString("key"),b.getString("text"),i);
            System.out.println(bs.getKey()+" "+bs.getContent()+ " "+bs.getLocation());
        }
    }

    @Test
    public void testObjectToJson(){
    }

    @Test
    public void testJsonToObject(){
        String str = AnnouncementEntity.dao.findById(1,1).getContent();
        ContentState total = JSON.parseObject(str,ContentState.class);
    }

    @Test
    public void testCaulatePaper(){
        AnnouncementEntity a = AnnouncementEntity.dao.findById(1,1);
        String str = a.getContent();
        String ss = a.getDecrement();
    }

    @Test
    public void testCalucateBlocks(){
        String present = AnnouncementEntity.dao.findById(33,2237).getContent();
        String history = AnnouncementEntity.dao.findById(68,2298).getContent();

        ContentState p = JSON.parseObject(present,ContentState.class);
        ContentState h = JSON.parseObject(history,ContentState.class);

        EditDistanceForm result = announcementService.compareTwoPapers(p,h);
        String operations = JSONObject.toJSONString(result);
        System.out.println(operations);
    }



}
