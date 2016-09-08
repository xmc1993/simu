import cn.superid.webapp.model.TestEntity;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by xmc1993 on 16/9/8.
 */
public class TestSerializeCapability {

    TestEntity testEntity;

    @Before
    public void setUp(){
        testEntity = new TestEntity();
        testEntity.setId(345L);
        testEntity.setDescription("test");
        testEntity.setCreateRoleId(123L);
        testEntity.setAllianceId(456L);
        testEntity.setIsFree(1);
        testEntity.setIsVideo(0);
    }

    @Test
    public void testSerialize(){

    }



}
