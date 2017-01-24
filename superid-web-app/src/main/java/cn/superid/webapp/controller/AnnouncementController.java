package cn.superid.webapp.controller;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.VO.DraftDetailVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementIdVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementVO;
import cn.superid.webapp.controller.VO.SimpleDraftIdVO;
import cn.superid.webapp.controller.forms.AnnouncementListForm;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.forms.ContentState;
import com.alibaba.fastjson.JSON;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
@Controller
@RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    private IAnnouncementService announcementService;

    @ApiOperation(value = "根据事务得到公告id",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_id_by_affair", method = RequestMethod.GET)
    public SimpleResponse getIdByAffair(@RequestParam() Long allianceId ,@RequestParam() Long affairId ,@RequestParam() boolean isContainChild) {
        return SimpleResponse.ok(announcementService.getIdByAffair(affairId,allianceId,isContainChild));
    }

    @ApiOperation(value = "根据事务得到公告草稿id",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_draft_by_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_ANNOUNCEMENT)
    public SimpleResponse getDraftByAffair(@RequestParam() Long affairMemberId) {
        List<SimpleDraftIdVO> simpleDraftIdVOs = announcementService.getDraftByAffair(GlobalValue.currentAffairId(),GlobalValue.currentAllianceId(),GlobalValue.currentRoleId());
        if(simpleDraftIdVOs == null){
            simpleDraftIdVOs = new ArrayList<>();
        }
        return SimpleResponse.ok(simpleDraftIdVOs);
    }

    @ApiOperation(value = "根据草稿id得到草稿详情",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_draft_detail", method = RequestMethod.GET)
    public SimpleResponse getDraftDetail(@RequestParam() Long draftId) {
        DraftDetailVO result = announcementService.getDraftDetail(draftId);
        if(result == null){
            return SimpleResponse.error(null);
        }
        return SimpleResponse.ok(result);
    }


    @ApiOperation(value = "根据ids得到所有的公告概略",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_overview", method = RequestMethod.GET)
    public SimpleResponse getOverview(@RequestParam() String ids ,@RequestParam() Long allianceId ) {
        List<SimpleAnnouncementVO> result = announcementService.getOverview(ids,allianceId);
        if(result == null){
            return SimpleResponse.error(null);
        }
        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "查找公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SimpleResponse searchAnnouncement(@RequestParam() String content ,@RequestParam() Long affairId ,@RequestParam() Long allianceId ,@RequestParam() Boolean containChild) {
        List<SimpleAnnouncementIdVO> result = announcementService.searchAnnouncement(content,affairId,allianceId,containChild);
        if(result == null){
            return SimpleResponse.error(null);
        }
        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "得到公告所有版本",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_all_version", method = RequestMethod.GET)
    public SimpleResponse searchAnnouncement(@RequestParam() Long announcementId ,@RequestParam() Long allianceId) {
        return SimpleResponse.ok(announcementService.getAllVersion(announcementId,allianceId));
    }

    @ApiOperation(value = "查看详细公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_detail", method = RequestMethod.GET)
    public SimpleResponse getDetail(@RequestParam() Long announcementId ,@RequestParam() Long allianceId ){
        return SimpleResponse.ok(announcementService.getDetail(announcementId,allianceId));
    }


    @ApiOperation(value = "查看详细公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_details", method = RequestMethod.GET)
    public SimpleResponse getDetail(@RequestParam() Long announcementId , Integer offsetHead , Integer offsetTail , Integer version ,@RequestParam() Long allianceId ) {

        if(version == null){version = 0;}
        if(offsetTail == null){offsetTail = 0;}
        if(offsetHead == null){offsetHead = 0;}


        return SimpleResponse.ok(announcementService.getDetails(announcementId,offsetHead,offsetTail,version,allianceId));

    }

    @ApiOperation(value = "保存",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_ANNOUNCEMENT)
    public SimpleResponse save(@RequestParam() Long announcementId ,@RequestParam() String contentState ,@RequestParam() String title, @RequestParam() Long affairMemberId){
        try{
            ContentState content = JSON.parseObject(contentState,ContentState.class);
            boolean result = announcementService.save(content,announcementId,GlobalValue.currentAllianceId(),GlobalValue.currentRoleId(),title);
            if(result == true){
                return SimpleResponse.ok(announcementService.getDetail(announcementId,GlobalValue.currentAllianceId()));
            }else{
                return SimpleResponse.error(null);
            }

        }catch (Exception e){
            e.printStackTrace();
            return SimpleResponse.error(null);
        }

    }

    @ApiOperation(value = "修改公告公开性",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/modify_public_type", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_ANNOUNCEMENT)
    public SimpleResponse modifyPublicType(@RequestParam() Long announcementId , @RequestParam() Integer publicType , @RequestParam() Long affairMemberId){

        return SimpleResponse.ok(announcementService.modifyPublicType(announcementId,publicType,GlobalValue.currentAllianceId()));
    }

    @ApiOperation(value = "修改置顶",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/modify_stuck", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.EDIT_ANNOUNCEMENT)
    public SimpleResponse modifyStuck(@RequestParam() Long announcementId , @RequestParam() Integer isStuck , @RequestParam() Long affairMemberId){

        return SimpleResponse.ok(announcementService.modifyStuck(announcementId,isStuck,GlobalValue.currentAllianceId()));
    }

    @ApiOperation(value = "保存草稿",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/save_draft", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_ANNOUNCEMENT)
    public SimpleResponse saveDraft(@RequestParam() Long affairMemberId , Long draftId ,@RequestParam() String delta ,@RequestParam() Integer publicType ,@RequestParam() String title , Long taskId , String entityMap , int editMode){

        if(draftId == null){
            draftId = 0L ;
        }
        if(taskId == null){
            taskId = 0L ;
        }
        long result = announcementService.saveDraft(delta,draftId,GlobalValue.currentAllianceId(),GlobalValue.currentAffairId(),GlobalValue.currentRoleId(),publicType,title,taskId,entityMap,editMode);
        if(result == 0){
            return SimpleResponse.error("添加失败");
        }
        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "删除草稿",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/delete_draft", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.INVALID_ANNOUNCEMENT)
    public SimpleResponse deleteDraft(@RequestParam() Long draftId,@RequestParam() Long affairMemberId ){
        try{
            return SimpleResponse.ok(announcementService.deleteDraft(draftId,GlobalValue.currentAllianceId()));
        }catch (Exception e){
            e.printStackTrace();
            return SimpleResponse.error("删除失败");
        }

    }



    @ApiOperation(value = "创建新公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/create_announcement", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_ANNOUNCEMENT)
    public SimpleResponse createAnnouncement(@RequestParam() String title  , Long taskId ,@RequestParam() Long affairMemberId ,@RequestParam() Integer isTop ,@RequestParam() Integer publicType ,@RequestParam() String content){
        if(taskId == null){
            taskId = 0L ;
        }
        try{
            ContentState contentState = JSON.parseObject(content,ContentState.class);
            return SimpleResponse.ok(announcementService.createAnnouncement(title,GlobalValue.currentAffairId(),GlobalValue.currentAllianceId(),taskId,GlobalValue.currentRoleId(),isTop,publicType,contentState));
        }catch (Exception e){
            e.printStackTrace();
            return SimpleResponse.error("ContentState is invalid");
        }

    }

    @ApiOperation(value = "删除公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/delete_announcement", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.INVALID_ANNOUNCEMENT)
    public SimpleResponse deleteAnnouncement(@RequestParam() Long announcementId){
        return SimpleResponse.ok(announcementService.deleteAnnouncement(announcementId,GlobalValue.currentAllianceId(),GlobalValue.currentRoleId()));
    }
}
