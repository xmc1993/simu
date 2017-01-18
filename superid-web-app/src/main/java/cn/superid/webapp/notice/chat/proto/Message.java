package cn.superid.webapp.notice.chat.proto;

import cn.superid.webapp.notice.chat.Constant.MessageType;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by jessiechen on 09/01/17.
 */
public class Message {
    @Protobuf(fieldType = FieldType.STRING, order = 1, required = false)
    private String _id;

    @Protobuf(fieldType = FieldType.UINT32, order = 2, required = false)
    private Integer type; //chat type

    @Protobuf(fieldType = FieldType.UINT64, order = 3, required = false)
    private Long fromUid;

    @Protobuf(fieldType = FieldType.UINT64, order = 4, required = false)
    private Long toUid;

    @Protobuf(fieldType = FieldType.UINT64, order = 5, required = false)
    private Long rid;

    @Protobuf(fieldType = FieldType.UINT64, order = 6, required = false)
    private Long aid;

    @Protobuf(fieldType = FieldType.UINT64, order = 7, required = false)
    private Long toRid;

    @Protobuf(fieldType = FieldType.UINT64, order = 8, required = false)
    private Long frRid;

    @Protobuf(fieldType = FieldType.UINT32, order = 9, required = false)
    private Integer sub;

    @Protobuf(fieldType = FieldType.STRING, order = 10, required = false)
    private String name;

    @Protobuf(fieldType = FieldType.STRING, order = 11, required = false)
    private String content;

    @Protobuf(fieldType = FieldType.UINT64, order = 12, required = false)
    private Long time;

    @Protobuf(fieldType = FieldType.UINT32, order = 13, required = false)
    private Integer index;

    @Protobuf(fieldType = FieldType.UINT64, order = 14, required = false)
    private Long gaid;

    //事务内聊天
    public static Message getAffairChatMsg(Integer sub, Long aid, Long fromUid, Long frRid, Long toUid, Long toRid, String name, String content) {
        Message message = new Message(MessageType.AFFAIR_CHAT, sub, fromUid, frRid, toUid, toRid, name, content);
        message.aid = aid;
        return message;
    }

    //好友聊天
    public static Message getFriendChatMsg(Integer sub, Long fromUid, Long toUid, String name, String content) {
        Message message = new Message(MessageType.FRIEND_CHAT, sub, fromUid, null, toUid, null, name, content);
        return message;
    }

    //公告聊天
    public static Message getNoticeChatMsg(Integer sub, Long aid, Long rid, Long gaid, Long fromUid, Long frRid, String name, String content) {
        Message message = new Message(MessageType.AFFAIR_CHAT, sub, fromUid, frRid, null, null, name, content);
        message.aid = aid;
        message.rid = rid;
        message.gaid = gaid;
        return message;
    }

    public Message() {
    }

    public Message(Integer type, Integer sub, Long fromUid, Long frRid, Long toUid, Long toRid, String name, String content) {
        this.type = type;
        this.toUid = toUid;
        this.fromUid = fromUid;
        this.toRid = toRid;
        this.frRid = frRid;
        this.sub = sub;
        this.name = name;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getFromUid() {
        return fromUid;
    }

    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    public Long getToUid() {
        return toUid;
    }

    public void setToUid(Long toUid) {
        this.toUid = toUid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Long getToRid() {
        return toRid;
    }

    public void setToRid(Long toRid) {
        this.toRid = toRid;
    }

    public Long getFrRid() {
        return frRid;
    }

    public void setFrRid(Long frRid) {
        this.frRid = frRid;
    }

    public Integer getSub() {
        return sub;
    }

    public void setSub(Integer sub) {
        this.sub = sub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getGaid() {
        return gaid;
    }

    public void setGaid(Long gaid) {
        this.gaid = gaid;
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
