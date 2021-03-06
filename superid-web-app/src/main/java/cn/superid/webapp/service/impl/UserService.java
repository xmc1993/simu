package cn.superid.webapp.service.impl;

import cn.superid.utils.*;
import cn.superid.webapp.controller.forms.ChangePublicTypeForm;
import cn.superid.webapp.enums.type.PublicType;
import cn.superid.webapp.forms.*;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by zp on 2016/8/1.
 */
@Service
public class UserService implements IUserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private IAuth auth;


    @Autowired
    private IAllianceService allianceService;

    @Autowired
    private IAffairMemberService affairMemberService;


    private final String lastEmailTime = "last_email_time";
    private String forgetPasswordEmailTmpl;
    private String verifyCodeEmailTmpl;


    public UserService() {
        try {
            InputStream forgetPasswordEmailTmplInStream = this.getClass().getClassLoader().getResourceAsStream("forget-password-email-tmpl.html");
            if (forgetPasswordEmailTmplInStream != null) {
                String content = FileUtil.tryParseStreamToString(forgetPasswordEmailTmplInStream);
                this.forgetPasswordEmailTmpl = content;
            }
        } catch (Exception e) {
            LOG.error("init user service error", e);
        }
        try {
            InputStream verifyCodeEmailTmplInStream = this.getClass().getClassLoader().getResourceAsStream("verify-code-email-tmpl.html");
            if (verifyCodeEmailTmplInStream != null) {
                String content = FileUtil.tryParseStreamToString(verifyCodeEmailTmplInStream);
                this.verifyCodeEmailTmpl = content;
            }
        } catch (Exception e) {
            LOG.error("init user service error", e);
        }
    }

    @Override
    public boolean belong(long roleId) {
        return (Long) RoleCache.dao.findFieldByKey(roleId, "userId", Long.class) == currentUserId();
    }


    private boolean canSendVerifyCodeAgain() {
        Date date = (Date) auth.getSessionAttr("last_token_time");
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 1);
            if (calendar.getTime().after(new Date())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean getVerifyCode(String token, String template) {
        if (canSendVerifyCodeAgain()) {
            final String code = NumberUtils.randomNumberString(6);
            auth.setSessionAttr("code", code);
            auth.setSessionAttr("last_token_time", new Date());
            auth.setSessionAttr("token", token);
            if (StringUtil.isEmail(token)) {
                String emailText = this.verifyCodeEmailTmpl.replace("${verifyCode}", code);
                return DirectEmailDao.sendEmail("SuperId邮箱验证", emailText, token);
            } else {
                //此处因为要分是注册还是其他获取验证码方式,所以要先看看数据库里有咩有该账号
                //因为注册是要传区号过来,并且数据库没有该条数据,所以token不需要处理
                //其他获取方式不需要区号,所以要自己去数据库取出区号来加上前端传过来的token然后去发短信
                UserEntity userEntity = UserEntity.dao.eq("mobile", token).selectOne("countryCode");
                String countryCode;
                if (userEntity != null) {
                    countryCode = userEntity.getCountryCode();
                    token = countryCode + " " + token;
                }

                if (MobileUtil.isChinaMobile(token)) {
                    return AliSmsDao.sendSMSMessageToMobileWithTemplate(MobileUtil.getMobile(token), template, MapUtil.hashmap("code", code));
                } else {
                    token = MobileUtil.getCountryCode(token) + MobileUtil.getMobile(token);
                    return YunPianSmsDao.sendSMSMessageToForeignMobile(token.trim(), code, template);
                }

            }
        }
        return false;
    }

    @Override
    public boolean checkVerifyCode(String code, String token) {
        Date date = (Date) auth.getSessionAttr("last_token_time");
        String cachedCode = (String) auth.getSessionAttr("code");
        if (token != null) {
            String cachedToken = (String) auth.getSessionAttr("token");
            if (!cachedToken.equals(token)) {
                return false;
            }
        }

        if (date != null && code != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 15);
            if (calendar.getTime().before(new Date())) {//超过15分钟
                return false;
            }
            if (StringUtils.equalsIgnoreCase(code, cachedCode)) {
                return true;
            }
        }
        return false;
    }

    @Override
