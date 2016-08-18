package cn.superid.webapp.service.impl;

import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AllianceEntity;
import cn.superid.webapp.service.IAllianceService;
import org.springframework.stereotype.Service;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AllianceService  implements IAllianceService{
    @Override
    public String getPermissions(long alliance, long roleId) {
        return null;
    }

    @Override
    public AllianceEntity createAlliance(AllianceCreateForm allianceCreateForm) {
//        throw new JdbcRuntimeException("error");
        return new AllianceEntity();
    }
}
