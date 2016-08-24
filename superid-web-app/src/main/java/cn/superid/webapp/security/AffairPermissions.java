package cn.superid.webapp.security;

import cn.superid.webapp.forms.IdNameNode;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public class AffairPermissions {
    public final static int Default=0;
    public final static int EditNotice=1;
    public final static int AddNotice =39;
    public final static int DeleteNotice =3;
    public final static int AddAffair=4;




    public static String  getAllAffairPermissions(){

        List<IdNameNode> rs=new ArrayList<>();
        IdNameNode idNameNode =new IdNameNode(AffairPermissions.Default,"公告管理");
        rs.add(idNameNode);

        IdNameNode idNameNode1 = new IdNameNode(EditNotice,"编辑公告");
        idNameNode.getChilds().add(idNameNode1);

        IdNameNode idNameNode2 = new IdNameNode(AddNotice,"发布公告");
        idNameNode.getChilds().add(idNameNode2);

        IdNameNode idNameNode3 = new IdNameNode(DeleteNotice,"删除公告");
        idNameNode.getChilds().add(idNameNode3);

        return JSONObject.toJSONString(rs);
    }

}
