package cn.superid.webapp.service.forms;

import clojure.lang.Obj;
import com.wordnik.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/9/28.
 */
@ApiModel
public class ContentState {

    private Object entityMap;
    private List<TotalBlock> blocks = new ArrayList<>();

    public Object getEntityMap() {
        return entityMap;
    }

    public void setEntityMap(Object entityMap) {
        this.entityMap = entityMap;
    }

    public List<TotalBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<TotalBlock> blocks) {
        this.blocks = blocks;
    }
}
