package socket;

import cn.superid.webapp.notice.tcp.Composer;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Created by xmc1993 on 16/12/6.
 */
public class ComposerTest {

    @Test
    public void testSeparatedPackage
            () throws Exception {
        Composer composer = new Composer() {
            @Override
            public void onMessage(byte[] bytes) {
                System.out.println("on message:" + new String(bytes, StandardCharsets.UTF_8));
            }
        };
        String resource = "{\"fromId\":}";
        byte[] compose = composer.compose(resource.getBytes("utf-8"));

        byte[] bytes = new byte[5];
        int length = compose.length;
        byte[] bytes1 = new byte[length - 5];
        System.arraycopy(compose, 0, bytes, 0, 5);
        System.arraycopy(compose, 5, bytes1, 0, length - 5);

//        composer.feed(compose);
        composer.feed(bytes);
        composer.feed(bytes1);

    }

    @Test
    public void testCombinePackage() throws Exception {
        Composer composer = new Composer() {
            @Override
            public void onMessage(byte[] bytes) {
                try {
                    String s = new String(bytes, "utf-8");
                    System.out.println(s);
                    System.out.println("--------------不同包的分隔符---------");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
        String resource1 = "dasdsagdaskdsgkafdkgfdl ldashkfgdslgfldalsflsahdlfjadhsfhjkasdlhfhlshjfjhlha bljkjdfsahfjhfhlhls";
        String resource2 = "    // Positional Access Operations\n" +
                "\n" +
                "    @SuppressWarnings(\"unchecked\")\n" +
                "    E elementData(int index) {\n" +
                "        return (E) elementData[index];\n" +
                "    }\n" +
                "\n" +
                "    /** \n" +
                "     * Returns the element at the specified position in this list.\n" +
                "     *\n" +
                "     * @param  index index of the element to return\n" +
                "     * @return the element at the specified position in this list\n" +
                "     * @throws IndexOutOfBoundsException {@inheritDoc}\n" +
                "     */\n" +
                "    public E get(int index) {\n" +
                "        rangeCheck(index);\n" +
                "\n" +
                "        return elementData(index);\n" +
                "    }";
        byte[] bytes1 = composer.compose(resource1.getBytes("utf-8"));
        byte[] bytes2 = composer.compose(resource2.getBytes("utf-8"));
        int length = bytes1.length;
        int length1 = bytes2.length;
        byte[] bytes = new byte[length + length1];
        System.arraycopy(bytes1, 0, bytes, 0, length);
        System.arraycopy(bytes2, 0, bytes, length, length1);
        composer.feed(bytes);

    }
}
