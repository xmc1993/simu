package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.UserAllianceRolesVO;
import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.service.vo.AllianceRolesVO;

import java.util.List;
import java.util.Map;

/**
 * Created by zp on 2016/7/26.
 */
public interface IUserService {
     boolean belong(long roleId);

     boolean getVerifyCode(String token,String template);

     boolean checkVerifyCode(String code,String token);

     UserEntity createUser(UserEntity userEntity);

     UserEntity findByToken(String token);

     boolean validUsername(String username);

     boolean validToken(String token);

     boolean validTokenForReset(String token);

     UserEntity getCurrentUser();

     long currentUserId();

     boolean editBaseInfo(EditUserBaseInfo userBaseInfo);

     boolean changeToken(String token);

     boolean changePwd(String oldPwd,String newPwd);

     boolean resetPwd(String newPwd,String token);

     boolean editDetailInfo(EditUserDetailForm editUserDetailForm);

     boolean changePublicType(int publicType);

     ResultUserInfo getUserInfo(long userId);

     public Map<Long,List<Object>> getAffairMember();

     public Map<Long,List<Object>> getAffairMemberByAllianceId(long allianceId);

     /**
      * 用于用户在登录时获取自己所拥有的所有盟下的所有角色
      * 要去掉个人盟及其角色
      * @return
      */
     //public List<UserAllianceRolesVO> getUserAllianceRoles();
     public List<AllianceRolesVO> getUserAllianceRoles();

     /**
      * 用户修改superid前检测是否已存在该id
      * @param superId
      * @return
      */
     public boolean validSuperId(String superId);

     public boolean modifySuperId(String superId);

     public void rollbackTest();
}
