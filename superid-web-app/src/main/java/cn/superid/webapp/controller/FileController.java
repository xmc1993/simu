package cn.superid.webapp.controller;

import cn.superid.webapp.controller.forms.AddFileForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.service.forms.FileForm;
import cn.superid.webapp.service.forms.FolderForm;
import cn.superid.webapp.utils.AliOssDao;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @ApiOperation(value = "获取上传token", response = SimpleResponse.class, notes = "格式正确而且没有被注册")
    @RequestMapping(value = "/get_token", method = RequestMethod.GET)
    public SimpleResponse getToken() {
        StringBuffer sb =new StringBuffer("user/");
        sb.append(userService.currentUserId());
        return  SimpleResponse.ok(AliOssDao.generateToken(sb.toString()));
    }

    @ApiOperation(value = "得到该文件下所有直系子文件夹和文件", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/get_child", method = RequestMethod.POST)
    public SimpleResponse getChild(Long folderId,Long affairId) {
        if(folderId == null | affairId == null){
            return SimpleResponse.error("参数错误");
        }
        List<FolderForm> folders = fileService.getChildFolder(folderId,affairId);
        List<FileForm> files = fileService.getChildFile(folderId,affairId);
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
    public SimpleResponse addFolder(Long folderId,String name,Long operationRoleId,Long affairId,Long taskId) {
        if(folderId == null | name == null | operationRoleId == null){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.addFolder(folderId,name,operationRoleId,affairId,taskId);

        return SimpleResponse.ok(result);

    }

    @ApiOperation(value = "添加文件", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/add_file", method = RequestMethod.POST)
    public SimpleResponse addFile(AddFileForm form) {
//        if(form.getFileId() == null | form.getFileName() == null | form.getSize() == null | form.getUploader() == null | form.getFolderId() == null | form.getAffairId() == null){
//            return SimpleResponse.error("参数错误");
//        }
        boolean result = fileService.addFile(form);

        return SimpleResponse.ok(result);

    }

    @ApiOperation(value = "删除文件", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/remove_file", method = RequestMethod.POST)
    public SimpleResponse removeFile(Long id , Long affairId) {

        if(id == null | affairId == null){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.removeFile(id,affairId);


        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "删除文件夹", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/remove_folder", method = RequestMethod.POST)
    public SimpleResponse removeFolder(Long affairId , Long folderId , Long operationRoleId) {

        if(affairId == null | folderId == null){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.removeFolder(affairId,folderId);


        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "重命名文件夹", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/rename_folder", method = RequestMethod.POST)
    public SimpleResponse renameFolder(Long affairId , Long folderId , String name) {

        if(affairId == null | folderId == null | name == null){
            return SimpleResponse.error("参数错误");
        }
        boolean result = fileService.renameFolder(affairId,folderId,name);


        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "得到某个文件历史版本", response = SimpleResponse.class, notes = "")
    @RequestMapping(value = "/get_history", method = RequestMethod.POST)
    public SimpleResponse getHistoryFile(Long affairId , Long fileId ) {

        if(affairId == null | fileId == null){
            return SimpleResponse.error("参数错误");
        }
        List<FileForm> result = fileService.getHistoryFile(fileId,affairId);


        return SimpleResponse.ok(result);
    }




}
