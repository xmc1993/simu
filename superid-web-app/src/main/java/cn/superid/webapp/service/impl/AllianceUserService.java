package cn.superid.webapp.service.impl;

import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.AllianceUserEntity;
import cn.superid.webapp.service.IAllianceUserService;
import org.springframework.stereotype.Service;

/**
 * Created by njuTms on 16/12/21.
 */
@Service
public class AllianceUserService implements IAllianceUserService {
    @Override
    public AllianceUserEntity addAllianceUser(long allianceId, long userId) {
        AllianceUserEntity allianceUserEntity = new AllianceUserEntity();
        allianceUserEntity.setAllianceId(allianceId);
        allianceUserEntity.setUserId(userId);
        allianceUserEntity.setState(ValidState.Valid);
        allianceUserEntity.save();
        return allianceUserEntity;
    }
}
