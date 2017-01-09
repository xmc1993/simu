package cn.superid.webapp.notice.tcp;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * Created by jessiechen on 09/01/17.
 */
public class Message {
    @Protobuf(fieldType = FieldType.STRING, order = 1, required = false)
    public String _id;

    @Protobuf(fieldType = FieldType.UINT32, order = 2, required = false)
    public Integer type; //chat type

    @Protobuf(fieldType = FieldType.UINT64, order = 3, required = false)
    public Long fromUid;

    @Protobuf(fieldType = FieldType.UINT64, order = 4, required = false)
    public Long toUid;

    @Protobuf(fieldType = FieldType.UINT64, order = 5, required = false)
    public Long rid;

    @Protobuf(fieldType = FieldType.UINT64, order = 6, required = false)
    public Long aid;

    @Protobuf(fieldType = FieldType.UINT64, order = 7, required = false)
    public Long toRid;

    @Protobuf(fieldType = FieldType.UINT64, order = 8, required = false)
    public Long frRid;

    @Protobuf(fieldType = FieldType.UINT32, order = 9, required = false)
    public Integer sub;

    @Protobuf(fieldType = FieldType.STRING, order = 10, required = false)
    public String name;

    @Protobuf(fieldType = FieldType.STRING, order = 11, required = false)
    public String content;

    @Protobuf(fieldType = FieldType.UINT64, order = 12, required = false)
    public Long time;

    @Protobuf(fieldType = FieldType.UINT32, order = 13, required = false)
    public Integer index;

    @Protobuf(fieldType = FieldType.UINT64, order = 14, required = false)
    public Long gaid;
}
