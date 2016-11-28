package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.forms.AddFileForm;
import cn.superid.webapp.controller.forms.CalculateSignForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IPictureService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;
import cn.superid.webapp.utils.AliOssDao;
import cn.superid.webapp.utils.TimeUtil;
import com.aliyun.oss.model.PutObjectResult;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPictureService pictureService;

    @ApiOperation(value = "获取头像上传token", response = SimpleResponse.class, notes = "格式正确而且没有被注册")
    @RequestMapping(value = "/get_token", method = RequestMethod.GET)
    public SimpleResponse getToken() {
        StringBuffer sb =new StringBuffer("user/");
        sb.append(userService.currentUserId());
        return  SimpleResponse.ok(AliOssDao.generateToken(sb.toString()));
    }

    @ApiOperation(value = "断点续传上传token", response = SimpleResponse.class, notes = "= =")
    @RequestMapping(value = "/get_video_token", method = RequestMethod.GET)
    public SimpleResponse getVideoToken(String objectKey,String method) {
        StringBuffer sb =new StringBuffer("user/");
        sb.append(userService.currentUserId());
        return  SimpleResponse.ok(AliOssDao.getVideoUploadSignature(objectKey,method));
    }

    @RequestMapping(value = "/get_signature", method = RequestMethod.POST)
    public SimpleResponse getSignature(@RequestBody  CalculateSignForm calculateSignForm) {
        return  SimpleResponse.ok(AliOssDao.getVideoUploadSignature(calculateSignForm.getVerb(),calculateSignForm.getContentMD5(),
                calculateSignForm.getContentType(),calculateSignForm.getCanonicalizedOSSHeaders(),
                calculateSignForm.getCanonicalizedResource()));
    }
    @ApiOperation(value = "获取事务文件上传token", response = SimpleResponse.class, notes = "格式正确而且没有被注册")
//    @RequiredPermissions(affair = AffairPermissions.UPLOAD_FILE)
    @RequestMapping(value = "/get_file_token", method = RequestMethod.GET)
    public SimpleResponse getFileToken(Long affairId) {
        StringBuffer sb =new StringBuffer("affair/");
//        sb.append(GlobalValue.currentAffairId());
        sb.append(affairId);
        return  SimpleResponse.ok(AliOssDao.generateToken(sb.toString()));
    }

    @ApiOperation(value = "得到该文件下所有直系子文件夹和文件", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/get_child", method = RequestMethod.POST)
    public SimpleResponse getChild(Long folderId) {
        if(folderId == null ){
            return SimpleResponse.error("参数错误");
        }
        List<FolderForm> folders = fileService.getChildFolder(folderId, GlobalValue.currentAffairId(),GlobalValue.currentAllianceId());
        List<FileForm> files = fileService.getChildFile(folderId,GlobalValue.currentAffairId(),GlobalValue.currentAllianceId());
        if(folders == null | files == null){
            return SimpleResponse.error("id不合法");
        }
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("folder", folders);
        rsMap.put("file", files);

        return  SimpleResponse.ok(rsMap);
    }

    @ApiOperation(value = "添加文件夹", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/add_folder", method = RequestMethod.POST)
    public SimpleResponse addFolder(Long folderId,String name,Long taskId) {
        if(folderId == null | name == null ){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.addFolder(folderId,name,GlobalValue.currentRoleId(),GlobalValue.currentAffairId(),taskId,GlobalValue.currentAllianceId());

        return SimpleResponse.ok(result);

    }

    @ApiOperation(value = "添加文件", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/add_file", method = RequestMethod.POST)
    public SimpleResponse addFile(AddFileForm form) {
        boolean result = fileService.addFile(form,GlobalValue.currentAffairId(),GlobalValue.currentAllianceId());

        return SimpleResponse.ok(result);

    }

    @ApiOperation(value = "删除文件", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/remove_file", method = RequestMethod.POST)
    public SimpleResponse removeFile(Long id ) {

        if(id == null ){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.removeFile(id,GlobalValue.currentAllianceId());


        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "删除文件夹", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/remove_folder", method = RequestMethod.POST)
    public SimpleResponse removeFolder(Long folderId ) {

        if( folderId == null){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.removeFolder(GlobalValue.currentAffairId(),folderId , GlobalValue.currentAllianceId());


        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "重命名文件夹", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/rename_folder", method = RequestMethod.POST)
    public SimpleResponse renameFolder( Long folderId , String name) {

        if(folderId == null | name == null){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.renameFolder(folderId,name, GlobalValue.currentAllianceId());


        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "得到某个文件历史版本", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/get_history", method = RequestMethod.POST)
    public SimpleResponse getHistoryFile( Long fileId ) {

        if( fileId == null){
            return SimpleResponse.error("参数错误");
        }
        List<FileForm> result = fileService.getHistoryFile(fileId,GlobalValue.currentAllianceId());


        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "缩放用户头像", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/condense_picture", method = RequestMethod.POST)
    public SimpleResponse condensePicture(@RequestParam("picture") CommonsMultipartFile picture) {


        //设置两个文件名
        String big = "user/"+userService.currentUserId()+"/"+ TimeUtil.getDate()+"."+picture.getContentType().split("/")[1];
        String small = "user/"+userService.currentUserId()+"/large_"+ TimeUtil.getDate()+"."+picture.getContentType().split("/")[1];

        return SimpleResponse.ok(fileService.condense_picture(picture,big,small,0));



    }

    @ApiOperation(value = "缩放盟头像", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/condense_alliance_picture", method = RequestMethod.POST)
    public SimpleResponse condenseAlliancePicture(@RequestParam("picture") CommonsMultipartFile picture , Long allianceId) {


        //设置两个文件名
        String big = "alliance/"+allianceId+"/"+ TimeUtil.getDate()+"."+picture.getContentType().split("/")[1];
        String small = "alliance/"+allianceId+"/large_"+ TimeUtil.getDate()+"."+picture.getContentType().split("/")[1];

        return SimpleResponse.ok(fileService.condense_picture(picture,big,small,0));



    }

    @ApiOperation(value = "缩放盟头像", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/condense_affair_picture", method = RequestMethod.POST)
    public SimpleResponse condenseAffairPicture(@RequestParam("picture") CommonsMultipartFile picture , Long affairId) {


        //设置两个文件名
        String big = "affair/"+affairId+"/"+ TimeUtil.getDate()+"."+picture.getContentType().split("/")[1];
        String small = "affair/"+affairId+"/large_"+ TimeUtil.getDate()+"."+picture.getContentType().split("/")[1];

        return SimpleResponse.ok(fileService.condense_picture(picture,big,small,0));



    }




}
