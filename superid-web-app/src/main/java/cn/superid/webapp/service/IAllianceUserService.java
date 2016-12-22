package cn.superid.webapp.service;

import cn.superid.webapp.model.AllianceUserEntity;

/**
 * Created by njuTms on 16/12/15.
 */
public interface IAllianceUserService{
    public AllianceUserEntity addAllianceUser(long allianceId,long userId);
}
