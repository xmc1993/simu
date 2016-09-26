package cn.superid.webapp.controller;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.forms.SimpleResponse;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.forms.Block;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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
    public SimpleResponse getDetail(@PathVariable Long announcementId , Integer offsetTail , Long affairId) {
        if(announcementId == null | affairId == null){
            return SimpleResponse.error("参数不正确");
        }
        if(offsetTail == null){offsetTail = 0;}

        AnnouncementEntity announcement = AnnouncementEntity.dao.findById(announcementId,affairId);
        if(announcement == null){
            return SimpleResponse.error("id不正确");
        }
        String present = announcement.getContent();
        List<Block> pbs = announcementService.paperToBlockList(present);






        return SimpleResponse.ok("yep");

    }


}
