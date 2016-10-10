package cn.superid.webapp.service;

import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.service.forms.Block;
import cn.superid.webapp.service.forms.ContentState;
import cn.superid.webapp.service.forms.Operation;
import cn.superid.webapp.service.forms.OperationListForm;

import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public interface IAnnouncementService {

    public EditDistanceForm compareTwoBlocks(List<Block> present , List<Block> history );

    public EditDistanceForm compareTwoPapers(ContentState present , ContentState history);

    public List<Block> paperToBlockList(String content);

    public String caulatePaper(String content , String operations);

    public List<Block> getBlock(ContentState content);

    public boolean save(ContentState contentState , long announcementId , long allianceId , long roleId);

    public boolean createAnnouncement(String title , long affairId , long allianceId, long taskId , long roleId , int isTop , int publicType , String thumb , ContentState content);

    public boolean deleteAnnouncement(long announcementId , long allianceId);
}
