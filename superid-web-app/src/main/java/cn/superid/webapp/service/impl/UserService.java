package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.SQLDao;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.utils.FileUtil;
import cn.superid.utils.MapUtil;
import cn.superid.utils.MobileUtil;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.controller.VO.SimpleAllianceVO;
import cn.superid.webapp.controller.VO.SimpleRoleVO;
import cn.superid.webapp.controller.VO.UserAllianceRolesVO;
import cn.superid.webapp.enums.type.PublicType;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.forms.EditUserBaseInfo;
import cn.superid.webapp.forms.EditUserDetailForm;
import cn.superid.webapp.forms.ResultUserInfo;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IAllianceService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.vo.AffairMemberVO;
import cn.superid.webapp.service.vo.AllianceRolesVO;
import cn.superid.webapp.utils.*;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        user.setModifyTime(TimeUtil.getCurrentSqlTime());
        user.update();
        if(allianceEntity==null) return null;
        return userEntity;
    }

    @Override
    public UserEntity findByToken(String token) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("zp");
        userEntity.save();
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
        resultUserInfo.setNickNames(userEntity.getNicknames());
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
        return getMaps(affairMemberVOList);
    }

    @Override
    public Map<Long, List<Object>> getAffairMemberByAllianceId(long allianceId) {
        StringBuilder sb = new StringBuilder("select a.* , b.title from affair_member a join (select id,user_id,title from role where alliance_id = ? and user_id = ? ) b on a.role_id = b.id ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(currentUserId());
        List<AffairMemberVO> affairMemberVOList = AffairMemberEntity.getSession().findList(AffairMemberVO.class,sb.toString(),p);

        return getMaps(affairMemberVOList);
    }

    private Map<Long, List<Object>> getMaps(List<AffairMemberVO> affairMemberVOList){
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
    //public List<UserAllianceRolesVO> getUserAllianceRoles() {
      public List<AllianceRolesVO> getUserAllianceRoles(){
        /*
        //算法1
        //先取出用户拥有的所有盟
        StringBuilder sb = new StringBuilder("select a.id, a.name from alliance a where a.id in (select alliance_id from role r where r.user_id = ? group by alliance_id)");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(currentUserId());
        List<SimpleAllianceVO> allianceVOs = AllianceEntity.getSession().findList(SimpleAllianceVO.class,sb.toString(),p);

        //对于该用户的每个盟
        for(SimpleAllianceVO simpleAllianceVO : allianceVOs){
            UserAllianceRolesVO userAllianceRolesVO = new UserAllianceRolesVO();
            userAllianceRolesVO.setAllianceId(simpleAllianceVO.getId());
            userAllianceRolesVO.setAllianceName(simpleAllianceVO.getName());
            //根据allianceId和userId去role表中取出相应的数据
            sb = new StringBuilder("select r.id, r.title from role r where r.user_id = ? and r.alliance_id = ?");
            p = new ParameterBindings();
            p.addIndexBinding(currentUserId());
            p.addIndexBinding(simpleAllianceVO.getId());
            List<SimpleRoleVO> simpleRoleVOs = RoleEntity.getSession().findList(SimpleRoleVO.class,sb.toString(),p);
            //将list转为字符串放到userAllianceRole中
            userAllianceRolesVO.setRoles(simpleRoleVOs);
            roles.add(userAllianceRolesVO);
        }
        */

        /*
        算法2
        sb = new StringBuilder(
                "select t1.id,t1.name,t2.role_id,t2.title  from  (select a.id,a.name from alliance  a where a.id in (select alliance_id from role where user_id = 1893 group by alliance_id)) t1 " +
                "join" +
                "(select r.id as role_id,r.title,r.alliance_id from role r where r.user_id = 1893) t2 " +
                "on t1.id = t2.alliance_id ");
        testVos = AllianceEntity.getSession().findList(AllianceRolesVO.class,sb.toString());
        List<Long> allianceIds = new ArrayList<>();
        List<SimpleRoleVO> simpleRoleVOs = new ArrayList<>();
        //用于定位,用法在循环里有解释
        int index = -1;
        //下面的这个for循环的前提条件是取出来的数据是group by allianceId的,不然就错了
        //应该是group的,因为t1表是Group盟的,就是说在join的时候一张表是已经group的
        //算法的思路是,先将盟id抽取出来,然后遍历testVos,如果盟id相同就更新结果list中的相应的盟id的记录
        for(AllianceRolesVO allianceRolesVO : testVos){
            if(allianceIds.contains(allianceRolesVO.getId())){
                SimpleRoleVO simpleRoleVO = new SimpleRoleVO();
                simpleRoleVO.setRoleId(allianceRolesVO.getRoleId());
                simpleRoleVO.setRoleName(allianceRolesVO.getName());
                simpleRoleVOs.add(simpleRoleVO);
                roles2.get(index).setRoles(simpleRoleVOs);
                continue;
            }

            UserAllianceRolesVO userAllianceRolesVO = new UserAllianceRolesVO();
            userAllianceRolesVO.setAllianceId(allianceRolesVO.getId());
            userAllianceRolesVO.setAllianceName(allianceRolesVO.getName());
            //碰到不一样的就重置simpleRoles
            simpleRoleVOs = new ArrayList<>();
            SimpleRoleVO simpleRoleVO = new SimpleRoleVO();
            simpleRoleVO.setRoleId(allianceRolesVO.getRoleId());
            simpleRoleVO.setRoleName(allianceRolesVO.getName());
            simpleRoleVOs.add(simpleRoleVO);
            userAllianceRolesVO.setRoles(simpleRoleVOs);
            roles2.add(userAllianceRolesVO);
            //定位现在的userAllianceRolesVO
            index++;

            allianceIds.add(allianceRolesVO.getId());

        }
         */
        //这样的格式是王海青强烈要求。。。。
        StringBuilder sb = new StringBuilder(SQLDao.GET_USER_ALLIANCE_ROLES);
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(currentUserId());
        p.addIndexBinding(currentUserId());
        List<AllianceRolesVO> allianceRolesVOs = AllianceEntity.getSession().findList(AllianceRolesVO.class,sb.toString(),p);
        return allianceRolesVOs;
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

    @Override
    @Transactional
    public void rollbackTest() {
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding("汤茂思");
        p.addIndexBinding(1899L);
        StringBuilder correctSql = new StringBuilder("update user set username = ? where id= ?");
        UserEntity.getSession().execute(correctSql.toString(),p);
        /*
            StringBuilder incorrectSql = new StringBuilder("update user set userame = 'tms' where id=1899");
            UserEntity.getSession().execute(incorrectSql.toString());
            */
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
