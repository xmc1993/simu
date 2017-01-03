package cn.superid.webapp.service;

import cn.superid.webapp.controller.VO.*;
import cn.superid.webapp.controller.forms.AnnouncementForm;
import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.model.AnnouncementEntity;
import cn.superid.webapp.service.forms.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by jizhenya on 16/9/26.
 */
public interface IAnnouncementService {

    //公告接口第一部分:比对公告内容等方法

    /**
     * 比较两个段落列表
     * @param present
     * @param history
     * @return
     */
    public EditDistanceForm compareTwoBlocks(List<TotalBlock> present , List<TotalBlock> history );

    /**
     * 比较两篇文章
     * @param present
     * @param history
     * @return
     */
    public EditDistanceForm compareTwoPapers(ContentState present , ContentState history);

    /**
     * 根据一篇文章和一个delta生成新的文章
     * @param content
     * @param operations
     * @return
     */
    public String caulatePaper(String content , String operations);

    /**
     * 从文章得到段落列表
     * @param content
     * @return
     */
    public List<Block> getBlock(ContentState content);

    //公告接口第二部分:公告的保存,发布,删除

    /**
     * 保存公告
     * @param contentState
     * @param announcementId
     * @param allianceId
     * @param roleId
     * @return
     */
    public boolean save(ContentState contentState , long announcementId , long allianceId , long roleId);

    /**
     * 创建公告
     * @param title
     * @param affairId
     * @param allianceId
     * @param taskId
     * @param roleId
     * @param isTop
     * @param publicType
     * @param content
     * @return
     */
    public boolean createAnnouncement(String title , long affairId , long allianceId, long taskId , long roleId , int isTop , int publicType , ContentState content);

    /**
     * 删除公告
     * @param announcementId
     * @param allianceId
     * @param roleId
     * @return
     */
    public boolean deleteAnnouncement(long announcementId , long allianceId , long roleId);

    /**
     * 得到公告列表id索引
     * @param affairId
     * @param allianceId
     * @param isContainChild
     * @return
     */
    public List<SimpleAnnouncementIdVO> getIdByAffair(long affairId , long allianceId , boolean isContainChild);

    /**
     * 得到概略公告列表
     * @param ids
     * @param allianceId
     * @return
     */
    public List<SimpleAnnouncementVO> getOverview(String ids , long allianceId);

    /**
     * 得到公告详情(阉割版)
     * @param announcementId
     * @param allianceId
     * @return
     */
    public AnnouncementEntity getDetail(long announcementId , long allianceId);

    /**
     * 得到公告详情(高配版)
     * @param announcementId
     * @param offsetHead
     * @param offsetTail
     * @param version
     * @param allianceId
     * @return
     */
    public Map<String,Object> getDetails(long announcementId, int offsetHead, int offsetTail, int version, long allianceId);

    /**
     * 搜索公告
     * @param content
     * @param affairId
     * @param allianceId
     * @return
     */
    public List<SimpleAnnouncementIdVO> searchAnnouncement(String content, Long affairId, Long allianceId, boolean containChild);

    //公告接口第三部分:公告草稿部分

    /**
     * 得到一个角色在一个事务中的草稿
     * @param affairId
     * @param allianceId
     * @param roleId
     * @return
     */
    public List<SimpleDraftIdVO> getDraftByAffair(long affairId , long allianceId , long roleId);

    /**
     * 得到草稿详情
     * @param draftId
     * @return
     */
    public DraftDetailVO getDraftDetail(long draftId);

    /**
     * 删除草稿
     * @param draftId
     * @param allianceId
     * @return
     */
    public boolean deleteDraft(long draftId , long allianceId);

    /**
     * 自动保存草稿接口
     * @param delta
     * @param draftId
     * @param allianceId
     * @param affairId
     * @param roleId
     * @param publicType
     * @param title
     * @param taskId
     * @param entityMap
     * @param editMode
     * @return
     */
    public long saveDraft(String delta , long draftId , long allianceId , long affairId , long roleId , int publicType , String title , long taskId , String entityMap , int editMode);


    //公告接口第四部分:历史节点公告还原部分

    /**
     * 得到历史某个时间点上的公告列表
     * @param affairId
     * @param allianceId
     * @param count
     * @param time
     * @return
     */
    public List<SimpleAnnouncementHistoryVO> getHistoryOverview(long affairId , long allianceId , int count, Timestamp time);

    /**
     * 回退到某个版本
     * @param announcementId
     * @param version
     * @param allianceId
     * @return
     */
    public SimpleAnnouncementVO getHistoryVersion(long announcementId, int version, long allianceId);
}
