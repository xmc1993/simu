package cn.superid.webapp.model;

import cn.superid.jpa.orm.Dao;
import cn.superid.jpa.orm.ExecutableModel;

/**
 * Created by zp on 2016/8/9.
 */
public class AllianceEntity extends ExecutableModel {
    public Dao<AllianceEntity> dao = new Dao<>(this.getClass());
}
