namespace java cn.superid.webapp.notice.thrift

struct S2c{
	1: optional i32 type; //消息类型
	2: optional i64 frUid;//发送者userId
	3: optional i64 toUid;//接收者userId
	4: optional i64 rid;//相关id,比如讨论组Id,合同Id,公告Id等
	5: optional i64 allId;//盟id
	6: optional i64 aid;//事务Id
	7: optional i64 toAid;//要移动到的父事务的id
	8: optional i64 tid;//任务Id
	9: optional i64 toRid;//接收者roleId
	10: optional i64 frRid;//发送者roleId
	11: optional i32 sub;//消息子类型
	12: optional string content;//发送内容
	13: optional i64 time;//发送时间,不需要填
	14: optional i32 index;//接收消息的时候会有index做验证
}




service NoticeService{

    bool sendNotice(1: S2c msg);

}