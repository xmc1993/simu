package cn.superid.webapp.service.forms;

/**
 * Created by njuTms on 16/11/16.
 */
public class ModifyAffairInfoForm {
    private String name;
    private Integer publicType;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPublicType() {
        return publicType;
    }

    public void setPublicType(Integer publicType) {
        this.publicType = publicType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
