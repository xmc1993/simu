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

    public final static int Alliance = Default;
    public final static int ViewAllianceHomepage = 1;
    public final static int EditAllianceInfo = 9;

    public final static int Contract = Default;
    public final static int SignContract = 2;
    public final static int ManageContractTemplate = 10;
    public final static int ViewAllContract = 18;

    public final static int Fund = Default;
    public final static int CheckAllFundInfo = 3;

    public final static int Goods = Default;
    public final static int CheckAllGoodsInfo = 4;
    public final static int ManageGoodsConvertTemplate = 12;
    public final static int ManageBrands = 20;

    public final static int Role = Default;
    public final static int AddAllianceRole = 5;
    public final static int InvalidAllianceRole = 13;

    public final static int ManageUser = 21;

    public final static int AllocatePermission = 6;

    public static String alliancePermissions = null;

    private AlliancePermissions(){

    }

    public static String getAllAlliancePermissions() {
        if (alliancePermissions != null) {
            return alliancePermissions;
        }

        synchronized (AlliancePermissions.class) {//避免多次初始化
            List<IdNameNode> rs = new ArrayList<>();
            IdNameNode childNode;
            IdNameNode parentNode;

            parentNode = new IdNameNode(Alliance, "盟信息");
            rs.add(parentNode);
            childNode = new IdNameNode(ViewAllianceHomepage, "查看盟主页");
            parentNode.getChilds().add(childNode);
            childNode = new IdNameNode(EditAllianceInfo, "修改盟信息");
            parentNode.getChilds().add(childNode);

            parentNode = new IdNameNode(Contract, "合同");
            rs.add(parentNode);
            childNode = new IdNameNode(SignContract, "签字权限");
            parentNode.getChilds().add(childNode);
            childNode = new IdNameNode(ManageContractTemplate, "管理合同模板");
            parentNode.getChilds().add(childNode);
            childNode = new IdNameNode(ViewAllContract, "查看所有合同");
            parentNode.getChilds().add(childNode);

            parentNode = new IdNameNode(Fund, "资金");
            rs.add(parentNode);
            childNode = new IdNameNode(CheckAllFundInfo, "查看所有资金信息");
            parentNode.getChilds().add(childNode);

            parentNode = new IdNameNode(Goods, "资产");
            rs.add(parentNode);
            childNode = new IdNameNode(CheckAllGoodsInfo, "查看所有资产信息");
            parentNode.getChilds().add(childNode);
            childNode = new IdNameNode(ManageGoodsConvertTemplate, "管理合成模板");
            parentNode.getChilds().add(childNode);
            childNode = new IdNameNode(ManageBrands, "管理商标库");
            parentNode.getChilds().add(childNode);

            parentNode = new IdNameNode(Role, "角色");
            rs.add(parentNode);
            childNode = new IdNameNode(AddAllianceRole, "添加盟成员");
            parentNode.getChilds().add(childNode);
            childNode = new IdNameNode(InvalidAllianceRole, "失效盟成员");
            parentNode.getChilds().add(childNode);

            parentNode = new IdNameNode(AllocatePermission, "分配权限");
            rs.add(parentNode);
            alliancePermissions = JSONObject.toJSONString(rs);
        }

        return alliancePermissions;
    }
}
