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
    public final static int CREATE_AFFAIR = 13; //创建事务
    public final static int MOVE_AFFAIR = 26 ; //移动事务
    public final static int INVALID_AFFAIR = 39; //失效事务

    public final static int AffairInfo=1; //事务信息权限
    public final static int CHECK_AFFAIR_HOMEPAGE = 14; //查看事务主页
    public final static int EDIT_AFFAIR_INFO = 27 ; //修改事务信息
    public final static int TRANSFER_OWNER = 40; //转移负责人
    public final static int SET_ADMINISTRATOR = 53; //设置管理员

    public final static int CHILD_AFFAIR = 2 ; //子事务权限
    public final static int CREATE_CHILD_AFFAIR = 15; //创建子事务
    public final static int GENERATE_CHILD_AFFAIR_PERMISSION = 28; //分配子事务权限

    public final static int MEMBER = 3; //成员
    public final static int ADD_AFFAIR_MEMBER = 16; // 添加事务成员
    public final static int INVALID_AFFAIR_MEMBER = 29; // 失效事务成员

    public final static int ANNOUNCEMENT = 4; //公告
    public final static int CHECK_ANNOUNCEMENT = 17;//查看公告
    public final static int ADD_ANNOUNCEMENT = 30; //发布公告
    public final static int EDIT_ANNOUNCEMENT = 43; //编辑公告
    public final static int APPLY_ANNOUNCEMENT = 56; //回复公告
    public final static int INVALID_ANNOUNCEMENT = 69; //失效公告

    public final static int TASK = 5; //任务
    public final static int CHECK_TASK_LIST = 18; //查看任务列表
    public final static int ADD_TASK = 31; //添加任务
    public final static int ADD_TASK_MEMBER = 44; //添加任务成员
    public final static int REMOVE_TASK_MEMBER = 57; //移除任务成员
    public final static int EDIT_TASK = 70; //编辑任务
    public final static int ADD_TASK_CONTENT = 83; //添加任务内容

    public final static int FILE = 6; //文件
    public final static int CHECK_FILE = 19; //查看文件
    public final static int UPLOAD_FILE = 32; //上传文件
    public final static int MOVE_FILE = 45; //移动文件
    public final static int DELETE_FILE = 58; //删除文件
    public final static int DOWNLOAD_FILE = 71; //下载文件
    public final static int UPDATE_FILE_VERSION = 84; //更新文件版本
    public final static int MANAGE_FOLDER = 97; //管理文件夹

    public final static int TRADE = 7; //交易
    public final static int ADD_TRADE = 20; //发起交易
    public final static int CHECK_TRADE_FLOW = 33; //查看交易流水表
    public final static int CHECK_TRADE_TO_AND_FROM = 46; //查看交易往来表

    public final static int CONTRACT = 8; //合同
    public final static int CHECK_CONTRACT = 21; //查看合同
    public final static int ADD_CONTRACT = 34; //发起合同
    public final static int EDIT_CONTRACT = 47; //编辑合同


    public final static int PERMISSION_GROUP = 9; //权限组
    public final static int GENERATE_PERMISSION_GROUP = 22; // 分配权限组
    public final static int ADD_PERMISSION_GROUP = 35; // 添加权限组
    public final static int REMOVE_PERMISSION_GROUP = 48; // 移除权限组
    public final static int UPDATE_PERMISSION_GROUP = 61; // 更新权限组
    public final static int SET_DEFAULT_PERMISSION_GROUP = 74; // 设置默认权限组

    public final static int FUND = 10; //资金
    public final static int CHECK_AFFAIR_FUND = 23; // 查看事务资金

    public final static int GOODS = 11; //资产
    public final static int CHECK_AFFAIR_GOODS =24; // 查看事务资产

    public static String affairPermissions = null;




    public static String  getAllAffairPermissions(){

        if(affairPermissions!=null){
            return affairPermissions;
        }

        List<IdNameNode> rs=new ArrayList<>();
        IdNameNode childNode ;
        IdNameNode parentNode;

        parentNode =new IdNameNode(AFFAIR,"事务");
        rs.add(parentNode);
        childNode = new IdNameNode(CREATE_AFFAIR,"创建事务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(MOVE_AFFAIR,"移动事务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(INVALID_AFFAIR,"失效事务");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(AffairInfo,"事务信息");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECK_AFFAIR_HOMEPAGE,"查看事务主页");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDIT_AFFAIR_INFO,"修改事务信息");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(TRANSFER_OWNER,"转移负责人");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(SET_ADMINISTRATOR,"设置管理员");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(CHILD_AFFAIR,"子事务");
        rs.add(parentNode);
        childNode = new IdNameNode(CREATE_CHILD_AFFAIR,"创建子事务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(GENERATE_CHILD_AFFAIR_PERMISSION,"分配子事务权限");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(MEMBER,"成员");
        rs.add(parentNode);
        childNode = new IdNameNode(ADD_AFFAIR_MEMBER,"添加事务成员");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(INVALID_AFFAIR_MEMBER,"失效事务成员");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(ANNOUNCEMENT,"公告");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECK_ANNOUNCEMENT,"查看公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADD_ANNOUNCEMENT,"发布公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDIT_ANNOUNCEMENT,"编辑公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(APPLY_ANNOUNCEMENT,"回复公告");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(INVALID_ANNOUNCEMENT,"失效公告");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(TASK,"任务");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECK_TASK_LIST,"查看任务列表");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADD_TASK,"新建任务");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADD_TASK_MEMBER,"添加成员");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(REMOVE_TASK_MEMBER,"移除成员");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDIT_TASK,"编辑任务信息");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADD_TASK_CONTENT,"添加任务内容");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(FILE,"文件");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECK_FILE,"查看文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(UPLOAD_FILE,"上传文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(MOVE_FILE,"移动文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(DELETE_FILE,"删除文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(DOWNLOAD_FILE,"下载文件");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(UPDATE_FILE_VERSION,"更新文件版本");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(MANAGE_FOLDER,"文件夹管理");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(TRADE,"交易");
        rs.add(parentNode);
        childNode = new IdNameNode(ADD_TRADE,"发起交易");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(CHECK_TRADE_FLOW,"查看交易流水表");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(CHECK_TRADE_TO_AND_FROM,"查看交易往来表");
        parentNode.getChilds().add(childNode);

        parentNode =new IdNameNode(CONTRACT,"合同");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECK_CONTRACT,"查看合同");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADD_CONTRACT,"创建合同");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(EDIT_CONTRACT,"编辑合同");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(PERMISSION_GROUP,"权限组");
        rs.add(parentNode);
        childNode = new IdNameNode(GENERATE_PERMISSION_GROUP,"分配权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(ADD_PERMISSION_GROUP,"添加权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(REMOVE_PERMISSION_GROUP,"移除权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(UPDATE_PERMISSION_GROUP,"更新权限组");
        parentNode.getChilds().add(childNode);
        childNode = new IdNameNode(SET_DEFAULT_PERMISSION_GROUP,"设置默认权限组");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(FUND,"资金");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECK_AFFAIR_FUND,"查看事务资金");
        parentNode.getChilds().add(childNode);


        parentNode =new IdNameNode(GOODS,"资产");
        rs.add(parentNode);
        childNode = new IdNameNode(CHECK_AFFAIR_GOODS,"查看事务资产");
        parentNode.getChilds().add(childNode);

        affairPermissions = JSONObject.toJSONString(rs);

        return affairPermissions;
    }

}
