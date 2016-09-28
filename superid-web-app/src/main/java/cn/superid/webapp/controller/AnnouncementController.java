package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.NotLogin;
import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.model.AnnouncementHistoryEntity;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.forms.Block;
import com.alibaba.fastjson.JSON;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @ApiOperation(value = "查看详细公告",response = String.class, notes = "拥有权限")
    @RequestMapping(value = "/getDetail/{announcementId}", method = RequestMethod.POST)
    @RequiredPermissions()
    @NotLogin
    public SimpleResponse getDetail(@PathVariable Long announcementId , Integer offsetHead , Integer offsetTail , Long affairId , Integer version) {

        if(announcementId == null | affairId == null){
            return SimpleResponse.error("参数不正确");
        }
        if(offsetTail == null){offsetTail = 0;}
        if(offsetHead == null){offsetHead = 0;}

        List<EditDistanceForm> operations = new ArrayList<>();

        AnnouncementEntity announcement = AnnouncementEntity.dao.findById(announcementId,affairId);
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

        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("baseRawDraftContent", content);
        rsMap.put("history",operations);
        return SimpleResponse.ok(rsMap);

    }
}
