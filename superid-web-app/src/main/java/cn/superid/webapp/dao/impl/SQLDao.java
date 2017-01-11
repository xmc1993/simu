package cn.superid.webapp.dao.impl;

/**
 * Created by jizhenya on 16/11/23.
 * 用于存放长sql语句
 */
public class SQLDao {

    /**
     * affairService相关的sql语句
     */
    public static String GET_ALL_OFFICIALS = "select a.role_id as roleId , a.permissions as permissions , b.user_id as userId , b.title as title , d.name as affairName , d.id as affairId from " +
            "(select af.role_id , af.permissions from affair_member af where af.state = 1 and af.affair_id = ? and af.alliance_id = ? and af.permission_group_id < 4 ) a " +
            " join (select bf.title , bf.id , bf.belong_affair_id , bf.user_id from role bf where bf.alliance_id = ? ) b " +
            " join (select df.id , df.name from affair df where df.alliance_id = ? ) d " +
            " on a.role_id = b.id and b.belong_affair_id = d.id ";


    public static String GET_AFFAIR_TREE = "select a.id , a.parent_id , a.name , a.short_name , a.alliance_id , a.superid , a.public_type , b.is_stuck , a.path , b.role_id as roleId ,a.modify_time from " +
            "(select * from affair where alliance_id = ? and state = 0 ) a " +
            "left join (select role_id,affair_id,is_stuck from affair_user where alliance_id = ? and user_id = ? ) b " +
            "on a.id = b.affair_id ";

    public static String GET_AFFAIR_TREE_BY_USER = "select a.id , a.parent_id , a.name , a.short_name , a.alliance_id , a.superid , a.public_type , b.is_stuck , a.path , b.role_id as roleId,a.modify_time from " +
            "(select * from affair where alliance_id in (" +
            "select alliance_id from role where user_id = ? ) and state = 0 ) a " +
            "left join (select role_id,affair_id,is_stuck from affair_user where user_id = ? ) b " +
            "on a.id = b.affair_id ";

    //获取用户的盟外事务
    public static String GET_OUT_ALLIANCE_AFFAIRS = "select * from " +
            "(select au.affair_id from affair_user au where au.user_id = ? and au.alliance_id not in " +
            "(select alu.alliance_id from alliance_user alu where alu.user_id = ?)) b " +
            "join affair a " +
            "on a.id = b.affair_id ";

    /**
     * userService相关的sql语句
     */
    //获取用户的所有盟下的所有角色
    public static String GET_USER_ALLIANCE_ROLES = "select t1.id as alliance_id,t1.name as alliance_name,t1.logo_url,t2.public_type,t2.role_id,t2.title as role_name from  " +
            "(select a.id,a.name,a.logo_url from alliance  a where a.id in (select alliance_id from role where user_id = ? group by alliance_id)) t1 " +
            "join" +
            "(select r.id as role_id,r.title,r.alliance_id,r.public_type from role r where r.user_id = ?) t2 " +
            "on t1.id = t2.alliance_id";

    /**
     * affairUserService相关的sql语句
     */
    //获取某个事务下的所有成员及其对应的需要显示的字段
    public static String GET_AFFAIR_USERS = "select t1.id as user_id,t1.username,t1.superid,t1.gender,t2.id as role_id,t2.title,t2.belong_affair_id ,t2.name as belong_affair_name from " +
            "(select * from user u where u.id in (select au.user_id from affair_user au where au.affair_id = ? )) t1 " +
            "left join (select r.id,r.title,r.belong_affair_id,r.user_id,a.name from role r join affair a on a.id = r.belong_affair_id where r.alliance_id = ? and r.state = 0) t2 " +
            "on t1.id=t2.user_id";




}