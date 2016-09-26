package cn.superid.webapp.service.forms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public class OperationListForm {

    private List<Operation> insert ;
    private List<Operation> substitute;
    private List<Operation> delete;

    public OperationListForm(List<Operation> insert, List<Operation> substitute, List<Operation> delete) {
        this.insert = insert;
        this.substitute = substitute;
        this.delete = delete;
    }

    public List<Operation> getInsert() {
        return insert;
    }

    public void setInsert(List<Operation> insert) {
        this.insert = insert;
    }

    public List<Operation> getSubstitute() {
        return substitute;
    }

    public void setSubstitute(List<Operation> substitute) {
        this.substitute = substitute;
    }

    public List<Operation> getDelete() {
        return delete;
    }

    public void setDelete(List<Operation> delete) {
        this.delete = delete;
    }
}
