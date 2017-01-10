package cn.superid.webapp.controller;

import cn.superid.ValidateCode;
import cn.superid.utils.MobileUtil;
import cn.superid.utils.StringUtil;
import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.controller.VO.LoginUserInfoVO;
import cn.superid.webapp.controller.forms.ChangePublicTypeForm;
import cn.superid.webapp.controller.forms.UserPrivateInfoForm;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.*;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.model.UserPrivateInfoEntity;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import cn.superid.webapp.utils.PasswordEncryptor;
import cn.superid.webapp.utils.SmsType;
import cn.superid.webapp.utils.token.TokenUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by zp on 2016/8/3.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAffairMemberService affairMemberService;

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IAuth auth;
    public static Log LOG = LogFactory.getLog(UserController.class);
    private long userId;


    @ApiOperation(value = "检查服务器状态,并且获取jSessionId", httpMethod = "GET", response = String.class)
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public SimpleResponse ping(){
        return SimpleResponse.ok("isLogin");
    }

    @ApiOperation(value = "根据userid,获得", httpMethod = "GET", response = String.class)
    @RequestMapping(value = "/avatar/{userId}", method = RequestMethod.GET)
    public SimpleResponse getAvatar(@PathVariable String userId){
//        this.userId = userId;
        return SimpleResponse.ok("isLogin");
    }

    /**
     * 获取注册验证码,不允许同一个ip地址频繁访问
     * @param request
     * @param token
     * @return
     */

    @ApiOperation(value = "获取注册验证码", httpMethod = "GET", response = String.class, notes = "不允许同一个IP地址频繁访问,若是手机,格式为+86 15***")
    @NotLogin
    @RequestMapping(value = "/get_register_code", method = RequestMethod.GET)
    public SimpleResponse getRegisterVerifyCode(HttpServletRequest request, String token){
        if(CheckFrequencyUtil.isFrequent(request.getRemoteAddr())){
            LOG.warn(String.format("ip %s, token %s, frequent get register code",request.getRemoteAddr(),token));
            return SimpleResponse.error("请求过于频繁");
        }
        if(StringUtil.isEmpty(token)){
            return new SimpleResponse(ResponseCode.BadRequest,null);
        }else {
            if((!StringUtil.isEmail(token))&&(!MobileUtil.isValidFormat(token))){
                return new SimpleResponse(ResponseCode.InvalidMobileFormat,"该手机号格式不正确");
            }

            if(userService.validToken(token)){
                return new SimpleResponse(ResponseCode.HasRegistered,"此账号已被注册");
            }


            return new SimpleResponse(ResponseCode.OK,userService.getVerifyCode(token, SmsType.registerCode));
        }
    }


    /**
     * 获取身份验证码
     * @param request
     * @param token
     * @return
     */
    @ApiOperation(value = "获取身份验证码", httpMethod = "GET", response = String.class, notes = "获取身份验证码,一般用于与登录注册无关的系统验证")
    @RequestMapping(value = "/get_verify_code", method = RequestMethod.GET)
    public SimpleResponse getVerifyCode(HttpServletRequest request,String token){
        if(CheckFrequencyUtil.isFrequent(request.getRemoteAddr())){
            LOG.warn(String.format("ip %s, token %s, frequent get verify code",request.getRemoteAddr(),token));
            return SimpleResponse.error("frequent_request");
        }
        if(StringUtil.isEmpty(token)){
            return new SimpleResponse(ResponseCode.BadRequest,null);
        }else {
            return new SimpleResponse(ResponseCode.OK,userService.getVerifyCode(token, SmsType.checkIdentityCode));
        }
    }

    /**
     * 获取重置密码验证码
     * @param request
     * @param token
     * @return
     */
    @NotLogin
    @ApiOperation(value = "获取身份验证码,目前用于重置密码,不需要登录", httpMethod = "GET", response = String.class, notes = "获取身份验证码,一般用于与登录注册无关的系统验证")
    @RequestMapping(value = "/get_reset_code", method = RequestMethod.GET)
    public SimpleResponse getResetCode(HttpServletRequest request,String token){
        try{
            if(CheckFrequencyUtil.isFrequent(request.getRemoteAddr())){
                LOG.warn(String.format("ip %s, token %s, frequent get verify code",request.getRemoteAddr(),token));
                return SimpleResponse.error("frequent_request");
            }
            if(StringUtil.isEmpty(token)){
                return new SimpleResponse(ResponseCode.BadRequest,null);
            }else {
                if(userService.validTokenForReset(token)){//没有注册的号码
                    return new SimpleResponse(ResponseCode.NotRegistered,"该账号不存在");
                }
                auth.setSessionAttr("token",token);
                return new SimpleResponse(ResponseCode.OK,userService.getVerifyCode(token, SmsType.checkIdentityCode));
            }
        }catch (Exception e){
            return new SimpleResponse(ResponseCode.BadRequest,"访问过于频繁");
        }

    }

    /**
     * 获取登录验证码
     * @param request
     * @param token
     * @return
     */
    @ApiOperation(value = "获取登录验证码", httpMethod = "GET", response = String.class, notes = "获取登录验证码")
    @NotLogin
    @RequestMapping(value = "/get_login_code", method = RequestMethod.GET)
    public SimpleResponse getLoginVerifyCode(HttpServletRequest request,String token){
        if(CheckFrequencyUtil.isFrequent(request.getRemoteAddr())){
            LOG.warn(String.format("ip %s, token %s, frequent get login code",request.getRemoteAddr(),token));
            return SimpleResponse.error("frequent_request");
        }
        if(StringUtil.isEmpty(token)){
            return new SimpleResponse(ResponseCode.BadRequest,null);
        }else {
            return new SimpleResponse(ResponseCode.OK,userService.getVerifyCode(token, SmsType.loginCode));
        }
    }

    @ApiOperation(value = "用户注册", httpMethod = "POST", response = SimpleResponse.class, notes = "用户注册,手机号码需要加国家区号,例如+86 15***,表单传参")
    @NotLogin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public SimpleResponse register(String token,String password,String username,String verifyCode){
        if(!userService.checkVerifyCode(verifyCode,token)){
            return new SimpleResponse(ResponseCode.ErrorVerifyCode,"验证码错误");
        }

        if(StringUtil.isEmpty(username) | StringUtil.isEmpty(password)){
            return new SimpleResponse(ResponseCode.ErrorUserNameOrPassword,"不正确的用户名或密码");
        }

        if(userService.validToken(token)){
            return new SimpleResponse(ResponseCode.HasRegistered,"该账号已注册");
        }

        if((!StringUtil.isEmail(token))&&(!MobileUtil.isValidFormat(token))){
            return new SimpleResponse(ResponseCode.InvalidMobileFormat,"该手机号格式不正确");
        }
        UserEntity result = userService.createUser(token,password,username);
        if(result!=null){
            return SimpleResponse.ok(result);
        }else{
            return SimpleResponse.error("server_error");
        }
    }

    @ApiOperation(value = "判断验证码是否正确", httpMethod = "POST", response = SimpleResponse.class, notes = "判断验证码是否正确")
    @NotLogin
    @RequestMapping(value = "/check_token", method = RequestMethod.POST)
    //public SimpleResponse checkToken(String verifyCode){
    public SimpleResponse checkToken(String token,String verifyCode){
        if(StringUtil.isEmpty(verifyCode)){
            return new SimpleResponse(ResponseCode.NeedVerifyCode,"验证码不能为空");
        }
        if(!userService.checkVerifyCode(verifyCode,token)){
            return new SimpleResponse(ResponseCode.ErrorVerifyCode,"验证码错误");
        }
        auth.setSessionAttr("verified_time",new Date());
        return SimpleResponse.ok("success");
    }

    @ApiOperation(value = "忘记密码", httpMethod = "POST", response = SimpleResponse.class, notes = "忘记密码,表单传参")
    @NotLogin
    @RequestMapping(value = "/forget_pwd", method = RequestMethod.POST)
    public SimpleResponse forgetPwd(String verifyCode,String newPwd){
        if(StringUtil.isEmpty(verifyCode) | StringUtil.isEmpty(newPwd)){
            return new SimpleResponse(ResponseCode.BadRequest,"params cannot be null");
        }
        if(!userService.checkVerifyCode(verifyCode,null)){
            return new SimpleResponse(ResponseCode.ErrorVerifyCode,"验证码不正确");
        }
        return new SimpleResponse(userService.forgetPwd(newPwd,(String) auth.getSessionAttr("token")));
    }

    @ApiOperation(value = "修改手机或者邮箱号码",response = SimpleResponse.class, notes = "表单传参")
    @RequestMapping(value = "/change_mobile_or_email",method = RequestMethod.POST)
    public SimpleResponse changeMobileOrEmail(String token,String verifyCode){
        if(StringUtil.isEmpty(verifyCode) | StringUtil.isEmpty(token)){
            return new SimpleResponse(ResponseCode.BadRequest,"params cannot be null");
        }
        Date date =(Date) auth.getSessionAttr("verified_time");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 15);
        if (calendar.getTime().before(new Date())) {//超过15分钟
            return SimpleResponse.error("not_verified_time");
        }
        if (!userService.checkVerifyCode(verifyCode,token)){
            return new SimpleResponse(ResponseCode.ErrorVerifyCode,"验证码不正确");

        }
        return new SimpleResponse(userService.changeToken(token));

    }



    @ApiOperation(value = "用户登录", httpMethod = "POST", response = UserEntity.class, notes = "用户登录,picCode是图片验证码,表单传参")
    @NotLogin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SimpleResponse login(String token, String password, String verifyCode,HttpServletRequest request){

        if (StringUtil.isEmpty(token) | StringUtil.isEmpty(password)){
            return new SimpleResponse(ResponseCode.BadRequest,"params cannot be null");
        }

        if(!userService.validToken(token)){
            return new SimpleResponse(ResponseCode.NotRegistered,"该账号不存在");
        }


        int limit =5;
        UserEntity userEntity =userService.findByToken(token);
        if(CheckFrequencyUtil.isFrequent(token,limit)) {//超过五次需要验证码

            if("".equals(verifyCode)||(verifyCode == null)){
                return new SimpleResponse(ResponseCode.NeedVerifyCode,"需要验证码");
            }
            if(!PasswordEncryptor.matches(password,userEntity.getPassword())){
                return new SimpleResponse(ResponseCode.ErrorUserNameOrPassword,"密码错误");
            }
            if(userService.checkVerifyCode(verifyCode,token)){
                CheckFrequencyUtil.reset(token);
            }else{
                //LOG.warn(String.format("ip %s, token %s, login error >5",request.getRemoteAddr(),token));
                //return new SimpleResponse(ResponseCode.Frequency,"访问过于频繁");
                return new SimpleResponse(ResponseCode.ErrorVerifyCode,"验证码错误");
            }
        }else{
            if(!PasswordEncryptor.matches(password,userEntity.getPassword())){
                if(CheckFrequencyUtil.getCounts(token) == (limit-1)){
                    return new SimpleResponse(ResponseCode.NeedVerifyCode,"密码错误");
                }
                return new SimpleResponse(ResponseCode.ErrorUserNameOrPassword,"密码错误");
            }

        }


        String chatToken = TokenUtil.setLoginToken(userEntity.getId());
        userEntity.setChatToken(chatToken);
        auth.authUser(userEntity.getId(), chatToken);
        CheckFrequencyUtil.reset(token);


        LoginUserInfoVO loginUserInfoVO = new LoginUserInfoVO();
        userEntity.copyPropertiesTo(loginUserInfoVO);

        return SimpleResponse.ok(loginUserInfoVO);
    }

    @ApiOperation(value = "验证用户名", response = boolean.class, notes = "验证用户名,表单传参")
    @NotLogin
    @RequestMapping(value = "/valid_username", method = RequestMethod.POST)
    public  SimpleResponse validUsername(String username){
        if(StringUtil.isEmpty(username)){
            return new SimpleResponse(ResponseCode.BadRequest,"params cannot be null");
        }
        return SimpleResponse.ok(userService.validUsername(username));
    }

    /**
     * 手机或者邮箱是否合法,格式正确而且没有被注册
     * @param token
     * @return
     */
    @ApiOperation(value = "验证手机邮箱是否合法", response = boolean.class, notes = "格式正确而且没有被注册,表单传参")
    @NotLogin
    @RequestMapping(value = "/valid_token", method = RequestMethod.POST)
    public  SimpleResponse validToken(String token){
        if(token==null) {
            return SimpleResponse.error("token is null");
        }
        return SimpleResponse.ok(!userService.validToken(token));
    }

    @ApiOperation(value = "登出", response = String.class, notes = "登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public  SimpleResponse logout(){
        auth.unAuthUser();
        return SimpleResponse.ok("success");
    }

    /**
     * 修改用户信息
     */
    @ApiOperation(value = "修改用户信息", response = String.class,notes = "修改用户")
    @RequestMapping(value = "/edit_base", method = RequestMethod.POST)
    public  SimpleResponse editBase(EditUserDetailForm editUserDetailForm){
        if(editUserDetailForm == null){
            return new SimpleResponse(ResponseCode.BadRequest,null);
        }
        return new SimpleResponse(userService.editBaseInfo(editUserDetailForm));
    }


    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", response = String.class, notes = "表单传参")
    @RequestMapping(value = "/change_pwd", method = RequestMethod.POST)
    public  SimpleResponse changePwd(String oldPwd,String newPwd){
        if(StringUtil.isEmpty(oldPwd) | StringUtil.isEmpty(newPwd)){
            return new SimpleResponse(ResponseCode.BadRequest,"params cannot be null");
        }
        return new SimpleResponse(userService.changePwd(oldPwd,newPwd));
    }


    @ApiOperation(value = "获取自己的详细消息", response = ResultUserInfo.class,notes = "如果获取本人信息,则不需要传userId,表单传参")
    @RequestMapping(value = "/user_info", method = RequestMethod.GET)
    public  SimpleResponse getUserInfo(){
        ResultUserInfo resultUserInfo = new ResultUserInfo();
        UserEntity user = userService.getCurrentUser();
        user.copyPropertiesTo(resultUserInfo);
        UserPrivateInfoForm userPrivateInfoForm = new UserPrivateInfoForm();
        userPrivateInfoForm.copyPropertiesFromAndSkipNull(UserPrivateInfoEntity.dao.partitionId(userService.currentUserId()).selectOne());
        resultUserInfo.setUserPrivateInfoForm(userPrivateInfoForm);
        resultUserInfo.setNickNames(Arrays.asList(user.getNicknames().split(",")));
        return SimpleResponse.ok(resultUserInfo);
    }

    /**
     * 响应验证码页面
     * @return
     */
    @ApiOperation(value = "获取图片验证码",response = String.class,notes = "不停地访问,访问一次生成一次验证码,需要传个token过来")
    @NotLogin
    @RequestMapping(value="/validate_code",method = RequestMethod.GET)
    public String validateCode(HttpServletRequest request,HttpServletResponse response,String token) throws Exception{
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        HttpSession session = request.getSession();

        ValidateCode vCode = new ValidateCode(120,40,4,100);
        auth.setSessionAttr("code", vCode.getCode());
        auth.setSessionAttr("last_token_time",new Date());
        auth.setSessionAttr("token",token);

        vCode.write(response.getOutputStream());
        return "ok";
    }

    @RequestMapping(value = "/valid_image_code",method = RequestMethod.POST)
    public SimpleResponse validValidateCode(HttpServletRequest request){
        String code = request.getParameter("pic_code");
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute("pic_code");
        if (!StringUtils.equalsIgnoreCase(code, sessionCode)) {  //忽略验证码大小写
            return SimpleResponse.error("验证码不正确");
        }
        else {
            return SimpleResponse.ok("");
        }
    }

    @ApiOperation(value = "得到该用户所有affairMemberId",response = String.class,notes = "")
    @RequestMapping(value="/get_affairMember",method = RequestMethod.POST)
    public SimpleResponse getAffairMember(HttpServletRequest request,HttpServletResponse response) throws Exception{
        return SimpleResponse.ok(affairMemberService.getAffairMember());
    }

    @ApiOperation(value = "得到用户的公开性",response = String.class,notes = "")
    @RequestMapping(value="/get_public_property",method = RequestMethod.GET)
    public SimpleResponse getPublicProperty(){
        return SimpleResponse.ok(userService.getPublicProperty(userService.currentUserId()));
    }

    @ApiOperation(value = "设置详细信息公开性", response = String.class)
    @RequestMapping(value = "/change_public_type", method = RequestMethod.POST)
    public  SimpleResponse changePublicType(ChangePublicTypeForm publicType){
        return new SimpleResponse(userService.changePublicType(publicType));
    }


    @RequestMapping(value = "/rollback",method = RequestMethod.POST)
    @NotLogin
    public SimpleResponse rollbackTest(){
        try{
            userService.rollbackTest();
        }catch (RuntimeException e){
            e.printStackTrace();
            return SimpleResponse.error("");
        }

        return SimpleResponse.ok("");
    }
}
