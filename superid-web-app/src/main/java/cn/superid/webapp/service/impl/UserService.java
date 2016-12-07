package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.utils.FileUtil;
import cn.superid.utils.MapUtil;
import cn.superid.utils.MobileUtil;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.controller.VO.SimpleRoleVO;
import cn.superid.webapp.enums.type.PublicType;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.UserPrivateInfoEntity;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.vo.AffairMemberVO;
import cn.superid.webapp.utils.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
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
    public boolean belong(long roleId) {
       return (Long)RoleCache.dao.findFieldByKey(roleId,"userId",Long.class)==currentUserId();
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
            final String code = NumberUtils.randomNumberString(6);
            auth.setSessionAttr("code",code);
            auth.setSessionAttr("last_token_time",new Date());
            auth.setSessionAttr("token",token);
            if(StringUtil.isEmail(token)){
                String emailText = this.verifyCodeEmailTmpl.replace("${verifyCode}", code);
                return DirectEmailDao.sendEmail("SuperId邮箱验证",emailText,token);
            }else{
                //此处因为要分是注册还是其他获取验证码方式,所以要先看看数据库里有咩有该账号
                //因为注册是要传区号过来,并且数据库没有该条数据,所以token不需要处理
                //其他获取方式不需要区号,所以要自己去数据库取出区号来加上前端传过来的token然后去发短信
                UserEntity userEntity = UserEntity.dao.eq("mobile",token).selectOne("countryCode");
                String countryCode;
                if(userEntity != null){
                    countryCode = userEntity.getCountryCode();
                    token = countryCode + " " + token;
                }

                if(MobileUtil.isChinaMobile(token)){
                    return AliSmsDao.sendSMSMessageToMobileWithTemplate(MobileUtil.getMobile(token),template, MapUtil.hashmap("code",code));
                }else {
                    return YunPianSmsDao.sendSMSMessageToForeignMobile(token,code,template);
                }

            }
        }
        return false;
    }

    @Override
    public boolean checkVerifyCode(String code,String token) {
        Date date =(Date)auth.getSessionAttr("last_token_time");
        String cachedCode =(String) auth.getSessionAttr("code");
        if(token!=null){
            String cachedToken = (String) auth.getSessionAttr("token");
            if(!cachedToken.equals(token)){
                return false;
            }
        }

        if(date!=null&&code!=null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 15);
            if (calendar.getTime().before(new Date())) {//超过15分钟
                return false;
            }
            if(StringUtils.equalsIgnoreCase(code, cachedCode)){
                return true;
            }
        }
        return false;
    }

    @Override
