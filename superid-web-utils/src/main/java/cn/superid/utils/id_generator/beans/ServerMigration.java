package cn.superid.utils.id_generator.beans;

/**
 * 数据库服务器迁移记录
 * Created by zoowii on 2014/9/2.
 */
public class ServerMigration {
    private String groupName;
    private long from;
    private long to;

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
