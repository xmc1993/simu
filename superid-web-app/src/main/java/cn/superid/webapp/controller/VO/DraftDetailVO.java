package cn.superid.webapp.controller.VO;

import cn.superid.jpa.orm.ConditionalDao;

/**
 * Created by jizhenya on 16/12/16.
 */
public class DraftDetailVO {
    public final static ConditionalDao<DraftDetailVO> dao = new ConditionalDao<>(DraftDetailVO.class);

    private String content;
    private String title;
    private int publicType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicType() {
        return publicType;
    }

    public void setPublicType(int publicType) {
        this.publicType = publicType;
    }
}
