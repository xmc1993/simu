package cn.superid.webapp.controller;

import cn.superid.utils.StringUtil;
import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.*;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.AliSmsDao;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import cn.superid.webapp.utils.PasswordEncryptor;
import cn.superid.webapp.utils.token.TokenUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by zp on 2016/8/3.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IAuth auth;
    public static Log LOG = LogFactory.getLog(UserController.class);


    /**
     * 获取注册验证码,不允许同一个ip地址频繁访问
     * @param request
     * @param token
     * @return
     */
    @ApiOperation(value = "获取注册验证码", httpMethod = "GET", response = String.class, notes = "不允许同一个IP地址频繁访问")
    @NotLogin
    @RequestMapping(value = "/get_register_code", method = RequestMethod.GET)
    public SimpleResponse getRegisterVerifyCode(HttpServletRequest request, String token){
        if(CheckFrequencyUtil.isFrequent(request.getRemoteAddr())){
            return SimpleResponse.error("frequent_request");
        }
        if(StringUtil.isEmpty(token)){
            return new SimpleResponse(ResponseCode.BadRequest,null);
        }else {
            return new SimpleResponse(ResponseCode.OK,userService.getVerifyCode(token, AliSmsDao.registerCode));
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
            return SimpleResponse.error("frequent_request");
        }
        if(StringUtil.isEmpty(token)){
            return new SimpleResponse(ResponseCode.BadRequest,null);
        }else {
            return new SimpleResponse(ResponseCode.OK,userService.getVerifyCode(token, AliSmsDao.checkIdentityCode));
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
            return SimpleResponse.error("frequent_request");
        }
        if(StringUtil.isEmpty(token)){
            return new SimpleResponse(ResponseCode.BadRequest,null);
        }else {
            return new SimpleResponse(ResponseCode.OK,userService.getVerifyCode(token, AliSmsDao.loginCode));
        }
    }

    @ApiOperation(value = "用户注册", httpMethod = "POST", response = SimpleResponse.class, notes = "用户注册")
    @NotLogin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public SimpleResponse register(String token,String password,String username,String verifyCode){
        if(!userService.checkVerifyCode(verifyCode)){
            return new SimpleResponse(ResponseCode.BadRequest,"error_verifyCode");
        }

        UserEntity userEntity=new UserEntity();
        if(StringUtil.isEmail(token)){
            userEntity.setEmail(token);
        }else if(StringUtil.isMobile(token)){
            userEntity.setMobile(token);
        }
        if(!userService.validToken(token)){
            return new SimpleResponse(ResponseCode.BadRequest,"error_token");
        }
        userEntity.setPassword(PasswordEncryptor.encode(password));
        userEntity.setUsername(username);
        UserEntity result = userService.createUser(userEntity);
        if(result!=null){
            return SimpleResponse.ok(result);
        }else{
            return SimpleResponse.error("server_error");
        }
    }

    @ApiOperation(value = "判断验证码是否正确", httpMethod = "POST", response = SimpleResponse.class, notes = "判断验证码是否正确")
    @RequestMapping(value = "/check_token", method = RequestMethod.POST)
    public SimpleResponse checkToken(String verifyCode){
        if(!userService.checkVerifyCode(verifyCode)){
            return new SimpleResponse(ResponseCode.BadRequest,"error_verifyCode");
        }
        auth.setSessionAttr("verified_time",new Date());
        return SimpleResponse.ok("success");
    }

    @ApiOperation(value = "修改手机或者邮箱号码",response = SimpleResponse.class)
    @RequestMapping(value = "/change_mobile_or_email",method = RequestMethod.POST)
    public SimpleResponse changeMobileOrEmail(String token,String verifyCode){
        Date date =(Date) auth.getSessionAttr("verified_time");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, 15);
        if (calendar.getTime().before(new Date())) {//超过15分钟
            return SimpleResponse.error("not_verified_time");
        }
        if (!userService.checkVerifyCode(verifyCode)){
            return new SimpleResponse(ResponseCode.BadRequest,"error_verifyCode");

        }
        return new SimpleResponse(userService.changeToken(token));

    }



    @ApiOperation(value = "用户登录", httpMethod = "POST", response = UserDto.class, notes = "用户登录")
    @NotLogin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SimpleResponse login(String token, String password, String verifyCode){
        int limit =3;
         UserEntity userEntity =userService.findByToken(token);

        if(userEntity==null){
            if(CheckFrequencyUtil.isFrequent(token,limit)){//超过三次需要验证码
                if(userService.checkVerifyCode(verifyCode)){
                    CheckFrequencyUtil.reset(token);
                }else{
                    return SimpleResponse.error("need_verify_code");
                }
            }
            return SimpleResponse.error("not_exist");
        }

        if(!PasswordEncryptor.matches(password,userEntity.getPassword())){
            if(CheckFrequencyUtil.isFrequent(token,limit)){//超过三次需要验证码
                if(userService.checkVerifyCode(verifyCode)){
                    CheckFrequencyUtil.reset(token);
                }else{
                    return SimpleResponse.error("need_verify_code");
                }
            }
            return SimpleResponse.error("pwd_error");
        }

        String chatToken = TokenUtil.setLoginToken(userEntity.getId());
        UserDto userDto = UserDto.UserEntity2UserDto(userEntity);
        userDto.setChatToken(chatToken);
        auth.authUser(userEntity.getId(), chatToken);
        return SimpleResponse.ok(userDto);
    }

    @ApiOperation(value = "验证用户名", response = boolean.class, notes = "验证用户名")
    @NotLogin
    @RequestMapping(value = "/valid_username", method = RequestMethod.POST)
    public  SimpleResponse validUsername(String username){
        return SimpleResponse.ok(userService.validUsername(username));
    }

    /**
     * 手机或者邮箱是否合法,格式正确而且没有被注册
     * @param token
     * @return
     */
    @ApiOperation(value = "验证手机邮箱是否合法", response = boolean.class, notes = "格式正确而且没有被注册")
    @NotLogin
    @RequestMapping(value = "/valid_token", method = RequestMethod.POST)
    public  SimpleResponse validToken(String token){
        if(token==null) {
            return SimpleResponse.error("token is null");
        }
        return SimpleResponse.ok(userService.validToken(token));
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
    @ApiOperation(value = "修改用户信息", response = String.class,notes = "修改用户信息")
    @RequestMapping(value = "/edit_base", method = RequestMethod.POST)
    public  SimpleResponse editBase(@RequestBody EditUserBaseInfo userBaseInfo){
        return new SimpleResponse(userService.editBaseInfo(userBaseInfo));
    }


    /**
     * 修改密码
     */
    @ApiOperation(value = "修改密码", response = String.class)
    @RequestMapping(value = "/change_pwd", method = RequestMethod.POST)
    public  SimpleResponse changePwd(String oldPwd,String newPwd){
        return new SimpleResponse(userService.changePwd(oldPwd,newPwd));
    }



    @ApiOperation(value = "编辑详细信息", response = String.class)
    @RequestMapping(value = "/edit_detail", method = RequestMethod.POST)
    public  SimpleResponse editDetail(@RequestBody EditUserDetailForm editUserDetailForm){
        return new SimpleResponse(userService.editDetailInfo(editUserDetailForm));
    }

    @ApiOperation(value = "设置详细信息公开性", response = String.class)
    @RequestMapping(value = "/change_public_type", method = RequestMethod.POST)
    public  SimpleResponse changePublicType(int publicType){
        return new SimpleResponse(userService.changePublicType(publicType));
    }

    @ApiOperation(value = "获取用户的详细消息", response = ResultUserInfo.class,notes = "如果获取本人信息,则不需要传userId")
    @RequestMapping(value = "/user_info", method = RequestMethod.POST)
    public  SimpleResponse getUserInfo(Long userId){
        if(userId==null||userId==userService.currentUserId()){
            return SimpleResponse.ok(userService.getCurrentUser());
        }else{
            ResultUserInfo resultUserInfo=userService.getUserInfo(userId);
            return new SimpleResponse(resultUserInfo==null?-1:0,resultUserInfo);
        }
    }

}
