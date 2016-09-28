
import cn.superid.webapp.enums.MessageColumn;
import cn.superid.webapp.service.IMessageService;
import com.aliyun.openservices.ots.model.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

/**
 * Created by njuTms on 16/9/26.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class OTSTest {
    @Autowired
    private IMessageService messageService;
    @Test
    public void insertTest(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("type",2);
        map.put("title","托马斯");
        map.put("content","test");
        map.put("content","第几条");

        long ts = System.currentTimeMillis();
        messageService.insertIntoTable(1L,1L,map);
    }

    @Test
    public void getTest(){
        List<Row> rows = messageService.getFromTable(1L);
        for(Row row : rows){
            System.out.println(row);
        }
        //List<Row> rows = messageService.getFromTable(1L,2);

        for(Row row : rows){
            System.out.println(row);
        }

        //System.out.println(rows.get(rows.size()-1));
    }

    @Test
    public void getFromTableByTypeTest(){
        messageService.getFromTableByColumnName(1L,1L, MessageColumn.TYPE,2);
    }
}
