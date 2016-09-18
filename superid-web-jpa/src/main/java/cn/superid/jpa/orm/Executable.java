package cn.superid.jpa.orm;



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
