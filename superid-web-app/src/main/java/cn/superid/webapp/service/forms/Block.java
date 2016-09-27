package cn.superid.webapp.service.forms;

/**
 * Created by jizhenya on 16/9/26.
 */
public class Block {

    private String key;
    private String content;
    private int location;//这个变量是它在老文章中的位置
    private int newlocation;//这个变量是他在新文章中的位置

    public Block(String key, String content, int location) {
        this.key = key;
        this.content = content;
        this.location = location;
    }

    public Block(String key, String content, int location, int newlocation) {
        this.key = key;
        this.content = content;
        this.location = location;
        this.newlocation = newlocation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getNewlocation() {
        return newlocation;
    }

    public void setNewlocation(int newlocation) {
        this.newlocation = newlocation;
    }
}
