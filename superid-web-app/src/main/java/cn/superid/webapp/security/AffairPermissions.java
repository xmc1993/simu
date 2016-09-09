package cn.superid.webapp.security;

import cn.superid.webapp.forms.IdNameNode;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zp on 2016/7/26.
 */
public class AffairPermissions {
    public final static int AFFAIR = 0;
    public final static int CREATEAFFAIR = 13; //创建事务
    public final static int MOVEAFFAIR = 26 ; //移动事务
    public final static int INVALIDAFFAIR = 39; //失效事务

    public final static int AffairInfo=1; //事务信息权限
    public final static int CHECKAFFAIRMAINPAGE = 14; //查看事务主页
    public final static int EDITAFFAIRINFO = 27 ; //修改事务信息
    public final static int TRANSFEROWNER = 40; //转移负责人
    public final static int SETADMINISTRATOR = 53; //设置管理员

    public final static int CHILDAFFAIR = 2 ; //子事务权限
    public final static int CREATECHILDAFFAIR = 15; //创建子事务
    public final static int GENERATECHILDAFFAIRPERMISSION = 28; //分配子事务权限

    public final static int MEMBER = 3; //成员
    public final static int ADDAFFAIRMEMBER = 16; // 添加事务成员
    public final static int INVALIDAFFAIRMEMBER = 29; // 失效事务成员

    public final static int ANNOUNCEMENT = 4; //公告
    public final static int CHECKANNOUNCEMENT = 17;//查看公告
    public final static int ADDANNOUNCEMENT = 30; //发布公告
    public final static int EDITANNOUNCEMENT = 43; //编辑公告
    public final static int APPLYANNOUNCEMENT = 56; //回复公告
    public final static int INVALIDANNOUNCEMENT = 69; //失效公告

    public final static int TASK = 5; //任务
    public final static int CHECKTASKLIST = 18; //查看任务列表
    public final static int ADDTASK = 31; //添加任务
    public final static int ADDTASKMEMBER = 44; //添加任务成员
    public final static int REMOVETASKMEMBER = 57; //移除任务成员
    public final static int EDITTASK = 70; //编辑任务
    public final static int ADDTASKCONTENT = 83; //添加任务内容

    public final static int FILE = 6; //文件
    public final static int CHECKFILE = 19; //查看文件
    public final static int UPLOADFILE = 32; //上传文件
    public final static int MOVEFILE = 45; //移动文件
    public final static int DELETEFILE = 58; //删除文件
    public final static int DOWNLOADFILE = 71; //下载文件
    public final static int UPDATEFILEVERSION = 84; //更新文件版本
    public final static int  MANAGEFOLDER = 97; //管理文件夹

    public final static int TRADE = 7; //交易
    public final static int ADDTRADE = 20; //发起交易
    public final static int CHECKTRADEFLOW = 33; //查看交易流水表
    public final static int CHECKTRADETOANDFROM = 46; //查看交易往来表

    public final static int CONTRACT = 8; //合同
    public final static int CHECKCONTRACT = 21; //查看合同
    public final static int ADDCONTRACT = 34; //发起合同
    public final static int EDITCONTRACT = 47; //编辑合同


    public final static int PERMISSIONGROUP = 9; //权限组
    public final static int GENERATEPERMISSIONGROUP = 22; // 分配权限组
    public final static int ADDPERMISSIONGROUP = 35; // 添加权限组
    public final static int REMOVEPERMISSIONGROUP = 48; // 移除权限组
    public final static int UPDATEPERMISSIONGROUP = 61; // 更新权限组
    public final static int SETDEFAULTPERMISSIONGROUP = 74; // 设置默认权限组

    public final static int FUND = 10; //资金
    public final static int CHECKAFFAIRFUND = 23; // 查看事务资金

    public final static int GOODS = 11; //资产
    public final static int CHECKAFFAIRGOODS =24; // 查看事务资产





    public static String  getAllAffairPermissions(){

        List<IdNameNode> rs=new ArrayList<>();
        IdNameNode childNode ;
        IdNameNode parentNode;

        parentNode =new IdNameNode(AFFAIR,"事务");
        rs.add(parentNode);
        childNode = new IdNameNode(CREATEAFFAIR,"创建事务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(MOVEAFFAIR,"移动事务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(INVALIDAFFAIR,"失效事务");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(AffairInfo,"事务信息");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECKAFFAIRMAINPAGE,"查看事务主页");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDITAFFAIRINFO,"修改事务信息");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(TRANSFEROWNER,"转移负责人");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(SETADMINISTRATOR,"设置管理员");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(CHILDAFFAIR,"子事务");
        rs.add(parentNode);
        childNode = new IdNameNode(CREATECHILDAFFAIR,"创建子事务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(GENERATECHILDAFFAIRPERMISSION,"分配子事务权限");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(MEMBER,"成员");
        rs.add(parentNode);
        childNode = new IdNameNode(ADDAFFAIRMEMBER,"添加事务成员");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(INVALIDAFFAIRMEMBER,"失效事务成员");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(ANNOUNCEMENT,"公告");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECKANNOUNCEMENT,"查看公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADDANNOUNCEMENT,"发布公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDITANNOUNCEMENT,"编辑公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(APPLYANNOUNCEMENT,"回复公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(INVALIDANNOUNCEMENT,"失效公告");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(TASK,"任务");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECKTASKLIST,"查看任务列表");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADDTASK,"新建任务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADDTASKMEMBER,"添加成员");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(REMOVETASKMEMBER,"移除成员");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDITTASK,"编辑任务信息");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADDTASKCONTENT,"添加任务内容");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(FILE,"文件");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECKFILE,"查看文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(UPLOADFILE,"上传文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(MOVEFILE,"移动文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(DELETEFILE,"删除文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(DOWNLOADFILE,"下载文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(UPDATEFILEVERSION,"更新文件版本");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(MANAGEFOLDER,"文件夹管理");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(TRADE,"交易");
        rs.add(parentNode);
        childNode = new IdNameNode(ADDTRADE,"发起交易");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(CHECKTRADEFLOW,"查看交易流水表");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(CHECKTRADETOANDFROM,"查看交易往来表");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(CONTRACT,"合同");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECKCONTRACT,"查看合同");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADDCONTRACT,"创建合同");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDITCONTRACT,"编辑合同");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(PERMISSIONGROUP,"权限组");
        rs.add(parentNode);
        childNode = new IdNameNode(GENERATEPERMISSIONGROUP,"分配权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADDPERMISSIONGROUP,"添加权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(REMOVEPERMISSIONGROUP,"移除权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(UPDATEPERMISSIONGROUP,"更新权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(SETDEFAULTPERMISSIONGROUP,"设置默认权限组");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(FUND,"资金");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECKAFFAIRFUND,"查看事务资金");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(GOODS,"资产");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECKAFFAIRGOODS,"查看事务资产");
        parentNode.getChilds().add(childNode);

        return JSONObject.toJSONString(rs);
    }

}
