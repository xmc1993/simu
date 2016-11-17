package cn.superid.webapp.service.vo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/11/14.
 */
public class AffairTreeVO implements Comparable<AffairTreeVO> {
    private long id;
    private String superid;
    private String name ="";
    private long allianceId;
    private int publicType;
    private String shortName;
    private long roleId;
    private boolean isStuck;//是否置顶
    private String path;
    private long parentId;
    private boolean isIndex;//是否主页事务

    private List<AffairTreeVO> children = new ArrayList<>();



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(long allianceId) {
        this.allianceId = allianceId;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean getIsStuck() {
        return isStuck;
    }

    public void setIsStuck(boolean isStuck) {
        this.isStuck = isStuck;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public List<AffairTreeVO> getChildren() {
        return children;
    }

    public void setChildren(List<AffairTreeVO> children) {
        this.children = children;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(boolean isIndex) {
        this.isIndex = isIndex;
    }

    @Override
    public int compareTo(AffairTreeVO a) {
        if(this.getPath().length()>a.getPath().length()){
            return 1;
        }else if(this.getPath().length()<a.getPath().length()){
            return -1;
        }
        return 0;
    }
}
