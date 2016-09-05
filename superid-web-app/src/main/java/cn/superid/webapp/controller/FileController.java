package cn.superid.webapp.controller;

import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.AliOssDao;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@RequestMapping("/affair")
public class FileController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "获取上传token", response = SimpleResponse.class, notes = "格式正确而且没有被注册")
    @RequestMapping(value = "/get_token", method = RequestMethod.GET)
    public SimpleResponse getToken() {
        StringBuffer sb =new StringBuffer("user/");
        sb.append(userService.currentUserId());
        return  SimpleResponse.ok(AliOssDao.generateToken(sb.toString()));
    }


}
