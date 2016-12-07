package socket;

import cn.superid.webapp.notice.tcp.Composer;
import org.junit.Test;

/**
 * Created by xmc1993 on 16/12/6.
 */
public class ComposerTest {

    @Test
    public void testCompose(){
        Composer composer = new Composer();
        byte[] resource = {0x10,0x01,0x56,0x09};
        byte[] compose = composer.compose(resource);
        for (byte b : compose) {
            System.out.println(b + " ");
        }
        System.out.println(compose);
    }


    @Test
    public void testFeed() throws Exception {
        Composer composer = new Composer();
        byte[] resource = {0x10,0x01,0x56,0x09};
        byte[] compose = composer.compose(resource);
        byte[] result = composer.feed(compose);
        System.out.println("over");
    }
}
