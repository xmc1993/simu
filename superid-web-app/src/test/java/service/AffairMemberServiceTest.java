package service;

import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.IAffairMemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by njuTms on 16/12/15.
 */

@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:META-INF/spring/spring-all-test.xml"})
public class AffairMemberServiceTest {
    @Autowired
    private IAffairMemberService affairMemberService;
    private long testAllianceId =2198;
    private long testAffairId = 7620;

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

    @Test
    public void testSearchAffairRoles() throws Exception {
        RoleEntity role = RoleEntity.dao.findById(3528L,2198);

        SearchAffairRoleConditions conditions = new SearchAffairRoleConditions();

        List<AffairRoleCard> list1 =affairMemberService.searchAffairRoleCards(testAllianceId,testAffairId,conditions);

        list1 = affairMemberService.searchAffairRoleCards(testAllianceId,testAffairId,conditions);

        conditions.setActive(true);
        list1 = affairMemberService.searchAffairRoleCards(testAllianceId,testAffairId,conditions);

        conditions.setInAlliance(true);
        list1 = affairMemberService.searchAffairRoleCards(testAllianceId,testAffairId,conditions);


        conditions.setKey("zp");
        list1 = affairMemberService.searchAffairRoleCards(testAllianceId,testAffairId,conditions);


    }
}
