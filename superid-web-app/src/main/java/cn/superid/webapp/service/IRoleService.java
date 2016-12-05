package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.SearchUserVO;
import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.vo.UserNameAndRoleNameVO;

import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public interface IRoleService {

    RoleEntity createRole(String title,long allianceId,long userId,long belongAffairId,String permissions,int type);

    public String getNameByRoleId(Long roleId);

    public UserNameAndRoleNameVO getUserNameAndRoleName(Long roleId);

    public List<SearchUserVO> searchUser(long allianceId , String input);

    public boolean addAllianceUser(List<AddAllianceUserForm> forms , long allianceId);



}
