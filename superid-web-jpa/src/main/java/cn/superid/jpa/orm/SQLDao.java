package cn.superid.jpa.orm;

/**
 * Created by jizhenya on 16/11/23.
 */
public class SQLDao {

    public static String GET_ALL_ROLE = "select a.role_id as roleId , a.permissions as permissions , b.user_id as userId , b.title as title , d.name as affairName , d.id as affairId from " +
            "(select af.role_id , af.permissions from affair_member af where af.state = 1 and af.affair_id = ? and af.alliance_id = ? and af.permission_group_id < 4 ) a " +
            " join (select bf.title , bf.id , bf.belong_affair_id , bf.user_id from role bf where bf.alliance_id = ? ) b " +
            " join (select df.id , df.name from affair df where df.alliance_id = ? ) d " +
            " on a.role_id = b.id and b.belong_affair_id = d.id ";


    public static String GET_AFFAIR_TREE = "select a.id , a.parent_id , a.name , a.short_name , a.alliance_id , a.superid , a.public_type , a.is_stuck , a.path , b.role_id as roleId from " +
            "(select * from affair where alliance_id = ? and state = 0 ) a " +
            "left join (select role_id,affair_id from affair_user where alliance_id = ? and user_id = ? ) b " +
            "on a.id = b.affair_id ";

    public static String GET_AFFAIR_TREE_BY_USER = "select a.id , a.parent_id , a.name , a.short_name , a.alliance_id , a.superid , a.public_type , a.is_stuck , a.path , b.role_id as roleId from " +
            "(select * from affair where alliance_id in (" +
            "select alliance_id from role where user_id = ? ) and state = 0 ) a " +
            "left join (select role_id,affair_id from affair_user where user_id = ? ) b " +
            "on a.id = b.affair_id ";


}
