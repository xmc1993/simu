package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.DraftDetailVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementIdVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementVO;
import cn.superid.webapp.controller.VO.SimpleDraftIdVO;
import cn.superid.webapp.controller.forms.AnnouncementForm;
import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.service.forms.*;

import java.util.List;
import java.util.Map;

/**
 * Created by jizhenya on 16/9/26.
 */
public interface IAnnouncementService {

    //公告接口第一部分:比对公告内容等方法

    public EditDistanceForm compareTwoBlocks(List<TotalBlock> present , List<TotalBlock> history );

    public EditDistanceForm compareTwoPapers(ContentState present , ContentState history);

    public List<Block> paperToBlockList(String content);

    public String caulatePaper(String content , String operations);

    public List<Block> getBlock(ContentState content);

    //公告接口第二部分:公告的保存,发布,删除

    public boolean save(ContentState contentState , long announcementId , long allianceId , long roleId);

    public boolean createAnnouncement(String title , long affairId , long allianceId, long taskId , long roleId , int isTop , int publicType , ContentState content);

    public boolean deleteAnnouncement(long announcementId , long allianceId , long roleId);

    public List<SimpleAnnouncementIdVO> getIdByAffair(long affairId , long allianceId , boolean isContainChild);

    public List<SimpleAnnouncementVO> getOverview(String ids , long allianceId);

    //阉割版
    public AnnouncementEntity getDetail(long announcementId , long allianceId);

    //高配版
    public Map<String,Object> getDetails(long announcementId, int offsetHead, int offsetTail, int version, long allianceId);

    public List<SimpleAnnouncementIdVO> searchAnnouncement(String content, Long affairId, Long allianceId);

    //公告接口第三部分:公告草稿部分

    public List<SimpleDraftIdVO> getDraftByAffair(long affairId , long allianceId , long roleId);

    public DraftDetailVO getDraftDetail(long draftId);

    public boolean deleteDraft(long draftId , long allianceId);

    public long saveDraft(String delta , long draftId , long allianceId , long affairId , long roleId , int publicType , String title , long taskId , String entityMap , int editMode);


    //公告接口第四部分:历史节点公告还原部分
    public List<SimpleAnnouncementVO> getHistoryOverview(long affairId , long allianceId , int count);
}
