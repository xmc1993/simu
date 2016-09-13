package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.enums.AllianceType;
import cn.superid.webapp.enums.PublicType;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.*;
import cn.superid.webapp.security.PermissionRoleType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AffairService implements IAffairService {
    @Autowired
    private IAffairMemberService affairMemberService;

    private IUserService userService;
    @Override
    public String getPermissions(long affairId, long roleId) {
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.eq("affairId",affairId).eq("roleId",roleId).selectOne();
        if(affairMemberEntity.getPermissions()==""){
            long permissionGroupId = affairMemberEntity.getPermissionGroupId();
            //switch不支持long
            if(permissionGroupId==1L){
                return PermissionRoleType.OWNER;
            }
            else if(permissionGroupId == 2L){
                return PermissionRoleType.ADMINISTRATOR;
            }
            else if(permissionGroupId == 3L){
                return PermissionRoleType.OFFICIAL;
            }
            else if(permissionGroupId == 4L){
                return PermissionRoleType.GUEST;
            }else if(permissionGroupId == 5L){
                return PermissionRoleType.VISITOR;
            }
            else{
                PermissionGroupEntity permissionGroupEntity = PermissionGroupEntity.dao.findById(permissionGroupId);
                return permissionGroupEntity.getPermissions();
            }
        }
        else{
            return affairMemberEntity.getPermissions();
        }
    }

    private void JustIndex(long parentId,int index){
        AffairEntity.execute(" update affair set index = index +1 where parent_id = ? and index>= ?",new ParameterBindings(parentId,index));//调整index顺序
        //TODO 如果redis缓存,需要更新缓存
    }

    @Override
    @Transactional
    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception{

        AffairEntity parentAffair = AffairEntity.dao.findById(createAffairForm.getAffairId());
        if(parentAffair==null){
            throw new Exception("parent affair not found ");
        }

        int count = AffairEntity.dao.eq("parentId",parentAffair.getId()).count();//已有数目

        AffairEntity affairEntity=new AffairEntity();
        affairEntity.setType(parentAffair.getType());
        affairEntity.setPublicType(createAffairForm.getPublicType());
        affairEntity.setAllianceId(parentAffair.getAllianceId());
        affairEntity.setName(createAffairForm.getName());
        affairEntity.setLevel(parentAffair.getLevel()+1);
        affairEntity.setPathIndex(count+1);
        affairEntity.setPath(parentAffair.getPath()+'/'+affairEntity.getPathIndex());
        affairEntity.save();

        this.JustIndex(parentAffair.getId(),createAffairForm.getIndex());

        return affairEntity;
    }

    /**
     * 创建根事务,一般根据盟名称产生,一个盟对应一个根事务
     * @param allianceId
     * @param name
     * @param roleId
     * @param type
     * @return
     */
    @Override
    public AffairEntity createRootAffair(long allianceId, String name, long roleId,int type) {
        AffairEntity affairEntity=new AffairEntity();
        affairEntity.setType(type);
        affairEntity.setPublicType(PublicType.TO_ALLIANCE);
        affairEntity.setAllianceId(allianceId);
        affairEntity.setName(name);
        affairEntity.setLevel(1);
        affairEntity.setPathIndex(1);
        affairEntity.setPath("/"+affairEntity.getPathIndex());
        affairEntity.save();

        affairMemberService.addMember(affairEntity.getId(),roleId,null,PermissionRoleType.OWNER_ID);//加入根事务
        return affairEntity;
    }

    @Override
    @Transactional
    public String applyForEnterAffair(long affairId, long roleId) {
        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        RoleEntity roleEntity = RoleEntity.dao.findById(roleId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setPermissions("");
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setUserId(roleEntity.getUserId());
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setState(0);
        affairMemberEntity.save();
        return "等待审核中";
    }

    @Override
    public AffairMemberApplicationEntity findAffairMemberApplicationById(Long applicationId) {
        return AffairMemberApplicationEntity.dao.findById(applicationId);
    }

    @Override
    @Transactional
    public AffairMemberEntity agreeAffairMemberApplication(Long applicationId, Long dealRoleId,String dealReason) throws Exception {
        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId);
        if (affairMemberApplicationEntity == null) {
            throw new Exception("找不到此申请");
        }
        AffairEntity affairEntity = AffairEntity.dao.findById(affairMemberApplicationEntity.getAffairId());
        if (affairEntity == null) {
            throw new Exception("找不到事务" + affairMemberApplicationEntity.getAffairId());
        }

        // TODO: 设置权限,暂时设置为游客即为5

        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setRoleId(affairMemberApplicationEntity.getRoleId());
        affairMemberEntity.setAffairId(affairEntity.getId());
        affairMemberEntity.setUserId(affairMemberApplicationEntity.getUserId());
        affairMemberEntity.setState(0);
        affairMemberEntity.setPermissions("");
        affairMemberEntity.setPermissionGroupId(5L);
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.save();


        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(1);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return affairMemberEntity;
    }

    @Override
    public AffairMemberApplicationEntity rejectAffairMemberApplication(Long applicationId, Long dealRoleId,String dealReason) throws Exception {
        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId);
        if (affairMemberApplicationEntity == null) {
            throw new Exception("找不到此申请");
        }
        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(2);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return affairMemberApplicationEntity;
    }


}
