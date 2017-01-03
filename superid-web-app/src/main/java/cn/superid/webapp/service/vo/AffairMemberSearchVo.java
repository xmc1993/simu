package cn.superid.webapp.service.vo;

import lombok.Data;

/**
 * Created by jessiechen on 03/01/17.
 */
@Data
public class AffairMemberSearchVo {
    private String username;
    private String superid;
    private int gender;
    private String roleTitle;
    //主事务
    private String belongAffair;

}
