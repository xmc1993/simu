package cn.superid.webapp.forms;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public class IdNameNode {
    private int id;
    private String name;
    private List<IdNameNode> childs =new ArrayList<>(20);

    public IdNameNode(){

    }

    public IdNameNode(int id,String name){
        this.id=id;
        this.name =name;
    }
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IdNameNode> getChilds() {
        return childs;
    }

    public void setChilds(List<IdNameNode> childs) {
        this.childs = childs;
    }
}
