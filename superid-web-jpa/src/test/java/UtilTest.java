import cn.superid.jpa.orm.ModelMeta;
import model.User;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaofengxu on 17/1/4.
 */
public class UtilTest {

    private static Map<String,String> map = new HashMap<>();

    @Test
    public void testName() throws Exception{
        ModelMeta modelMeta = ModelMeta.getModelMeta(User.class);


        Method method = User.class.getDeclaredMethod("isOld");

        Method method1 = User.class.getDeclaredMethod("setOld",boolean.class);


        int a =1;
    }




}
