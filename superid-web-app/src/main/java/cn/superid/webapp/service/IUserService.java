package cn.superid.webapp.service;

import cn.superid.webapp.forms.UserRegisterForm;
import cn.superid.webapp.model.UserEntity;

/**
 * Created by zp on 2016/7/26.
 */
public interface IUserService {
    public boolean belong(Long userId,Long roleId);

    public UserEntity login(String token,String password);

    public boolean register(UserRegisterForm userRegisterForm);

    public  boolean getVerifyCode(String token,String template);

    public boolean checkVerifyCode(String code);

    public UserEntity createUser(UserEntity userEntity);

    public UserEntity findByToken(String token);

}
