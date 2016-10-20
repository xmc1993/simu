import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IPictureService;
import cn.superid.webapp.utils.AliOssDao;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.io.stream.ByteBufferStreamInput;
import org.elasticsearch.common.io.stream.InputStreamStreamInput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * Created by jizhenya on 16/9/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring-all-test.xml")
public class OSSTest {

    @Autowired
    private IPictureService pictureService;

    @Test
    public void testOSS(){
        Map<String,String> result = AliOssDao.generateToken("user/101");
        System.out.println(result.get("signature"));
        System.out.println(result.get("expire"));
        System.out.println(result.get("host"));
    }

    @Test
    public void testUpload(){
        File f = new File("/Users/jizhenya/Desktop/avatarjzy.png");
        PutObjectResult p = AliOssDao.uploadFile(f,"user/101/"+f.getName());
        System.out.println(p.getETag());
    }

    @Test
    public void testResize(){
        File f = new File("/Users/jizhenya/Desktop/avatarjzy.png");
        try{
            BufferedImage sourceImage = ImageIO.read(new FileInputStream(f));
            double width = sourceImage.getWidth();
            double height = sourceImage.getHeight();

            ByteArrayOutputStream resizeOut = new ByteArrayOutputStream();

            pictureService.resizePicture(new FileInputStream(f),resizeOut,200,(int)(200*height/width));
            File tmpFile = File.createTempFile("tem2", ".png");
            IOUtils.write(resizeOut.toByteArray(), new FileOutputStream(tmpFile));
            PutObjectResult p = AliOssDao.uploadFile(tmpFile,"user/101/"+f.getName());
            System.out.println(p.getETag());
        }catch(Exception e){

        }
    }
}
