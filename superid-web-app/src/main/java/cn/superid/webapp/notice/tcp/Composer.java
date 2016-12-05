package cn.superid.webapp.notice.tcp;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by xmc1993 on 16/12/5.
 */
public class Composer {
    private final static Integer ST_LENGTH = 1;
    private final static Integer ST_DATA = 2;
    private final static Integer ST_ERROR = 3;
    private final static Integer DEFAULT_MAX_LENGTH = -1;
    private final static Integer LEFT_SHIFT_BITS = 1 << 7;

    private int offset = 0;
    private int left = 0;
    private int length = 0;
    private int state = ST_LENGTH;
    private int maxLength = DEFAULT_MAX_LENGTH;
    private ByteBuffer buf = null;


    public Composer() {

    }

    public Composer(int maxLength) {
        this.maxLength = maxLength;
    }

    public void compose(String data) {
//        Buffer buffer = new StringBuffer(data, "utf-8");
    }

    private void reset() {
        this.state = ST_LENGTH;
        this.length = 0;
        this.offset = 0;
        this.left = 0;
    }

    public void feed() throws Exception {
        if (this.state == ST_ERROR) {
            throw new Exception("compose in error state, reset it first");
        }
    }

    /**
     * 读取包中保存长度信息的部分
     * @param data
     * @param offset
     * @param end
     * @return 数据的长度????
     */
    private int readLength(ByteBuffer data, int offset, int end) {
        int length = this.length;
        int b = 0;
        boolean finish = false;
        for (int i = 0; i < end; i++) {
            //TODO 读取流中的信息
            length *= LEFT_SHIFT_BITS;
//            length += (b & 0x7f);
            if(this.maxLength > 0 && length > this.maxLength){
                this.state = ST_ERROR;
                //TODO
                return -1;
            }

            if(!((b & 0x80)==0x0)){
                i++;
                finish = true;
                break;
            }
        }

        this.length = length;

        if(finish){
            this.state = ST_DATA;
            this.offset = 0;
            this.left = this.length;
            this.buf = new ByteBuffer(this.length);
        }
    }


    /**
     * 从返回的流中读取data
     *
     * @param data
     * @param offset
     * @param end
     * @return
     */
    private int readData(ByteBuffer data, int offset, int end) {
        int left = end - offset;
        int size = Math.min(left, this.left);
        //TODO
        this.left -= size;
        this.offset += size;

        if (this.left == 0) {
            ByteBuffer buf = this.buf;
            this.reset();
            //TODO get the result
        }

        return offset + size;
    }

    private int calLengthSize(int length) {
        int res = 0;
        while (length > 0) {
            length >>>= 7;
            res++;
        }
        return 0;
    }

    private void fillLength(ByteBuffer buf, int data, int size) {
        int offset = size - 1;
        int index;
        for (; offset >= 0; offset--) {
            index = data % LEFT_SHIFT_BITS;
            if (offset < size - 1) {
                index |= 0x80;
            }
//            buf.
            data >>>= 7;
        }
    }

}
