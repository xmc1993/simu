package cn.superid.webapp.controller.forms;

/**
 * Created by jizhenya on 16/9/26.
 */
public class ReplaceForm {

    private int position;
    private String newContent;

    public ReplaceForm(int position, String newContent) {
        this.position = position;
        this.newContent = newContent;
    }

    public ReplaceForm(){}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}
