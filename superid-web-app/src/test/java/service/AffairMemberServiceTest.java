package service;

import cn.superid.jpa.util.Pagination;
import cn.superid.webapp.forms.AffairRoleCard;
import cn.superid.webapp.forms.GetRoleCardsMap;
import cn.superid.webapp.forms.SearchAffairMemberConditions;
import cn.superid.webapp.forms.SearchAffairRoleConditions;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.vo.AffairMemberSearchVo;
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
    private long testAllianceId =  2397;
    private long testAffairId =8192L ;

//    @Test
//    public void addMemberTest() {
//    }
//
//    @Test
//    public void addCreatorTest() {
//
//    }
//
//    @Test
//    public void modifyAffairMemberPermissionsTest() {
//
//    }

//    @Test
//    public void applyForEnterAffairTest() {
//
//    }
//
//    @Test
//    public void agreeAffairMemberApplicationTest() {
//
//    }
//
//    @Test
//    public void rejectAffairMemberApplicationTest() {
//
//    }
//
//    @Test
//    public void inviteToEnterAffair() {
//
//    }
//
//    @Test
//    public void agreeInvitationTest() {
//
//    }
//
//    @Test
//    public void rejectInvitationTest() {
//
//    }
//
//    @Test
//    public void isOwnerOfParentAffair() {
//
//    }
//
    @Test
    public void testSearchAffairRoles() throws Exception {
        RoleEntity role = RoleEntity.dao.findById(8192L, 2397);

        SearchAffairRoleConditions conditions = new SearchAffairRoleConditions();

        List<GetRoleCardsMap> list1 = affairMemberService.searchAffairRoleCards(testAllianceId, testAffairId, conditions);

        list1 = affairMemberService.searchAffairRoleCards(testAllianceId, testAffairId, conditions);

        conditions.setActive(true);
        list1 = affairMemberService.searchAffairRoleCards(testAllianceId, testAffairId, conditions);

        conditions.setInAlliance(true);
        list1 = affairMemberService.searchAffairRoleCards(testAllianceId, testAffairId, conditions);


        conditions.setKey("zp");
        list1 = affairMemberService.searchAffairRoleCards(testAllianceId, testAffairId, conditions);


    }

    @Test
    public void searchAffairMembers1() {
        SearchAffairMemberConditions conditions = new SearchAffairMemberConditions();
        conditions.setIncludeSubAffair(true);
        conditions.setSortColumn("level");
        conditions.setCount(190);
        conditions.setPage(1);
        conditions.setNeedTotal(true);
        conditions.setKey("汤");
        conditions.setIsAllianceUser(true);
        Pagination pagination=new Pagination(conditions.getPage(),conditions.getCount(),conditions.isNeedTotal());
        List<AffairMemberSearchVo> list = affairMemberService.searchAffairMembers(testAllianceId, testAffairId, conditions,pagination);
        System.out.println(list);
    }

//    @Test
//    public void searchAffairRoles() {
//        SearchAffairRoleConditions conditions = new SearchAffairRoleConditions();
//        conditions.setInAlliance(true);
//        List<AffairRoleCard> list = affairMemberService.searchAffairRoleCards(testAllianceId,testAffairId,conditions);
//        System.out.println(list);
//    }

}
