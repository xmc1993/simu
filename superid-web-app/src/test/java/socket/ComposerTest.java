package socket;

import cn.superid.webapp.notice.chat.proto.C2C;
import cn.superid.webapp.notice.tcp.Composer;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by xmc1993 on 16/12/6.
 */
public class ComposerTest {

    private final static Codec<C2C> codec = ProtobufProxy
            .create(C2C.class);

//    @Test
//    public void testSeparatedPackage() throws Exception {
//        Composer composer = new Composer() {
//            @Override
//            public void onMessage(C2C c2C) {
//                System.out.println("on message:" + c2C);
//            }
//        };
//        Message message = new Message();
//        message.content = "hi";
//        C2C c2c = new C2C(C2CType.MARK_READ_TIME, "mookRequestId");
//        c2c.setChat(message);
//        byte[] compose = composer.compose(codec.encode(c2c));
//
//        byte[] bytes = new byte[5];
//        int length = compose.length;
//        byte[] bytes1 = new byte[length - 5];
//        System.arraycopy(compose, 0, bytes, 0, 5);
//        System.arraycopy(compose, 5, bytes1, 0, length - 5);
//
////        composer.feed(compose);
//        composer.feed(bytes);
//        composer.feed(bytes1);
//
//    }
//
//    @Test
//    public void testCombinePackage() throws Exception {
//        Composer composer = new Composer() {
//            @Override
//            public void onMessage(C2C c2c) {
//                System.out.println("on message:" + c2c);
//                System.out.println("--------------不同消息的分隔符---------");
//
//            }
//        };
//        String resource1 = "dasdsagdaskdsgkafdkgfdl ldashkfgdslgfldalsflsahdlfjadhsfhjkasdlhfhlshjfjhlha bljkjdfsahfjhfhlhls";
//        String resource2 = "    // Positional Access Operations\n" +
//                "\n" +
//                "    @SuppressWarnings(\"unchecked\")\n" +
//                "    E elementData(int index) {\n" +
//                "        return (E) elementData[index];\n" +
//                "    }\n" +
//                "\n" +
//                "    /** \n" +
//                "     * Returns the element at the specified position in this list.\n" +
//                "     *\n" +
//                "     * @param  index index of the element to return\n" +
//                "     * @return the element at the specified position in this list\n" +
//                "     * @throws IndexOutOfBoundsException {@inheritDoc}\n" +
//                "     */\n" +
//                "    public E get(int index) {\n" +
//                "        rangeCheck(index);\n" +
//                "\n" +
//                "        return elementData(index);\n" +
//                "    }";
//        byte[] bytes1 = composer.compose(resource1.getBytes("utf-8"));
//        byte[] bytes2 = composer.compose(resource2.getBytes("utf-8"));
//        int length = bytes1.length;
//        int length1 = bytes2.length;
//        byte[] bytes = new byte[length + length1];
//        System.arraycopy(bytes1, 0, bytes, 0, length);
//        System.arraycopy(bytes2, 0, bytes, length, length1);
//        composer.feed(bytes);
//
//    }

    @Test
    public void testLength() {
        Composer composer = new Composer() {
            @Override
            public void onMessage(C2C c2c) throws IOException {

            }
        };
        byte[] a = new byte[150];
        Arrays.fill(a, 0, 149, (byte) 1);
        byte[] b = composer.compose(a);
        try {
            composer.feed(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
