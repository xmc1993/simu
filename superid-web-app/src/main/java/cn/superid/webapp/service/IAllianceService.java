package cn.superid.webapp.service;

import cn.superid.webapp.forms.AllianceCreateForm;
import cn.superid.webapp.model.AllianceEntity;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAllianceService {

    public String getPermissions(long alliance,long roleId);

    public AllianceEntity createAlliance(AllianceCreateForm allianceCreateForm);

}
