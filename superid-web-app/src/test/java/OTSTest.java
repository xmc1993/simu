
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
        map.put("type",1);
        map.put("sub_type",1);
        map.put("title","2");
        map.put("content","test");

        long st = System.currentTimeMillis();
        messageService.insertIntoTable(1L,3L,1L,map);
        long et = System.currentTimeMillis();
        System.out.println("时间"+(et-st)/1000);
    }

    @Test
    public void getTest(){
        //List<Row> rows = messageService.getFromTable(1L);
        long st = System.currentTimeMillis();
        List<Row> rows = messageService.getFromTable(1L,1L);
        long et = System.currentTimeMillis();
        System.out.println("时间"+(et-st));
        for(Row row : rows){
            System.out.println(row);
        }

        //System.out.println(rows.get(rows.size()-1));
    }

    @Test
    public void getFromTableByTypeTest(){
        for(int i = 0;i<10;i++){
            long st = System.currentTimeMillis();
            List<Row> rows = messageService.getFromTableByColumnName(1L,null,null, MessageColumn.TYPE,1);
            long et = System.currentTimeMillis();
            System.out.println("时间"+(et-st));
        }



    }
}
