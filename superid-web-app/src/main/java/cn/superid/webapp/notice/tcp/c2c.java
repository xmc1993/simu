package cn.superid.webapp.notice.tcp;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.util.List;

/**
 * Created by jessiechen on 09/01/17.
 */
public class c2c {

    @Protobuf(fieldType = FieldType.UINT32, order = 1, required = true)
    public Integer type;

    @Protobuf(fieldType = FieldType.OBJECT, order = 2, required = false)
    public Message chat;

    @Protobuf(fieldType = FieldType.STRING, order = 3, required = false)
    public String params;

    @Protobuf(fieldType = FieldType.OBJECT, order = 4, required = false)
    public List<Message> msgList;

    @Protobuf(fieldType = FieldType.STRING, order = 5, required = false)
    public String requestId;

    @Protobuf(fieldType = FieldType.STRING, order = 6, required = false)
    public String data;
}
