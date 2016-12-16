package cn.superid.webapp.controller.forms;

import cn.superid.webapp.service.forms.TotalBlock;

import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public class InsertForm {

    private int position;
    private List<TotalBlock> content;


    public InsertForm(int position, List<TotalBlock> content) {
        this.position = position;
        this.content = content;
    }

    public InsertForm(){}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<TotalBlock> getContent() {
        return content;
    }

    public void setContent(List<TotalBlock> content) {
        this.content = content;
    }
}
