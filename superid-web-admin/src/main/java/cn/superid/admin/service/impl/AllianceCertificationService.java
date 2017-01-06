package cn.superid.admin.service.impl;

import cn.superid.admin.model.AdminEntity;
import cn.superid.admin.model.AllianceCertificationEntity;
import cn.superid.admin.model.AllianceEntity;
import cn.superid.admin.service.IAllianceCertificationService;
import cn.superid.jpa.redis.RedisUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by njuTms on 16/10/10.
 */
@Service
public class AllianceCertificationService implements IAllianceCertificationService {

    @Override
    public List<AllianceCertificationEntity> showUncheckedCertification() {
        return AllianceCertificationEntity.dao.eq("check_state",0).selectList("id","company_name","alliance_id");
    }

    @Override
    public AllianceCertificationEntity showCertificationInfo(long id,long allianceId) {
        return AllianceCertificationEntity.dao.findById(id,allianceId);
    }

    @Override
    public boolean agreeCertification(long id,long allianceId,String dealReason,String userName) {
        AllianceEntity.dao.id(allianceId).set("verified",0);
        return AllianceCertificationEntity.dao.id(id).partitionId(allianceId).set("check_reason",dealReason,"check_admin",userName,"check_state",1)>0;
    }

    @Override
    public boolean rejectCertification(long id,long allianceId,String dealReason,String userName) {
        return AllianceCertificationEntity.dao.id(id).partitionId(allianceId).set("check_reason",dealReason,"check_admin",userName,"check_state",2)>0;
    }
}
