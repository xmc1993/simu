package service;

import cn.superid.webapp.service.IFriendsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by njuTms on 16/10/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class FriendsServiceTest {
    @Autowired
    private IFriendsService friendsService;

    @Test
    public void testApplyForFriend(){
        //friendsService.applyForFriend(1131L,1133L,"你长得真好看");
        friendsService.applyForFriend(1133L,1131L,"啊我认错人了,你长得真好看");
    }

    @Test
    public void testReject(){
        friendsService.rejectFriendApplication(1,1133L,"你太丑了");
    }

    @Test
    public void testAccept(){
        friendsService.acceptFriendApplication(3,1131L,"我原谅你了");
    }
}
