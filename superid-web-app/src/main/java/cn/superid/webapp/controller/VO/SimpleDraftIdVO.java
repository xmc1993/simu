package cn.superid.webapp.controller.VO;

import cn.superid.jpa.orm.ConditionalDao;

import java.sql.Timestamp;

/**
 * Created by jizhenya on 16/12/16.
 */
public class SimpleDraftIdVO {

    public final static ConditionalDao<SimpleDraftIdVO> dao = new ConditionalDao<>(SimpleDraftIdVO.class);

    private long id;
    private Timestamp modifyTime;
    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
