package cn.superid.webapp.service.forms;

import java.util.List;

/**
 * Created by jizhenya on 16/9/28.
 * {"blocks":[{"data":{},"depth":0,"entityRanges":[],"inlineStyleRanges":[],"key":"1dpia","text":"CreateAnnouncement. ","type":"unstyled"}],"entityMap":{}}
 */
public class TotalBlock {

    private Object data;
    private int depth;
    private List<Object> entityRanges;
    private List<Object> inlineStyleRanges;
    private String key;
    private String text;
    private String type;

    public TotalBlock(Object data, int depth, List<Object> entityRanges, List<Object> inlineStyleRanges, String key, String text, String type) {
        this.data = data;
        this.depth = depth;
        this.entityRanges = entityRanges;
        this.inlineStyleRanges = inlineStyleRanges;
        this.key = key;
        this.text = text;
        this.type = type;
    }

    public TotalBlock(){}

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<Object> getEntityRanges() {
        return entityRanges;
    }

    public void setEntityRanges(List<Object> entityRanges) {
        this.entityRanges = entityRanges;
    }

    public List<Object> getInlineStyleRanges() {
        return inlineStyleRanges;
    }

    public void setInlineStyleRanges(List<Object> inlineStyleRanges) {
        this.inlineStyleRanges = inlineStyleRanges;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
