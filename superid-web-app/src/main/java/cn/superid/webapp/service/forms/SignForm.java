package cn.superid.webapp.service.forms;

/**
 * Created by jizhenya on 16/8/24.
 */
public class SignForm {

    private int state;
    private String name;
    private Long time;

    public SignForm(int state, String name, Long time) {
        this.state = state;
        this.name = name;
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
