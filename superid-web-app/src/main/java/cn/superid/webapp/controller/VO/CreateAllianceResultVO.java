package cn.superid.webapp.controller.VO;

import cn.superid.webapp.service.vo.AffairTreeVO;

import java.util.List;
import java.util.Map;

/**
 * Created by njuTms on 16/11/17.
 */
//用于创建盟时给前端返回的数据格式
public class CreateAllianceResultVO {
    private long id;
    private String name;
    private int verified;
    private String code;
    private long ownRoleId;
    private AffairTreeVO affairTree;
    private Map<Long,List<Object>> affairMember;

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

    public AffairTreeVO getAffairTree() {
        return affairTree;
    }

    public void setAffairTree(AffairTreeVO affairTree) {
        this.affairTree = affairTree;
    }

    public Map<Long, List<Object>> getAffairMember() {
        return affairMember;
    }

    public void setAffairMember(Map<Long, List<Object>> affairMember) {
        this.affairMember = affairMember;
    }

    public long getOwnRoleId() {
        return ownRoleId;
    }

    public void setOwnRoleId(long ownRoleId) {
        this.ownRoleId = ownRoleId;
    }
}
