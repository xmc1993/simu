package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.model.PermissionGroupEntity;
import cn.superid.webapp.model.RoleEntity;
import cn.superid.webapp.security.PermissionRoleType;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.utils.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AffairService implements IAffairService {
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
                return PermissionRoleType.OFFCIAL;
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
        affairMemberEntity.setType(0);
        affairMemberEntity.save();
        return "等待审核中";
    }
}
