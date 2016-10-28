package cn.superid.admin.service;

import cn.superid.admin.model.AllianceCertificationEntity;

import java.util.List;

/**
 * Created by njuTms on 16/10/10.
 */
public interface IAllianceCertificationService {
    public List<AllianceCertificationEntity> showUncheckedCertification();

    public AllianceCertificationEntity showCertificationInfo(long id,long allianceId);

    public boolean agreeCertification(long id,long allianceId,String dealReason,String userName);

    public boolean rejectCertification(long id,long allianceId,String dealReason,String userName);
}
