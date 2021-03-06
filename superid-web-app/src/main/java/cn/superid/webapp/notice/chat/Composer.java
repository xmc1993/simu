package cn.superid.webapp.notice.chat;

import cn.superid.webapp.notice.chat.proto.C2C;
import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by xmc1993 on 16/12/5.
 */
public abstract class Composer {
    private final static int ST_LENGTH = 1;// state that we should read length
    private final static int ST_DATA = 2;// state that we should read data
    private final static int ST_ERROR = 3;// state that something wrong has happened
    private final static int DEFAULT_MAX_LENGTH = -1;// default max package size: unlimited
    private final static int LEFT_SHIFT_BITS = 1 << 7;

    private int offset = 0;
    private int left = 0;
    private int length = 0;
    private int state = ST_LENGTH;
    private int maxLength = DEFAULT_MAX_LENGTH;
    private byte[] buf = null;

    private final static Codec<C2C> codec = ProtobufProxy
            .create(C2C.class);

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

    /**
     * 当一个Composer被使用过以后应该使用该方法重新进行重新初始化Composer
     */
    public void reset() {
        this.state = ST_LENGTH;
        this.buf = null;
        this.length = 0;
        this.offset = 0;
        this.left = 0;
    }


    /**
     * 解开数据
     *
     * @param data
     * @param offset
     * @param end
     * @throws Exception
     */
    public void feed(byte[] data, int offset, int end) throws Exception {
        if (this.state == ST_ERROR) {
            throw new Exception("compose in error state, reset it first");
        }

        while (offset < end) {
            if (this.state == ST_LENGTH) {
                offset = this.readLength(data, offset, end);
            }
            if (this.state == ST_DATA) {
                offset = this.readData(data, offset, end);
            }
            if (this.state == ST_ERROR) {
                break;
            }

        }

    }

    /**
     * 解开数据
     *
     * @param data
     * @throws Exception
     */
    public void feed(byte[] data) throws Exception {
        if (this.state == ST_ERROR) {
            throw new Exception("compose in error state, reset it first");
        }
        feed(data, 0, data.length);
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
     * @param resource 源数据
     * @param offset
     * @param end
     * @return
     */
    private int readData(byte[] resource, int offset, int end) {
        int left = end - offset; //end - offset 表示这个数据包还有多少个byte没有读完
        int size = Math.min(left, this.left);//this.left表示当前的tcp包还需要读取多少数据包才完整

        System.arraycopy(resource, offset, this.buf, this.offset, size);
        this.left -= size;
        this.offset += size;

        if (this.left == 0) {
            byte[] bytes = this.buf;
            this.reset();
            try {
                onMessage(codec.decode(bytes));
            } catch (IOException e) {
                System.out.println("wrong format message:" + Arrays.toString(bytes));
            }
        }

        return offset + size; //给出当前数据包没有读完部分的offset
    }

    public abstract void onMessage(C2C c2c) throws IOException;

}