//    @Transactional  TODO 支持分布式事务再开启
    public UserEntity createUser(UserEntity userEntity) {
        userEntity.save();
        AllianceCreateForm allianceCreateForm = new AllianceCreateForm();
        allianceCreateForm.setName(userEntity.getUsername());
        allianceCreateForm.setUserId(userEntity.getId());
        allianceCreateForm.setIsPersonal(true);
        allianceCreateForm.setUserEntity(userEntity);
        AllianceEntity allianceEntity=allianceService.createAlliance(allianceCreateForm);
        UserEntity user = UserEntity.dao.findById(allianceCreateForm.getUserEntity().getId());
        user.setSuperid(generateSuperId(user.getId()));
        user.setPersonalRoleId(userEntity.getPersonalRoleId());
        user.update();
        if(allianceEntity==null) return null;
        return userEntity;
    }

    @Override
    public UserEntity findByToken(String token) {
        if(StringUtil.isEmail(token)){
            return UserEntity.dao.eq("email",token).selectOne();
        }else if(StringUtil.isMobile(token)){
           return UserEntity.dao.eq("mobile",token).selectOne();
        }else {
            return null;
        }
    }

    @Override
    public boolean validUsername(String username) {
        return !UserEntity.dao.eq("username",username).exists();
    }

    /**
     * 判断手机号码是否注册
     * @param token
     * @return
     */
    @Override
    public boolean validToken(String token) {
        if(StringUtil.isEmail(token)){
            return UserEntity.dao.eq("email",token).exists();
        }else{
            String[] strs = token.split("\\s+");
            if(StringUtil.isMobile(token)){
                if(strs.length==1){
                    return UserEntity.dao.eq("mobile",token).exists();
                }else {
                    return UserEntity.dao.eq("mobile",strs[1]).exists();
                }

            }
            return false;
        }

    }

    @Override
    public boolean validTokenForReset(String token) {
        if(StringUtil.isEmail(token)){
            return !UserEntity.dao.eq("email",token).exists();
        }else if(StringUtil.isMobile(token)){
            return !UserEntity.dao.eq("mobile",token).exists();
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
    public boolean editBaseInfo(@RequestParam EditUserBaseInfo userBaseInfo) {
        UserBaseInfo update = UserBaseInfo.dao.findById(currentUserId());//有缓存的实体可以先获取再更新
        if(update==null){
            return false;
        }else{
            update.copyPropertiesFromAndSkipNull(userBaseInfo);
            update.update();
        }
        return true;
    }

    @Override
    public boolean changeToken(String token) {
        long userId = currentUserId();
        String column = StringUtil.isEmail(token)?"email":"mobile";
        int result =  UserEntity.dao.eq("id",userId).set(column,token);
        return result>0;
    }

    @Override
    public boolean changePwd(String oldPwd, String newPwd) {
        UserEntity userEntity = getCurrentUser();
        if(!PasswordEncryptor.matches(oldPwd,userEntity.getPassword())){
            return false;
        }
        newPwd = PasswordEncryptor.encode(newPwd);
        int result =  UserEntity.dao.eq("id",currentUserId()).set("password",newPwd);
        return result>0;
    }

    @Override
    public boolean resetPwd(String newPwd,String token) {
        UserEntity userEntity = findByToken(token);
        if(userEntity==null){
            return  false;
        }
        newPwd = PasswordEncryptor.encode(newPwd);
        int result =  UserEntity.dao.eq("id",userEntity.getId()).set("password",newPwd);
        return result>0;
    }

    @Override
    public boolean editDetailInfo(EditUserDetailForm editUserDetailForm) {
        int result =  UserEntity.dao.id(currentUserId()).setByObject(editUserDetailForm);
        return result>0;
    }

    @Override
    public boolean changePublicType(int publicType) {

        return UserBaseInfo.dao.id(currentUserId()).set("publicType",publicType)>0;
    }

    @Override
    public ResultUserInfo getUserInfo(long userId) {
        long thisUserId = currentUserId();
        UserBaseInfo userBaseInfo =UserBaseInfo.dao.findById(userId);
        if(userBaseInfo.getPublicType()== PublicType.ALL||(userBaseInfo.getPublicType()==PublicType.TO_ALLIANCE&&allianceService.inSameAlliance(userId,thisUserId))){//如果公开或者对盟内成员公开
            return ResultUserInfo.dao.id(userId).selectOne();
        }

        ResultUserInfo resultUserInfo = new ResultUserInfo();//只返回基本信息
        UserEntity userEntity = UserEntity.dao.findById(userId);
        UserPrivateInfoEntity userPrivateInfoEntity = UserPrivateInfoEntity.dao.partitionId(userId).selectOne();
        resultUserInfo.setNikeNames(userEntity.getNicknames());
        resultUserInfo.setAvatar(userEntity.getAvatar());
        resultUserInfo.setSuperId(userEntity.getSuperid());
        resultUserInfo.setIsAuthenticated(userEntity.getIsAuthenticated());
        resultUserInfo.setGender(userEntity.getGender());
        resultUserInfo.setAddress(userEntity.getAddress());

        //TODO 盟和角色没搞

        if(userPrivateInfoEntity.isPersonalTags()){
            //TODO 标签还没弄
        }

        if(userPrivateInfoEntity.isActualName()){
            resultUserInfo.setUsername(userEntity.getUsername());
        }

        if(userPrivateInfoEntity.isIdentityCard()){
            resultUserInfo.setIdCard(userEntity.getIdCard());
        }

        if(userPrivateInfoEntity.isPhoneNumber()){
            resultUserInfo.setMobile(userEntity.getMobile());
        }

        if(userPrivateInfoEntity.isMail()){
            resultUserInfo.setEmail(userEntity.getEmail());
        }

        if(userPrivateInfoEntity.isBirthday()){
            resultUserInfo.setBirthday(userEntity.getBirthday());
        }
        //resultUserInfo.copyPropertiesFrom(userBaseInfo);
        resultUserInfo.setMembers(getAffairMember());
        return resultUserInfo;
    }

    @Override
    public Map<Long, List<Object>> getAffairMember() {
        StringBuilder sb = new StringBuilder("select a.* , b.title from affair_member a join (select id,user_id,title from role where user_id = ? ) b on a.role_id = b.id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(currentUserId());
        List<AffairMemberVO> affairMemberVOList = AffairMemberEntity.getSession().findList(AffairMemberVO.class,sb.toString(),p);
        Map<Long,List<Object>> members = new HashedMap();
        for(AffairMemberVO a : affairMemberVOList){
            List<Object> user = new ArrayList<>();
            user.add(a.getAffairId());
            SimpleRoleVO role = new SimpleRoleVO(a.getRoleId(),a.getTitle());
            user.add(role);
            members.put(a.getId(),user);
        }
        return members;
    }

    @Override
    public boolean validSuperId(String superId) {
        return UserEntity.dao.eq("superid",superId).exists();
    }

    @Override
    public boolean modifySuperId(String superId) {
        //检测修改的superid是否符合长度等要求,是不是要放在前端
        return UserEntity.dao.id(currentUserId()).set("superid",superId)>0;
    }

    private String generateSuperId(long id){
        String superid = id+"";
        int length = 8 - superid.length();
        for(int i = 0 ; i<length ; i++){
            superid = "0"+superid;
        }
        return superid;
    }
}
