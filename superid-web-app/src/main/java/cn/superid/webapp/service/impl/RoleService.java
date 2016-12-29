package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.SearchUserVO;
import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.enums.RoleType;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.enums.type.DefaultRole;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.vo.UserNameAndRoleNameVO;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaofengxu on 16/9/9.
 */
@Service
public class RoleService implements IRoleService {

    @Override
    public RoleEntity createRole(String title, long allianceId, long userId, long belongAffairId, String permissions, int type) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setTitle(title);
        roleEntity.setUserId(userId);
        roleEntity.setAllianceId(allianceId);
        roleEntity.setBelongAffairId(belongAffairId);
        roleEntity.setPermissions(permissions);
        roleEntity.setType(type);
        roleEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        roleEntity.save();
        return roleEntity;
    }

    @Override
    public String getNameByRoleId(Long roleId) {
        RoleCache role = RoleCache.dao.findById(roleId);
        UserBaseInfo user = UserBaseInfo.dao.findById(role.getUserId());
        if (role == null || user == null || role.getTitle() == null || user.getUsername() == null) {
            return null;
        }

        return role.getTitle() + ": " + user.getUsername();
    }

    @Override
    public UserNameAndRoleNameVO getUserNameAndRoleName(Long roleId) {
        RoleCache role = RoleCache.dao.findById(roleId);
        UserBaseInfo user = UserBaseInfo.dao.findById(role.getUserId());
        if (role == null || user == null || role.getTitle() == null || user.getUsername() == null) {
            return null;
        }
        UserNameAndRoleNameVO result = new UserNameAndRoleNameVO();
        result.setRoleName(role.getTitle());
        result.setUserName(user.getUsername());
        result.setAvatar(user.getAvatar());
        return result;
    }


    @Override
    public List<SearchUserVO> searchUser(long allianceId, String input, boolean containName, boolean containTag) {
        List<SearchUserVO> result = new ArrayList<>();
        if (input == null | input.equals("")) {
            return result;
        }
        StringBuilder sql = new StringBuilder("select  * from  user where id not in " +
                "( select distinct user_id from role where alliance_id = ? ) and (  ");
        ParameterBindings pb = new ParameterBindings();
        pb.addIndexBinding(allianceId);
        if (containName == true) {
            sql.append(" username like ? or superid like ? ");
            pb.addIndexBinding("%" + input + "%");
            pb.addIndexBinding("%" + input + "%");
        }
        if (containTag == true) {
            //TODO:等标签系统好再处理
        }
        sql.append(" ) order by id desc limit 20 ");
        List<UserEntity> userEntityList = UserEntity.dao.findList(sql.toString(), pb);


        if (userEntityList != null) {
            for (UserEntity u : userEntityList) {
                SearchUserVO user = new SearchUserVO();
                user.setId(u.getId());
                user.setAvatar(u.getAvatar());
                user.setName(u.getUsername());
                user.setSuperId(u.getSuperid());

                result.add(user);
            }
        }

        return result;
    }

    @Override
    public boolean addAllianceUser(List<AddAllianceUserForm> forms, long allianceId) {

        for (AddAllianceUserForm form : forms) {
            RoleEntity role = new RoleEntity();
            role.setUserId(form.getUserId());
            role.setTitle(form.getRoleName());
            role.setBelongAffairId(form.getMainAffairId());
            role.setAllianceId(allianceId);
            role.setPermissions(form.getPermissions());
            role.setType(DefaultRole.IsDefault);
            role.setState(ValidState.Valid);
            role.save();
        }


        return true;
    }

    @Override
    public List<RoleEntity> getInvalidRoles(long allianceId) {
//        String sql = "select * from role where alliance_id = ? and state = ?";
//        List<RoleEntity> invalidRoles = RoleEntity.dao.findList(sql, allianceId, ValidState.Invalid);
        return RoleEntity.dao.partitionId(allianceId).state(ValidState.Invalid).selectList();
    }
}
