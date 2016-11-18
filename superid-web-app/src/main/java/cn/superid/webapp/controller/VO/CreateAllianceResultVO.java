package cn.superid.webapp.controller.VO;

/**
 * Created by njuTms on 16/11/17.
 */
//用于创建盟时给前端返回的数据格式
public class CreateAllianceResultVO {
    private long id;
    private String name;
    private int verified;
    private String code;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
