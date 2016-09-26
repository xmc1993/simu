package cn.superid.webapp.controller.forms;

import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public class EditDistanceForm {
    private List<Integer> delete;
    private List<InsertForm> insert;
    private List<ReplaceForm> replace;

    public EditDistanceForm(List<Integer> delete, List<InsertForm> insert, List<ReplaceForm> replace) {
        this.delete = delete;
        this.insert = insert;
        this.replace = replace;
    }

    public List<Integer> getDelete() {
        return delete;
    }

    public void setDelete(List<Integer> delete) {
        this.delete = delete;
    }

    public List<InsertForm> getInsert() {
        return insert;
    }

    public void setInsert(List<InsertForm> insert) {
        this.insert = insert;
    }

    public List<ReplaceForm> getReplace() {
        return replace;
    }

    public void setReplace(List<ReplaceForm> replace) {
        this.replace = replace;
    }
}