//    @Transactional  TODO 支持分布式事务再开启
    public UserEntity createUser(String token , String password , String username) {
        UserEntity userEntity=new UserEntity();
        if(StringUtil.isEmail(token)){
            userEntity.setEmail(token);
        }else if(StringUtil.isMobile(token)){
            userEntity.setCountryCode(MobileUtil.getCountryCode(token));
            userEntity.setAddress(CountryCode.getAddress(userEntity.getCountryCode()));
            userEntity.setMobile(MobileUtil.getMobile(token));
        }
        userEntity.setPassword(PasswordEncryptor.encode(password));
        userEntity.setRealname(username);
        userEntity.setUsername(username);
        userEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        userEntity.save();

        String abbr = PingYinUtil.getFirstSpell(userEntity.getRealname());
        AllianceCreateForm allianceCreateForm = new AllianceCreateForm();
        allianceCreateForm.setName(userEntity.getRealname() + "的事务");
        allianceCreateForm.setUserId(userEntity.getId());
        allianceCreateForm.setIsPersonal(true);
        allianceCreateForm.setRoleTitle(userEntity.getRealname());
        allianceCreateForm.setCode(abbr);

        AllianceEntity allianceEntity = allianceService.createAlliance(allianceCreateForm);

        userEntity.setPersonalRoleId(allianceEntity.getOwnerRoleId());
        userEntity.setPersonalAllianceId(allianceEntity.getId());
        userEntity.setPersonalAffairId(allianceEntity.getRootAffairId());
        userEntity.setHomepageAffairId(allianceEntity.getRootAffairId());
        userEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        userEntity.setNameAbbr(abbr);
        userEntity.setSuperid(allianceEntity.getSuperid());
        userEntity.update();

        UserPrivateInfoEntity userPrivateInfoEntity = new UserPrivateInfoEntity();
        userPrivateInfoEntity.setUserId(userEntity.getId());
        userPrivateInfoEntity.setBirthday(PublicType.PRIVATE);
        userPrivateInfoEntity.setEmail(PublicType.PRIVATE);
        userPrivateInfoEntity.setIdCard(PublicType.PRIVATE);
        userPrivateInfoEntity.setMobile(PublicType.PRIVATE);
        userPrivateInfoEntity.setPersonalTags(PublicType.PRIVATE);
        userPrivateInfoEntity.setRealname(PublicType.PRIVATE);
        userPrivateInfoEntity.save();

        return userEntity;
    }

    @Override
    public UserEntity findByToken(String token) {

        if (StringUtil.isEmail(token)) {
            return UserEntity.dao.eq("email", token).selectOne();
        } else if (StringUtil.isMobile(token)) {
            return UserEntity.dao.eq("mobile", token).selectOne();
        } else {
            return null;
        }
    }

    @Override
    public boolean validUsername(String username) {
        return !UserEntity.dao.eq("username", username).exists();
    }

    /**
     * 判断手机号码是否注册
     *
     * @param token
     * @return
     */
    @Override
    public boolean validToken(String token) {
        if (StringUtil.isEmail(token)) {
            return UserEntity.dao.eq("email", token).exists();
        } else {
            String[] strs = token.split("\\s+");
            if (StringUtil.isMobile(token)) {
                if (strs.length == 1) {
                    return UserEntity.dao.eq("mobile", token).exists();
                } else {
                    return UserEntity.dao.eq("mobile", strs[1]).exists();
                }

            }
            return false;
        }

    }

    @Override
    public boolean validTokenForReset(String token) {
        if (StringUtil.isEmail(token)) {
            return !UserEntity.dao.eq("email", token).exists();
        } else if (StringUtil.isMobile(token)) {
            return !UserEntity.dao.eq("mobile", token).exists();
        }
        return true;
    }

    @Override
    public UserEntity getCurrentUser() {
        return UserEntity.dao.findById(currentUserId());
    }

    @Override
    public long currentUserId() {

        return auth.currentUserId();
    }

    @Override
    public boolean editBaseInfo(EditUserDetailForm userDetailForm) {
        UserBaseInfo update = UserBaseInfo.dao.findById(currentUserId());//有缓存的实体可以先获取再更新
        if (update == null) {
            return false;
        } else {
            update.copyPropertiesFromAndSkipNull(userDetailForm);
            UserEntity userEntity = UserEntity.dao.findById(currentUserId());
            if(userEntity == null){
                return  false;
            }
            userEntity.copyPropertiesFromAndSkipNull(userDetailForm);
            if(userDetailForm.getBirthday() != null){
                userEntity.setBirthday(new Timestamp(userDetailForm.getBirthday()));
            }
            userEntity.setNameAbbr(PingYinUtil.getFirstSpell(update.getUsername()));
            userEntity.update();
        }
        return true;
    }

    @Override
    public boolean changeToken(String token) {
        long userId = currentUserId();
        int result ;
        if(StringUtil.isEmail(token)){
            result =  UserEntity.dao.id(userId).set("email",token);
        }else {
            result = UserEntity.dao.id(userId).set("mobile",MobileUtil.getMobile(token));
        }
        return result>0;
    }

    @Override
    public boolean changePwd(String oldPwd, String newPwd) {
        UserEntity userEntity = getCurrentUser();
        if (!PasswordEncryptor.matches(oldPwd, userEntity.getPassword())) {
            return false;
        }
        newPwd = PasswordEncryptor.encode(newPwd);
        int result =  UserEntity.dao.id(currentUserId()).set("password",newPwd);
        return result>0;

    }

    @Override
    public boolean forgetPwd(String newPwd, String token) {
        UserEntity userEntity = findByToken(token);
        if (userEntity == null) {
            return false;
        }
        newPwd = PasswordEncryptor.encode(newPwd);
        int result =  UserEntity.dao.id(userEntity.getId()).set("password",newPwd);
        return result>0;

    }

    @Override
    public boolean editDetailInfo(EditUserDetailForm editUserDetailForm) {
        int result = UserEntity.dao.id(currentUserId()).setByObject(editUserDetailForm);
        return result > 0;
    }

    @Override
    public boolean changePublicType(ChangePublicTypeForm form) {
        return UserPrivateInfoEntity.dao.partitionId(currentUserId()).setByObject(form)>0;
    }

    @Override
    public ResultUserInfo getUserInfo(long userId) {

        return null;
    }



    @Override
    public boolean validSuperId(String superId) {
        return UserEntity.dao.eq("superid", superId).exists();
    }

    @Override
    public boolean modifySuperId(String superId) {
        //检测修改的superid是否符合长度等要求,是不是要放在前端
        return UserEntity.dao.id(currentUserId()).set("superid", superId) > 0;
    }


    @Override
    public UserPrivateInfoEntity getPublicProperty(long userId) {
        List<String> result = new ArrayList<>();
        return UserPrivateInfoEntity.dao.partitionId(userId).selectOne();
    }

    @Override
    public String getAvatarByUserId(long userId) {
        UserBaseInfo userBaseInfo = UserBaseInfo.dao.findById(userId);
        return userBaseInfo==null?null:userBaseInfo.getAvatar();
    }
}
