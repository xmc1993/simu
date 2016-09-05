package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.utils.FileUtil;
import cn.superid.utils.MapUtil;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.AliSmsDao;
import cn.superid.webapp.utils.DirectEmailDao;
import cn.superid.webapp.utils.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

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

    private final String lastEmailTime = "last_email_time";
    private String forgetPasswordEmailTmpl ;
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
    public boolean belong(Long userId, Long roleId) {
        return true;
    }


    private boolean canSendVerifyCodeAgain() {
        Date date =(Date)auth.getSessionAttr("last_token_time");
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
    public boolean  getVerifyCode(String token,String template) {
        if(canSendVerifyCodeAgain()){
            final String code = NumberUtils.randomNumberString(4);
            auth.setSessionAttr("code",code);
            auth.setSessionAttr("last_token_time",new Date());
            if(StringUtil.isEmail(token)){
                String emailText = this.verifyCodeEmailTmpl.replace("${verifyCode}", code);
                return DirectEmailDao.sendEmail("SuperId邮箱验证",emailText,token);
            }else{
                return AliSmsDao.sendSMSMessageToMobileWithTemplate(token,template, MapUtil.hashmap("code",code));
            }
        }
        return false;
    }

    @Override
    public boolean checkVerifyCode(String code) {
        Date date =(Date)auth.getSessionAttr("last_token_time");
        String cachedCode =(String) auth.getSessionAttr("code");

        if(date!=null&&code!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 15);
            if (calendar.getTime().before(new Date())) {//超过15分钟
                return false;
            }
            if(code.equals(cachedCode)){
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity userEntity) {
        userEntity.save();
        AllianceCreateForm allianceCreateForm = new AllianceCreateForm();
        allianceCreateForm.setName(userEntity.getUsername());
        allianceCreateForm.setIsPersonal(IntBoolean.TRUE);
        allianceCreateForm.setUserEntity(userEntity);
        AllianceEntity allianceEntity=allianceService.createAlliance(allianceCreateForm);
        if(allianceEntity==null) return null;
        return userEntity;
    }

    @Override
    public UserEntity findByToken(String token) {
        return UserEntity.dao.or(Expr.eq("email",token),Expr.eq("mobile",token),Expr.eq("superid",token)).selectOne();
    }

    @Override
    public boolean validUsername(String username) {
        return !UserEntity.dao.eq("username",username).exists();
    }

    @Override
    public boolean validToken(String token) {
        if(StringUtil.isEmail(token)){
            return !UserEntity.dao.eq("email",token).exists();
        }else if(StringUtil.isMobile(token)){
            return !UserEntity.dao.eq("mobile",token).exists();
        }
        return false;
    }

    @Override
    public UserEntity getCurrentUser() {
         return UserEntity.dao.findById(currentUserId());
    }

    @Override
    public long currentUserId() {
        return auth.currentUserId();
    }
}
