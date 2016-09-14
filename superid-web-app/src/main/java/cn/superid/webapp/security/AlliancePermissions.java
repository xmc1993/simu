package cn.superid.webapp.security;

import cn.superid.webapp.forms.IdNameNode;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public class AlliancePermissions {
    private static int Default = 0;

    public static int Alliance = Default;
    public static int ViewAllianceHomepage = 1;
    public static int EditAllianceInfo = 9;

    public static int Contract = Default;
    public static int SignContract = 2;
    public static int ManageContractTemplate = 10;
    public static int ViewAllContract = 18;

    public static int Fund = Default;
    public static int CheckAllFundInfo = 3;

    public static int Goods = Default;
    public static int CheckAllGoodsInfo = 4;
    public static int ManageGoodsConvertTemplate = 12;
    public static int ManageBrands = 20;

    public static int Role = Default;
    public static int AddAllianceRole = 5;
    public static int InvalidAllianceRole = 13;

    public static int AllocatePermission = 6;

    public static String getAllAlliancePermissions(){
        List<IdNameNode> rs=new ArrayList<>();
        IdNameNode childNode ;
        IdNameNode parentNode;

        parentNode =new IdNameNode(Alliance,"盟信息");
        rs.add(parentNode);
        childNode = new IdNameNode(ViewAllianceHomepage,"查看盟主页");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EditAllianceInfo,"修改盟信息");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(Contract,"合同");
        rs.add(parentNode);
        childNode = new IdNameNode(SignContract,"签字权限");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ManageContractTemplate,"管理合同模板");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ViewAllContract,"查看所有合同");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(Fund,"资金");
        rs.add(parentNode);
        childNode = new IdNameNode(CheckAllFundInfo,"查看所有资金信息");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(Goods,"资产");
        rs.add(parentNode);
        childNode = new IdNameNode(CheckAllGoodsInfo,"查看所有资产信息");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ManageGoodsConvertTemplate,"管理合成模板");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ManageBrands,"管理商标库");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(Role,"角色");
        rs.add(parentNode);
        childNode = new IdNameNode(AddAllianceRole,"添加盟成员");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(InvalidAllianceRole,"失效盟成员");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(AllocatePermission,"分配权限");
        rs.add(parentNode);

        return JSONObject.toJSONString(rs);
    }
}
