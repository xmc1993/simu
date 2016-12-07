package cn.superid.webapp.notice.tcp;

import java.io.UnsupportedEncodingException;

/**
 * Created by xmc1993 on 16/12/5.
 */
public class Composer {
    private final static Integer ST_LENGTH = 1;// state that we should read length
    private final static Integer ST_DATA = 2;// state that we should read length
    private final static Integer ST_ERROR = 3;// state that something wrong has happened
    private final static Integer DEFAULT_MAX_LENGTH = -1;// default max package size: unlimited
    private final static Integer LEFT_SHIFT_BITS = 1 << 7;

    private int offset = 0;
    private int left = 0;
    private int length = 0;
    private int state = ST_LENGTH;
    private int maxLength = DEFAULT_MAX_LENGTH;
    private byte[] buf = null;

    public Composer() {

    }

    public Composer(int maxLength) {
        this.maxLength = maxLength;
    }


    /**
     * 包装数据String类型
     *
     * @param data
     */
    public byte[] compose(String data) throws UnsupportedEncodingException {
        byte[] resource = data.getBytes("utf-8");
        return compose(resource);
    }

    /**
     * 包装数据byte[]数据
     *
     * @param resource
     * @return 包装好的数据
     */
    public byte[] compose(byte[] resource) {
        int lengthSize = calLengthSize(resource.length);
        byte[] res = new byte[lengthSize + resource.length];
        //填充数据包的长度信息
        fillLength(res, resource.length, lengthSize);
        //将源数据也填充到新的byte数组里面
        System.arraycopy(resource, 0, res, lengthSize, resource.length);
        return res;
    }

    /**
     * 向byte数组中写入长度信息
     *
     * @param buf
     * @param length
     * @param size
     */
    private void fillLength(byte[] buf, int length, int size) {
        int offset = size - 1; //偏移量
        Integer value; //需要写入的值
        for (; offset >= 0; offset--) {//对于对于length的每一个size都写入相应的数据
            value = length % LEFT_SHIFT_BITS;
            if (offset < size - 1) {
                value |= 0x80; //最高位要取1 其余的7位代表真实的数值信息
            }
            buf[offset] = value.byteValue();
            length >>>= 7;
        }
    }

    /**
     * 计算长度的规格
     *
     * @param length
     * @return
     */
    private int calLengthSize(int length) {
        int res = 0;
        while (length > 0) {
            length >>>= 7;
            res++;
        }
        return res;
    }

    private void reset() {
        this.state = ST_LENGTH;
        this.buf = null;
        this.length = 0;
        this.offset = 0;
        this.left = 0;
    }


    /**
     * 解开数据
     * @param data
     * @param offset
     * @param end
     * @throws Exception
     */
    public byte[] feed(byte[] data, int offset,int end) throws Exception {
        if (this.state == ST_ERROR) {
            throw new Exception("compose in error state, reset it first");
        }

        while (offset < end){
            if(this.state == ST_LENGTH){
                offset = this.readLength(data, offset, end);
            }
            if(this.state == ST_DATA){
                offset = this.readData(data,offset,end);
            }
            if(this.state == ST_ERROR){
                break;
            }

        }

        return this.buf;
    }

    /**
     * 解开数据
     * @param data
     * @throws Exception
     */
    public byte[] feed(byte[] data) throws Exception {
        if (this.state == ST_ERROR) {
            throw new Exception("compose in error state, reset it first");
        }
        return feed(data, 0, data.length);
    }


    /**
     * 读取数据包的长度信息
     *
     * @param resource
     * @param offset
     * @param end
     * @return
     */
    private int readLength(byte[] resource, int offset, int end) {
        int length = this.length;
        int value;
        boolean finish = false;//读取长度的过程是否结束了

        int i;
        for (i = 0; i < end; i++) {
            value = (int) resource[i + offset];
            //还原原数值
            length *= LEFT_SHIFT_BITS;
            length += (value & 0x7f);

            if (this.maxLength > 0 && length > this.maxLength) {
                this.state = ST_ERROR;
                throw new RuntimeException("包的大小超过了限制!");
            }

            int val = value & 0x80;
            if (((value & 0x80) == 0x00)) {//如果等到的还是长度的信息则继续读取长度信息 否则停止
                i++;
                finish = true;
                break;
            }
        }

        this.length = length;

        if (finish) {
            this.state = ST_DATA;
            this.offset = 0;
            this.left = this.length;
            this.buf = new byte[this.length];
        }

        return i + offset;
    }


    /**
     * 从返回的流中读取data
     *
     * @param resource
     * @param offset
     * @param end
     * @return
     */
    private int readData(byte[] resource, int offset, int end) {
        int left = end - offset;
        int size = Math.min(left, this.left);
        System.arraycopy(resource, offset, this.buf, 0 , size);
        this.left -= size;
        this.offset += size;

//        if (this.left == 0) {
//            this.reset();
//        }

        return offset + size;
    }

}
