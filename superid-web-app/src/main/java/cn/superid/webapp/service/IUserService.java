package cn.superid.webapp.service;

import cn.superid.webapp.model.base.UserBaseInfo;
import cn.superid.webapp.model.UserEntity;

/**
 * Created by zp on 2016/7/26.
 */
public interface IUserService {
    public boolean belong(Long userId,Long roleId);

    public  boolean getVerifyCode(String token,String template);

    public boolean checkVerifyCode(String code);

    public UserEntity createUser(UserEntity userEntity);

    public UserEntity findByToken(String token);

    public boolean validUsername(String username);

    public boolean validToken(String token);

    public UserEntity getCurrentUser();

    public long currentUserId();

    public boolean editBaseInfo(UserBaseInfo userBaseInfo);

}
