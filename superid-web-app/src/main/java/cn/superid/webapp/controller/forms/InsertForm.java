package cn.superid.webapp.controller.forms;

import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public class InsertForm {

    private int position;
    private List<String> content;

    public InsertForm(int position, List<String> content) {
        this.position = position;
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
