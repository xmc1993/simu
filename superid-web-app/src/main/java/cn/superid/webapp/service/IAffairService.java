package cn.superid.webapp.service;

import cn.superid.webapp.annotation.RequiredPermissions;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.security.IGetPermissions;

/**
 * Created by zp on 2016/7/26.
 */
public interface IAffairService  {

    public String getPermissions(long affairId,long roleId);

}
