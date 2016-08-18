package cn.superid.jpa.orm;

import cn.superid.jpa.core.Session;
import cn.superid.jpa.util.ParameterBindings;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by zp on 2016/7/20.
 */
public interface Executable {

    /**
     * save entity
     */
    public void save();
    /**
     * update entity
     */

    public void update();

    /**
     * delete entity
     */
    public void delete();

}
