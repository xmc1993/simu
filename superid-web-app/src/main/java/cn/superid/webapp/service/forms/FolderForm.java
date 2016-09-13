package cn.superid.webapp.service.forms;

/**
 * Created by jizhenya on 16/9/12.
 */
public class FolderForm {

    private String name;
    private Long id;
    private int total;

    public FolderForm(String name, Long id,int total) {
        this.name = name;
        this.id = id;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
