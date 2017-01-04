package cn.superid.webapp.dao;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.SimpleAnnouncementIdVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementVO;
import cn.superid.webapp.controller.VO.SimpleDraftIdVO;
import cn.superid.webapp.dao.impl.IAnnouncementDao;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AnnouncementEntity;
import org.springframework.stereotype.Component;

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
            StringBuilder sql = new StringBuilder(SQLDao.GET_ANNOUNCEMENT_ID);
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
}