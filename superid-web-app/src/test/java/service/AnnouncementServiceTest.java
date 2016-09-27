package service;

import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.service.forms.Block;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jizhenya on 16/9/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class AnnouncementServiceTest {

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
}
