package cn.superid.webapp.dao.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.SimpleAnnouncementHistoryVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementIdVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementVO;
import cn.superid.webapp.controller.VO.SimpleDraftIdVO;
import cn.superid.webapp.dao.IAnnouncementDao;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AnnouncementEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jizhenya on 17/1/4.
 */
@Component
public class AnnouncementDao implements IAnnouncementDao{

    @Override
    public List<SimpleAnnouncementIdVO> getIdByAffair(long affairId, long allianceId, boolean isContainChild) {
        List<SimpleAnnouncementIdVO> simpleAnnouncementIdVOList = null;
        if(isContainChild == false){
            StringBuilder sql = new StringBuilder("select id as announcementId,modify_time,affair_id,is_top from announcement  where alliance_id = ? and affair_id = ? and state = 0 order by modify_time desc ");
            ParameterBindings p = new ParameterBindings();
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affairId);
            simpleAnnouncementIdVOList = SimpleAnnouncementIdVO.dao.findListByNativeSql(sql.toString(),p);
        }else{
            AffairEntity affair = AffairEntity.dao.findById(affairId,allianceId);
            if(affair == null){
                return null;
            }
            StringBuilder sql = new StringBuilder("select a.* from (select id as announcementId,modify_time,affair_id,is_top from announcement a where alliance_id = ? and state = 0 ) a " +
                    "join (select id from affair where alliance_id = ? and path like ? ) b on a.affair_id = b.id ");
            ParameterBindings p = new ParameterBindings();
            p.addIndexBinding(allianceId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affair.getPath()+"%");

            simpleAnnouncementIdVOList = SimpleAnnouncementIdVO.dao.findListByNativeSql(sql.toString(),p);
        }
        return simpleAnnouncementIdVOList;
    }

    @Override
    public List<SimpleAnnouncementVO> getOverview(String ids, long allianceId) {
        String[] idList = ids.split(",");

        StringBuilder sql = new StringBuilder("select a.* , b.name as affairName from (select title , id , affair_id , thumb_content as content, modifier_id as creatorId, modifier_user_id as creatorUserId from announcement where id in ( 0 ");
        ParameterBindings p = new ParameterBindings();

        for(String id : idList){
            if(id.matches("[0-9]+")){
                sql.append(","+id);
            }
        }
        sql.append(" ) ) a join affair b on a.affair_id = b.id ");

        List<SimpleAnnouncementVO> result = AnnouncementEntity.getSession().findListByNativeSql(SimpleAnnouncementVO.class,sql.toString(),p);

        return result;
    }

    @Override
    public List<SimpleDraftIdVO> getDraftByAffair(long affairId, long allianceId, long roleId) {
        StringBuilder sql = new StringBuilder("select id ,modify_time,title from announcement_draft  where alliance_id = ? and affair_id = ? and creator_id = ? and state = 0 order by modify_time desc ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        p.addIndexBinding(roleId);
        return SimpleDraftIdVO.dao.findListByNativeSql(sql.toString(),p);
    }

    @Override
    public List<SimpleAnnouncementIdVO> searchAnnouncement(String content, Long affairId, Long allianceId, boolean containChild) {
        StringBuilder sql = null;
        ParameterBindings p = new ParameterBindings();
        if(containChild == true){
            AffairEntity affairEntity = AffairEntity.dao.findById(affairId,allianceId);
            if(affairEntity == null){
                return null;
            }
            sql = new StringBuilder("select a.id as announcementId , a.modify_time , a.affair_id from (select id,modifier_id,modify_time,title,affair_id from announcement where alliance_id = ? ) a " +
                    "join ( select id , user_id , title from role where alliance_id = ? ) b " +
                    "join ( select id , username from user ) c " +
                    "join (select id from affair where alliance_id = ? and path like ? ) d "+
                    " on a.modifier_id = b.id and b.user_id = c.id and a.affair_id = d.id where a.title like ? or b.title = ? or c.username = ? ");
            p.addIndexBinding(allianceId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affairEntity.getPath()+"%");
            p.addIndexBinding("%"+content+"%");
            p.addIndexBinding(content);
            p.addIndexBinding(content);
        }else{
            sql = new StringBuilder("select a.id as announcementId , a.modify_time , a.affair_id from (select id,modifier_id,modify_time,title,affair_id from announcement where alliance_id = ? and affair_id = ? ) a " +
                    "join ( select id , user_id , title from role where alliance_id = ? ) b " +
                    "join ( select id , username from user ) c " +
                    "on a.modifier_id = b.id and b.user_id = c.id where a.title like ? or b.title = ? or c.username = ? ");
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affairId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding("%"+content+"%");
            p.addIndexBinding(content);
            p.addIndexBinding(content);
        }


        return AnnouncementEntity.getSession().findListByNativeSql(SimpleAnnouncementIdVO.class,sql.toString(),p);
    }

    @Override
    public List<SimpleAnnouncementHistoryVO> getAnnouncementHistoryList(long affairId, long allianceId, int count, Timestamp time) {
        StringBuilder sql = new StringBuilder("select a.*,b.name as affairName from (" +
                "select announcement_id as id,max(version) as version,title,affair_id,thumb_content as content,creator_id,creator_user_id from announcement_history where alliance_id = ? and affair_id = ?  create_time <= ? and id not in (" +
                "select id from announcement_history where alliance_id = ? and affair_id = ?  modify_time <= ? and state = 1 ) order by announcement_id ) a " +
                "join (select name , id from affair where alliance_id = ? and id = ? ) b " +
                "on a.affair_id = b.id");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        p.addIndexBinding(time);
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        p.addIndexBinding(time);
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        return AnnouncementEntity.getSession().findListByNativeSql(SimpleAnnouncementHistoryVO.class,sql.toString(),p);
    }
}