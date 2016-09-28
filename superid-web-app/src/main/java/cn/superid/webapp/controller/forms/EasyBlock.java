package cn.superid.webapp.controller.forms;

/**
 * Created by jizhenya on 16/9/28.
 */
public class EasyBlock {

    private String content;
    private String key;

    public EasyBlock(String content, String key) {
        this.content = content;
        this.key = key;
    }

    public EasyBlock(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
