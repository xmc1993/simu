package cn.superid.webapp.service;

import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AllianceEntity;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAllianceService {

    String getPermissions(long alliance,long roleId);

    AllianceEntity createAlliance(AllianceCreateForm allianceCreateForm);

    boolean inSameAlliance(long userId1,long userId2);

    boolean validName(String name);


}
