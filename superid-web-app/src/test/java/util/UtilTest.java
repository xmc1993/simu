package util;

import cn.superid.utils.StringUtil;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by zp on 2016/8/4.
 */
@RunWith(util.JUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all.xml")
public class UtilTest {
    @Test
    public void testCheckFrequency(){
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertFalse(CheckFrequencyUtil.isFrequent("test"));
        Assert.assertTrue(CheckFrequencyUtil.isFrequent("test"));
    }

    @Test
    public void testCheck(){
        Assert.assertFalse(StringUtil.isEmail("15951818230163.com"));
        Assert.assertTrue(StringUtil.isEmail("15951818239@163.com"));
        Assert.assertFalse(StringUtil.isMobile("159518182309"));
        Assert.assertTrue(StringUtil.isMobile("15951818230"));
    }

}
