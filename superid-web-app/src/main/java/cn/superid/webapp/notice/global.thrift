namespace java cn.superid.webapp.notice.thrift

struct Msg{
	1: optional string _id;//发送不需要填
	2: optional i32 type; //消息类型
	3: optional i64 frUid;//发送者userId
	4: optional i64 toUid;//接收者userId
	5: optional i64 rid;//相关id,比如讨论组Id,合同Id,公告Id等
	6: optional i64 aid;//事务Id
	7: optional i64 toRid;//接收者roleId
	8: optional i64 frRid;//发送者roleId
	9: optional i32 sub;//消息子类型
	10: optional string name;//发送者名称
	11: optional string content;//发送内容
	12: optional i64 time;//发送时间,不需要填
	13: optional i32 index;//接收消息的时候会有index做验证
	14: optional i64 gaId;// guestAllianceId 客方盟id 公告聊天为客方盟id，
}

struct C2c {
	1: required i32 type;
	2: optional Msg chat;//附带的聊天相关消息
	3: optional string params;//常用于请求中携带参数,可以是json
	4: list<Msg> msgList;//返回消息体
	5: optional string requestId;
	6: optional string data;//返回的普通消息体，可以是string或者json.stringfy之后的
}



service NoticeService{
    bool sendNotice(1: C2c c2c);

}