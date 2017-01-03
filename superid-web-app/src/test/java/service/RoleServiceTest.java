package service;

import cn.superid.webapp.security.AlliancePermissions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by njuTms on 17/1/3.
 */

@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class RoleServiceTest {
    @Test
    public void test(){
        //String permissions = "*";
        //String permissions = "1,2,4,15,17,19";
        //String permissions = "17,19,1,2";
        String permissions = "1,2,4,15,17";
        String toReplace;
        String result = "";
        if ("*".equals(permissions)) {
            result = permissions;
        }
        else if (permissions.contains(AlliancePermissions.ChangeOwner + ",")) {
            toReplace = AlliancePermissions.ChangeOwner + ",";
            result = permissions.replaceAll(toReplace,"");
        }
        else if (permissions.contains("," + AlliancePermissions.ChangeOwner )){
            toReplace = "," + AlliancePermissions.ChangeOwner;
            result = permissions.replaceAll(toReplace,"");
        }
        System.out.println(result);
    }
}
