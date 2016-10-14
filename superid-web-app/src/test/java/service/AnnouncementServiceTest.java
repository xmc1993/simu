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
        List<Integer> delete = new ArrayList<>();
        delete.add(1);
        delete.add(2);
        List<EasyBlock> content = new ArrayList<>();
        content.add(new EasyBlock("abc","1"));
        content.add(new EasyBlock("bca","2"));
        List<EasyBlock> content2 = new ArrayList<>();
        content2.add(new EasyBlock("abcd","1"));
        content2.add(new EasyBlock("bcad","2"));
        List<InsertForm> insert = new ArrayList <>();
        insert.add(new InsertForm(1,content));
        insert.add(new InsertForm(2,content2));
        List<ReplaceForm> replaceForms = new ArrayList<>();
        replaceForms.add(new ReplaceForm(1,"ab"));
        replaceForms.add(new ReplaceForm(2,"bc"));
        EditDistanceForm e = new EditDistanceForm(delete,insert,replaceForms);
        String object = JSONObject.toJSONString(e);
        EditDistanceForm ope = JSON.parseObject(object,EditDistanceForm.class);
        System.out.println(object);
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


        List<Integer> delete = new ArrayList<>();
        delete.add(1);
        List<EasyBlock> content = new ArrayList<>();
        content.add(new EasyBlock("abc","4"));
        content.add(new EasyBlock("bca","5"));
        List<EasyBlock> content2 = new ArrayList<>();
        content2.add(new EasyBlock("abcd","6"));
        content2.add(new EasyBlock("bcad","7"));
        List<InsertForm> insert = new ArrayList<>();
        insert.add(new InsertForm(0,content));
        insert.add(new InsertForm(2,content2));
        List<ReplaceForm> replaceForms = new ArrayList<>();
        replaceForms.add(new ReplaceForm(2,"bc"));
        EditDistanceForm e = new EditDistanceForm(delete,insert,replaceForms);
        String operations = JSONObject.toJSONString(e);
        System.out.println(operations);

        String result = announcementService.caulatePaper(str,ss);
        String result2 = announcementService.caulatePaper(result,operations);

        List<Block> present = announcementService.getBlock(JSON.parseObject(result,ContentState.class));
        List<Block> history = announcementService.getBlock(JSON.parseObject(result2,ContentState.class));
        System.out.println(JSONObject.toJSONString(announcementService.compareTwoBlocks(history,present)));

    }
}
