package cn.superid.webapp.service.impl;

import cn.superid.jpa.exceptions.JdbcRuntimeException;
import cn.superid.webapp.enums.IntBoolean;
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
        if(allianceCreateForm.getIsPersonal()== IntBoolean.TRUE){
            AllianceEntity allianceEntity = new AllianceEntity();
            allianceEntity.setName(allianceCreateForm.getName());
//            allianceEntity.set
        }

//        throw new JdbcRuntimeException("error");
        return new AllianceEntity();
    }

    @Override
    public boolean inSameAlliance(long userId1, long userId2) {
        return false;
    }

    @Override
    public boolean validName(String name) {
        return AllianceEntity.dao.eq("name",name).exists();
    }
}
