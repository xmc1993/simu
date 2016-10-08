package cn.superid.webapp.service;

import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.UserEntity;

/**
 * Created by zp on 2016/7/26.
 */
public interface IUserService {
     boolean belong(long roleId);

     boolean getVerifyCode(String token,String template);

     boolean checkVerifyCode(String code);

     UserEntity createUser(UserEntity userEntity);

     UserEntity findByToken(String token);

     boolean validUsername(String username);

     boolean validToken(String token);

     UserEntity getCurrentUser();

     long currentUserId();

     boolean editBaseInfo(EditUserBaseInfo userBaseInfo);

     boolean changeToken(String token);

     boolean changePwd(String oldPwd,String newPwd);

     boolean editDetailInfo(EditUserDetailForm editUserDetailForm);

     boolean changePublicType(int publicType);

     ResultUserInfo getUserInfo(long userId);
}
