package cn.superid.webapp.controller;

import cn.superid.utils.StringUtil;
import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.model.UserEntity;
import cn.superid.webapp.security.IAuth;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.AliSmsDao;
import cn.superid.webapp.utils.CheckFrequencyUtil;
import cn.superid.webapp.utils.PasswordEncryptor;
import cn.superid.webapp.forms.SimpleResponse;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


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
    public static Logger LOG = LoggerFactory.getLogger(UserController.class);


    /**
     * 获取注册验证码,不允许同一个ip地址频繁访问
     * @param request
     * @param token
     * @return
     */
    @ApiOperation(value = "获取注册验证码", httpMethod = "GET", response = boolean.class, notes = "不允许同一个IP地址频繁访问")
    @NotLogin
    @RequestMapping(value = "/get_register_code", method = RequestMethod.GET)
    public SimpleResponse getRegisterVerifyCode(HttpServletRequest request, String token){
        LOG.info("get_register_code",request.getRemoteAddr());
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
     * 获取登录验证码
     * @param request
     * @param token
     * @return
     */
    @ApiOperation(value = "获取登录验证码", httpMethod = "GET", response = boolean.class, notes = "获取登录验证码")
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


    @ApiOperation(value = "用户登录", httpMethod = "POST", response = UserEntity.class, notes = "用户登录")
    @NotLogin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SimpleResponse login(String token,String password,String verifyCode){
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
        auth.authUser(userEntity.getId());
        return SimpleResponse.ok(userEntity);
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


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public  SimpleResponse logout(){
        auth.unAuthUser();
        return SimpleResponse.ok("success");
    }

    /**
     * TODO 用户信息修改
     */


    /**
     *
     */

}