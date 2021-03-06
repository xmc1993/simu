package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.UserAllianceRolesVO;
import cn.superid.webapp.controller.forms.ChangePublicTypeForm;
import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.UserPrivateInfoEntity;
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

     UserEntity createUser(String token , String password , String username);

     UserEntity findByToken(String token);

     boolean validUsername(String username);

     boolean validToken(String token);

     boolean validTokenForReset(String token);

     UserEntity getCurrentUser();

     long currentUserId();

     boolean editBaseInfo(EditUserDetailForm userBaseInfo);

     boolean changeToken(String token);

     boolean changePwd(String oldPwd,String newPwd);

     boolean forgetPwd(String newPwd, String token);

     boolean editDetailInfo(EditUserDetailForm editUserDetailForm);

     boolean changePublicType(ChangePublicTypeForm form);

     ResultUserInfo getUserInfo(long userId);

     String getAvatarByUserId(long userId);

     /**
      * 用户修改superid前检测是否已存在该id
      * @param superId
      * @return
      */
     public boolean validSuperId(String superId);

     public boolean modifySuperId(String superId);


     public UserPrivateInfoEntity getPublicProperty(long userId);
}
