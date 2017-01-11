package cn.superid.webapp.notice.chat.proto;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Created by jessiechen on 09/01/17.
 */
public class C2C {

    @Protobuf(fieldType = FieldType.UINT32, order = 1, required = true)
    private Integer type;

    @Protobuf(fieldType = FieldType.OBJECT, order = 2, required = false)
    private Message chat;

    @Protobuf(fieldType = FieldType.STRING, order = 3, required = false)
    private String params;

    @Protobuf(fieldType = FieldType.OBJECT, order = 4, required = false)
    private List<Message> msgList;

    @Protobuf(fieldType = FieldType.STRING, order = 5, required = false)
    private String requestId;

    @Protobuf(fieldType = FieldType.STRING, order = 6, required = false)
    private String data;

    public C2C() {
    }

    public C2C(Integer type, String requestId) {
        this.type = type;
        this.requestId = requestId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Message getChat() {
        return chat;
    }

    public void setChat(Message chat) {
        this.chat = chat;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public List<Message> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Message> msgList) {
        this.msgList = msgList;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
