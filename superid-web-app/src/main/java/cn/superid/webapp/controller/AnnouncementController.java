package cn.superid.webapp.controller;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.VO.DraftDetailVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementIdVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementVO;
import cn.superid.webapp.controller.VO.SimpleDraftIdVO;
import cn.superid.webapp.controller.forms.AnnouncementForm;
import cn.superid.webapp.controller.forms.AnnouncementListForm;
import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.model.AnnouncementHistoryEntity;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.GlobalValue;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.forms.Block;
import cn.superid.webapp.service.forms.ContentState;
import com.alibaba.fastjson.JSON;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public SimpleResponse getIdByAffair(Long allianceId , Long affairId , boolean isContainChild) {
        return SimpleResponse.ok(announcementService.getIdByAffair(affairId,allianceId,isContainChild));
    }

    @ApiOperation(value = "根据事务得到公告草稿id",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_draft_by_affair", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_ANNOUNCEMENT)
    public SimpleResponse getDraftByAffair(Long affairMemberId) {
        List<SimpleDraftIdVO> simpleDraftIdVOs = announcementService.getDraftByAffair(GlobalValue.currentAffairId(),GlobalValue.currentAllianceId(),GlobalValue.currentRoleId());
        if(simpleDraftIdVOs == null){
            simpleDraftIdVOs = new ArrayList<>();
        }
        return SimpleResponse.ok(simpleDraftIdVOs);
    }

    @ApiOperation(value = "根据草稿id得到草稿详情",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_draft_detail", method = RequestMethod.GET)
    public SimpleResponse getDraftDetail(Long draftId) {
        if(draftId == null ){
            return SimpleResponse.error("参数不能为空");
        }
        DraftDetailVO result = announcementService.getDraftDetail(draftId);
        if(result == null){
            return SimpleResponse.error("未得到结果");
        }
        return SimpleResponse.ok(result);
    }


    @ApiOperation(value = "根据ids得到所有的公告概略",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/get_overview", method = RequestMethod.GET)
    public SimpleResponse getOverview(String ids , Long allianceId ) {
        if(ids == null | allianceId == null ){
            return SimpleResponse.error("参数不能为空");
        }
        List<SimpleAnnouncementVO> result = announcementService.getOverview(ids,allianceId);
        if(result == null){
            return SimpleResponse.error("未搜到结果");
        }
        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "查找公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SimpleResponse searchAnnouncement(String content , Long affairId , Long allianceId ) {
        if(content == null | affairId == null | allianceId == null ){
            return SimpleResponse.error("参数不能为空");
        }
        List<SimpleAnnouncementIdVO> result = announcementService.searchAnnouncement(content,affairId,allianceId);

        if(result == null){
            return SimpleResponse.error("未搜到结果");
        }
        return SimpleResponse.ok(result);
    }



    @ApiOperation(value = "查看详细公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    public SimpleResponse getDetail( Long announcementId , Integer offsetHead , Integer offsetTail , Integer version , Long allianceId , Long affairId) {

        if(allianceId == null | affairId == null | announcementId == null ){
            return SimpleResponse.error("参数错误");
        }

        if(announcementId == null){
            return SimpleResponse.error("参数不正确");
        }
        if(offsetTail == null){offsetTail = 0;}
        if(offsetHead == null){offsetHead = 0;}

        List<EditDistanceForm> operations = new ArrayList<>();

        AnnouncementEntity announcement = AnnouncementEntity.dao.findById(announcementId,allianceId);
        if(announcement == null){
            return SimpleResponse.error("id不正确");
        }
        String present = announcement.getContent();
        String content = "";
        //以下代码用于得到首要的版本的contentState
        if(version == null | version < 1 | version > announcement.getVersion()){ version = announcement.getVersion();}
        if(version == announcement.getVersion()){
            content = present;
        }else{
            List<AnnouncementHistoryEntity> hs = AnnouncementHistoryEntity.dao.partitionId(announcementId).gt("version",version).desc("version").selectList();
            content = announcementService.caulatePaper(present,announcement.getDecrement());
            for(AnnouncementHistoryEntity h : hs){
                content = announcementService.caulatePaper(content,h.getDecrement());
            }
        }

        //处理取前几位
        int upper = version+offsetHead;
        int over = upper-announcement.getVersion();

        if(over > 0){
            //表示请求的越过上限,则在开头填上相同位数的null
            for(int i = 0 ; i < over ; i++){
                operations.add(null);
            }

        }
        List<AnnouncementHistoryEntity> histories = AnnouncementHistoryEntity.dao.partitionId(announcementId).gt("version",version-1).asc("version").selectList();
        for(AnnouncementHistoryEntity a : histories){
            EditDistanceForm e = JSON.parseObject(a.getIncrement(),EditDistanceForm.class);
            operations.add(e);
        }

        //处理取后几位
        if(version == announcement.getVersion() & offsetTail > 0){
            EditDistanceForm e = JSON.parseObject(announcement.getDecrement(),EditDistanceForm.class);
            operations.add(e);
        }
        int lower = version-offsetTail;
        List<AnnouncementHistoryEntity> lowHistories = AnnouncementHistoryEntity.dao.partitionId(announcementId).lt("version",version+1).gt("version",lower-1).desc("version").selectList();
        for(AnnouncementHistoryEntity a : lowHistories){
            if(a.getDecrement().equals("0")){
                break;
            }
            EditDistanceForm e = JSON.parseObject(a.getDecrement(),EditDistanceForm.class);
            operations.add(e);
        }
        if(lower < 1){
            for(int i = lower ; i <= 0 ; i++){
                operations.add(null);
            }
        }
        AnnouncementForm result = new AnnouncementForm();
        result.setId(announcement.getId());
        result.setContent(content);
        result.setCreateTime(announcement.getCreateTime());
        result.setCreatorId(announcement.getCreatorId());
        result.setState(announcement.getState());
        //组织返回结果
        if(version == announcement.getVersion()){

            result.setTitle(announcement.getTitle());
            result.setModifierId(announcement.getModifierId());
            result.setIsTop(announcement.getIsTop());
            result.setPublicType(announcement.getPublicType());
            result.setModifyTime(announcement.getModifyTime());
        }else{
            AnnouncementHistoryEntity h = AnnouncementHistoryEntity.dao.partitionId(announcementId).eq("version",version).selectOne();
            if(h != null){
                result.setTitle(h.getTitle());
                result.setModifierId(h.getModifierId());
                result.setIsTop(-1);
                result.setPublicType(-1);
                result.setModifyTime(h.getCreateTime());
            }
        }

        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("announcement", result);
        rsMap.put("history",operations);
        return SimpleResponse.ok(rsMap);

    }

    @ApiOperation(value = "保存",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse save(Long announcementId , String contentState){

        if(announcementId == null ){
            return SimpleResponse.error("参数不正确");
        }
        try{
            ContentState content = JSON.parseObject(contentState,ContentState.class);
            boolean result = announcementService.save(content,announcementId,GlobalValue.currentAllianceId(),GlobalValue.currentRoleId());
            return SimpleResponse.ok(result);
        }catch (Exception e){
            return SimpleResponse.error("ContentState is invaild");
        }
    }

    @ApiOperation(value = "保存草稿",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/save_draft", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_ANNOUNCEMENT)
    public SimpleResponse saveDraft(Long affairMemberId , Long draftId , String delta , Integer publicType , String title , Long taskId , String entityMap , int editMode){

        if(delta == null | publicType == null | title == null) {
            return SimpleResponse.error("参数不正确");
        }
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



    @ApiOperation(value = "创建新公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/create_announcement", method = RequestMethod.POST)
    @RequiredPermissions(affair = AffairPermissions.ADD_ANNOUNCEMENT)
    public SimpleResponse createAnnouncement(String title  , Long taskId , Long affairMemberId , Integer isTop , Integer publicType ,String content){
        if(taskId == null){
            taskId = 0L ;
        }
        try{
            ContentState contentState = JSON.parseObject(content,ContentState.class);
            return SimpleResponse.ok(announcementService.createAnnouncement(title,GlobalValue.currentAffairId(),GlobalValue.currentAllianceId(),taskId,GlobalValue.currentRoleId(),isTop,publicType,contentState));
        }catch (Exception e){
            e.printStackTrace();
            return SimpleResponse.error("ContentState is invaild");
        }

    }

    @ApiOperation(value = "删除公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/delete_announcement", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse deleteAnnouncement(Long announcementId){
        if(announcementId == null ){
            return SimpleResponse.error("参数不正确");
        }
        return SimpleResponse.ok(announcementService.deleteAnnouncement(announcementId,GlobalValue.currentAllianceId()));
    }

    @ApiOperation(value = "查看事务底下公告列表(不含task)",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/announcement_list", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getAnnouncementList(){
        List<AnnouncementEntity> announcementEntities = AnnouncementEntity.dao.partitionId(GlobalValue.currentAllianceId()).eq("affair_id",GlobalValue.currentAffairId()).eq("task_id",0).state(1).selectList();
        List<AnnouncementListForm> result = transformEntityToForm(announcementEntities);

        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "查看事务底下公告列表(含task)",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/announcement_list_contain_task", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getAnnouncementListContainTask(){
        List<AnnouncementEntity> announcementEntities = AnnouncementEntity.dao.partitionId(GlobalValue.currentAllianceId()).eq("affair_id",GlobalValue.currentAffairId()).state(1).selectList();
        List<AnnouncementListForm> result = transformEntityToForm(announcementEntities);
        return SimpleResponse.ok(result);
    }

    @ApiOperation(value = "查看事务及其子事务底下公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/announcement_list_contain_child", method = RequestMethod.POST)
    @RequiredPermissions()
    public SimpleResponse getAnnouncementListContainChild(){
        AffairEntity affair = AffairEntity.dao.findById(GlobalValue.currentAffairId(),GlobalValue.currentAllianceId());
        StringBuilder sql = new StringBuilder("select a.* from announcement a join affair b where a.affair_id = b.id and b.path like ? ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(affair.getPath()+"%");
        List<AnnouncementEntity> announcementEntities = AnnouncementEntity.dao.getSession().findList(AnnouncementEntity.class,sql.toString(),p);
        List<AnnouncementListForm> result = transformEntityToForm(announcementEntities);
        return SimpleResponse.ok(result);
    }

    private List<AnnouncementListForm> transformEntityToForm(List<AnnouncementEntity> announcementEntities){
        List<AnnouncementListForm> result = new ArrayList<>();
        if(announcementEntities != null){
            for(AnnouncementEntity a : announcementEntities){
                AnnouncementListForm announcementForm = new AnnouncementListForm();
                announcementForm.setId(a.getId());
                announcementForm.setModifyTime(a.getModifyTime());
                announcementForm.setPublicType(a.getPublicType());
                announcementForm.setModifierId(a.getModifierId());
                announcementForm.setIsTop(a.getIsTop());
                announcementForm.setState(a.getState());
                announcementForm.setThumbContent(a.getThumbContent());
                announcementForm.setVersion(a.getVersion());
                announcementForm.setCreatorId(a.getCreatorId());
                announcementForm.setCreateTime(a.getCreateTime());
                announcementForm.setTitle(a.getTitle());
                result.add(announcementForm);
            }
        }
        return result;
    }






}
