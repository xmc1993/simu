package socket;

import cn.superid.webapp.notice.tcp.Callback;
import cn.superid.webapp.notice.tcp.Composer;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

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

            }
        };
        String resource = "    /**\n" +
                "     * Returns an array containing all of the elements in this list\n" +
                "     * in proper sequence (from first to last element).\n" +
                "     *\n" +
                "     * <p>The returned array will be \"safe\" in that no references to it are\n" +
                "     * maintained by this list.  (In other words, this method must allocate\n" +
                "     * a new array).  The caller is thus free to modify the returned array.\n" +
                "     *\n" +
                "     * <p>This method acts as bridge between array-based and collection-based\n" +
                "     * APIs.\n" +
                "     *\n" +
                "     * @return an array containing all of the elements in this list in\n" +
                "     *         proper sequence\n" +
                "     */\n" +
                "    public Object[] toArray() {\n" +
                "        return Arrays.copyOf(elementData, size);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * Returns an array containing all of the elements in this list in proper\n" +
                "     * sequence (from first to last element); the runtime type of the returned\n" +
                "     * array is that of the specified array.  If the list fits in the\n" +
                "     * specified array, it is returned therein.  Otherwise, a new array is\n" +
                "     * allocated with the runtime type of the specified array and the size of\n" +
                "     * this list.\n" +
                "     *\n" +
                "     * <p>If the list fits in the specified array with room to spare\n" +
                "     * (i.e., the array has more elements than the list), the element in\n" +
                "     * the array immediately following the end of the collection is set to\n" +
                "     * <tt>null</tt>.  (This is useful in determining the length of the\n" +
                "     * list <i>only</i> if the caller knows that the list does not contain\n" +
                "     * any null elements.)\n" +
                "     *\n" +
                "     * @param a the array into which the elements of the list are to\n" +
                "     *          be stored, if it is big enough; otherwise, a new array of the\n" +
                "     *          same runtime type is allocated for this purpose.\n" +
                "     * @return an array containing the elements of the list\n" +
                "     * @throws ArrayStoreException if the runtime type of the specified array\n" +
                "     *         is not a supertype of the runtime type of every element in\n" +
                "     *         this list\n" +
                "     * @throws NullPointerException if the specified array is null\n" +
                "     */\n" +
                "    @SuppressWarnings(\"unchecked\")\n" +
                "    public <T> T[] toArray(T[] a) {\n" +
                "        if (a.length < size)\n" +
                "            // Make a new array of a's runtime type, but my contents:\n" +
                "            return (T[]) Arrays.copyOf(elementData, size, a.getClass());\n" +
                "        System.arraycopy(elementData, 0, a, 0, size);\n" +
                "        if (a.length > size)\n" +
                "            a[size] = null;\n" +
                "        return a;\n" +
                "    }\n" +
                "\n" +
                "    // Positional Access Operations\n" +
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
        String resource2 =                 "    // Positional Access Operations\n" +
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
