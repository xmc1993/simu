package service;

import cn.superid.webapp.service.IAffairMemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by njuTms on 16/12/15.
 */

@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class AffairMemberServiceTest {
    @Autowired
    private IAffairMemberService affairMemberService;

    @Test
    public void addMemberTest(){

    }

    @Test
    public void addCreatorTest(){

    }

    @Test
    public void modifyAffairMemberPermissionsTest(){

    }

    @Test
    public void applyForEnterAffairTest(){

    }

    @Test
    public void agreeAffairMemberApplicationTest(){

    }

    @Test
    public void rejectAffairMemberApplicationTest(){

    }

    @Test
    public void inviteToEnterAffair(){

    }

    @Test
    public void agreeInvitationTest(){

    }

    @Test
    public void rejectInvitationTest(){

    }

    @Test
    public void isOwnerOfParentAffair(){

    }
}
