package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.utils.PingYinUtil;
import cn.superid.webapp.controller.VO.SearchUserVO;
import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import cn.superid.webapp.enums.RoleType;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.enums.type.DefaultRole;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.service.IAllianceUserService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.vo.UserNameAndRoleNameVO;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaofengxu on 16/9/9.
 */
@Service
public class RoleService implements IRoleService {

    @Autowired
    private IAllianceUserService allianceUserService;
    @Override
    public RoleEntity createRole(String title, long allianceId, long userId, long belongAffairId, String permissions, int type) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setTitle(title);
        roleEntity.setUserId(userId);
        roleEntity.setAllianceId(allianceId);
        roleEntity.setBelongAffairId(belongAffairId);
        roleEntity.setPermissions(permissions);
        roleEntity.setType(type);
        roleEntity.setState(ValidState.Valid);
        roleEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        roleEntity.setTitleAbbr(PingYinUtil.getFirstSpell(title));
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
        if(containName == false & containTag == false){
            return  result;
        }
        StringBuilder sql = new StringBuilder("select a.*,b.id as memberId from (select id,username as name,avatar,superid as superId from user where ");
        ParameterBindings pb = new ParameterBindings();
        if (containName == true) {
            sql.append(" username like ? or superid like ? ");
            pb.addIndexBinding("%" + input + "%");
            pb.addIndexBinding("%" + input + "%");
        }
        if (containTag == true) {
            //TODO:等标签系统好再处理
        }
        sql.append(" order by id desc limit 20 ) a left join (select id , user_id from alliance_user where alliance_id = ? and state = 0 ) b on a.id = b.user_id ");
        pb.addIndexBinding(allianceId);
        result = UserEntity.getSession().findList(SearchUserVO.class,sql.toString(),pb);


        return result;
    }

    @Override
    public boolean addAllianceUser(List<AddAllianceUserForm> forms, long allianceId,long roleId) {
        allianceUserService.inviteToEnterAlliance(forms,allianceId,roleId);
        return true;
    }

    @Override
    public List<RoleEntity> getInvalidRoles(long allianceId) {
//        String sql = "select * from role where alliance_id = ? and state = ?";
//        List<RoleEntity> invalidRoles = RoleEntity.dao.findList(sql, allianceId, ValidState.Invalid);
        return RoleEntity.dao.partitionId(allianceId).state(ValidState.Invalid).selectList();
    }
}
