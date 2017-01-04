package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.SearchUserVO;
import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.service.vo.AllianceRolesVO;
import cn.superid.webapp.service.vo.UserNameAndRoleNameVO;

import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public interface IRoleService {

    RoleEntity createRole(String title, long allianceId, long userId, long belongAffairId, String permissions, int type);

    String getNameByRoleId(Long roleId);

    UserNameAndRoleNameVO getUserNameAndRoleName(Long roleId);

    List<SearchUserVO> searchUser(long allianceId, String input, boolean containName, boolean containTag);

    boolean addAllianceUser(List<AddAllianceUserForm> forms, long allianceId,long roleId,long inviteUserId);

    /**
     * 获取盟中失效的角色列表
     *
     * @param allianceId
     * @return
     */
    List<RoleEntity> getInvalidRoles(long allianceId);

    /**
     * 用于用户在登录时获取自己所拥有的所有盟下的所有角色
     * 要去掉个人盟及其角色
     * @return
     */
    public List<AllianceRolesVO> getUserAllianceRoles();

}
