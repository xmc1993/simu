package cn.superid.webapp.service.forms;

/**
 * Created by jizhenya on 16/9/26.
 */
public class Operation {

    private String type;
    private int location;
    private String content;

    public Operation(String type, int location, String content) {
        this.type = type;
        this.location = location;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
