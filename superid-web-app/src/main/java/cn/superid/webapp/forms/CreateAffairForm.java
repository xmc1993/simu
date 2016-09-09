package cn.superid.webapp.forms;

/**
 * Created by xiaofengxu on 16/8/31.
 */
public class CreateAffairForm {
    private String name;
    private long affairId;
    private Integer index;
    private long operationRoleId;
    private int publicType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAffairId() {
        return affairId;
    }

    public void setAffairId(long affairId) {
        this.affairId = affairId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public long getOperationRoleId() {
        return operationRoleId;
    }

    public void setOperationRoleId(long operationRoleId) {
        this.operationRoleId = operationRoleId;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }
}
