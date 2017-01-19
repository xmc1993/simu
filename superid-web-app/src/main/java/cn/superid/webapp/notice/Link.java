package cn.superid.webapp.notice;

/**
 * Created by jessiechen on 18/01/17.
 */
public class Link {
    private int type;
    private int begin;
    private int end;
    private long id;

    public Link() {
    }

    public Link(int type, int begin, int end, long id) {
        this.type = type;
        this.begin = begin;
        this.end = end;
        this.id = id;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
