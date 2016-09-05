import cn.superid.jpa.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by xmc1993 on 16/9/5.
 */
public class TestByteUtil {

//    @Test
//    public void testLong2Byte(){
//        long k = 123L;
//        byte[] bytes = ByteUtil.basicType2Bytes(k);
//        long l = (Long) ByteUtil.bytesToLong(bytes);
//        Assert.assertEquals(k, l);
//    }

    @Test
    public void getData(){
        System.out.println("long");
        System.out.println(Long.BYTES);
        System.out.println("int");
        System.out.println(Integer.BYTES);
        System.out.println("double");
        System.out.println(Double.BYTES);
        System.out.println("float");
        System.out.println(Float.BYTES);
        System.out.println("character");
        System.out.println(Character.BYTES);
        System.out.println("short");
        System.out.println(Short.BYTES);
    }
}
