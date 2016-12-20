package cn.superid.webapp.controller.forms;

import cn.superid.webapp.service.forms.TotalBlock;

import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public class ReplaceForm {

    private int position;
    private TotalBlock content;

    public ReplaceForm(int position, TotalBlock content) {
        this.position = position;
        this.content = content;
    }

    public ReplaceForm(){}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public TotalBlock getContent() {
        return content;
    }

    public void setContent(TotalBlock content) {
        this.content = content;
    }
}
