package cn.superid.webapp.dao.impl;

/**
 * Created by jizhenya on 16/11/23.
 * 用于存放长sql语句
 */
public class SQLDao {

    /**
     * userService相关的sql语句
     */
    //获取用户的所有盟下的所有角色
    public static String GET_USER_ALLIANCE_ROLES = "select t1.id as alliance_id,t1.name as alliance_name,t1.logo_url,t2.public_type,t2.role_id,t2.title as role_name from  " +
            "(select a.id,a.name,a.logo_url from alliance  a where a.id in (select alliance_id from role where user_id = ? group by alliance_id)) t1 " +
            "join" +
            "(select r.id as role_id,r.title,r.alliance_id,r.public_type from role r where r.user_id = ?) t2 " +
            "on t1.id = t2.alliance_id order by alliance_id";

}
