package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.enums.AffairState;
import cn.superid.webapp.enums.PublicType;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.*;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.service.IAffairMemberService;
import cn.superid.webapp.service.IAffairService;
import cn.superid.webapp.service.IFileService;
import cn.superid.webapp.service.IUserService;
import cn.superid.webapp.utils.TimeUtil;

import com.wordnik.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AffairService implements IAffairService {
    @Autowired
    private IAffairMemberService affairMemberService;
    @Autowired
    private IFileService fileService;
    @Autowired
    private IUserService userService;
    @Override
    public String getPermissions(Long allianceId,Long affairId, Long roleId) throws Exception{
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id",affairId).eq("role_id",roleId).selectOne();
        if(affairMemberEntity == null){
            throw new Exception("找不到该事务成员");
        }
        if(affairMemberEntity.getPermissions()==""){
            long permissionGroupId = affairMemberEntity.getPermissionGroupId();
            if((permissionGroupId>0)||(permissionGroupId<6)){
                Iterator it = AffairPermissionRoleType.roles.keySet().iterator();
                while(it.hasNext()) {
                    Long key = (Long)it.next();
                    if(key.longValue() == permissionGroupId){
                        return AffairPermissionRoleType.roles.get(key);
                    }
                }
            }

            PermissionGroupEntity permissionGroupEntity = PermissionGroupEntity.dao.partitionId(affairId).findById(permissionGroupId);
            if(permissionGroupEntity == null){
                throw new Exception("发生了一些未知错误");
            }
            return permissionGroupEntity.getPermissions();

        }
        else{
            return affairMemberEntity.getPermissions();
        }
    }

    private void JustIndex(long parentId,int index,long allianceId){
        AffairEntity.execute(" update affair set number = number +1 where alliance_id = ? and parent_id = ? and number>= ?",new ParameterBindings(allianceId,parentId,index));//调整index顺序
        //TODO 如果redis缓存,需要更新缓存
    }

    @Override
    @Transactional
    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception{

        AffairEntity parentAffair = AffairEntity.dao.findById(createAffairForm.getAffairId());
        if(parentAffair==null){
            throw new Exception("parent affair not found ");
        }

        int count = AffairEntity.dao.eq("parentId",parentAffair.getId()).partitionId(parentAffair.getAllianceId()).count();//已有数目

        AffairEntity affairEntity=new AffairEntity();
        affairEntity.setState(AffairState.VALID);
        affairEntity.setType(parentAffair.getType());
        affairEntity.setPublicType(createAffairForm.getPublicType());
        affairEntity.setAllianceId(parentAffair.getAllianceId());
        affairEntity.setName(createAffairForm.getName());
        affairEntity.setLevel(parentAffair.getLevel()+1);
        affairEntity.setPathIndex(count+1);
        affairEntity.setNumber(createAffairForm.getNumber());
        affairEntity.setPath(parentAffair.getPath()+'/'+affairEntity.getPathIndex());
        affairEntity.save();

        this.JustIndex(parentAffair.getId(),createAffairForm.getNumber(),affairEntity.getAllianceId());

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
        affairEntity.setNumber(1);
        affairEntity.setPath("/"+affairEntity.getPathIndex());
        affairEntity.save();
        long folderId = fileService.createRootFolder(allianceId,affairEntity.getId(),0,0,roleId);
        affairEntity.setFolderId(folderId);
        AffairEntity.dao.partitionId(allianceId).id(affairEntity.getId()).set("folderId",folderId);
        try{
            affairMemberService.addMember(affairEntity.getAllianceId(),affairEntity.getId(),roleId,"",AffairPermissionRoleType.OWNER_ID);//加入根事务
        }catch (Exception e){
            e.printStackTrace();
        }


        return affairEntity;
    }

    @Override
    public String applyForEnterAffair(Long allianceId,Long affairId, Long roleId) throws Exception{
        AffairEntity affairEntity = AffairEntity.dao.findById(affairId,allianceId);
        if(affairEntity == null){
            throw new Exception("找不到该事务");
        }
        /*
        RoleEntity roleEntity = RoleEntity.dao.findById(roleId,allianceId);
        if(roleEntity == null){
            throw new Exception("找不到该角色");
        }
        */
        boolean isExist = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id",affairId).eq("role_id",roleId).state(0).exists();
        if(isExist){
            throw new Exception("该角色已在此事务中");
        }
        boolean isApplied = AffairMemberApplicationEntity.dao.partitionId(affairId).eq("role_id",roleId).eq("affair_id",affairId).state(0).exists();
        if(isApplied){
            throw new Exception("该角色已申请,正在等待审核");
        }
        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setPermissions("");
        affairMemberEntity.setRoleId(roleId);
        //TODO 此处测试时没有currentUserId,运行报错
        affairMemberEntity.setUserId(userService.currentUserId());
        affairMemberEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberEntity.setState(2);
        affairMemberEntity.setPermissionGroupId(AffairPermissionRoleType.VISITOR_ID);
        affairMemberEntity.save();

        AffairMemberApplicationEntity affairMemberApplicationEntity = new AffairMemberApplicationEntity();
        affairMemberApplicationEntity.setRoleId(roleId);
        //TODO 此处测试时没有currentUserId,运行报错
        affairMemberApplicationEntity.setUserId(userService.currentUserId());
        affairMemberApplicationEntity.setAffairId(affairId);
        affairMemberApplicationEntity.setAllianceId(allianceId);
        affairMemberApplicationEntity.setState(0);
        affairMemberApplicationEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setDealReason("");
        affairMemberApplicationEntity.save();
        return "等待审核中";
    }

    @Override
    public AffairMemberApplicationEntity findAffairMemberApplicationById(Long affairId,Long applicationId) {
        return AffairMemberApplicationEntity.dao.partitionId(affairId).findById(applicationId);
    }

    @Override
    public AffairMemberEntity agreeAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason) throws Exception {

        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId,affairId);
        if ((affairMemberApplicationEntity == null)||(affairMemberApplicationEntity.getState() != 0)) {
            throw new Exception("找不到此申请");
        }

        // TODO: 设置权限,暂时设置为客方
        AffairMemberEntity.dao.partitionId(allianceId)
                .eq("role_id",affairMemberApplicationEntity.getRoleId()).eq("affair_id",affairId).set("state",0,"permissionGroupId",AffairPermissionRoleType.GUEST_ID);

        /*
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
         */


        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(1);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        //此处测试时没有currentUserId,运行报错
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return null;
    }

    @Override
    public AffairMemberApplicationEntity rejectAffairMemberApplication(Long allianceId,Long affairId,Long applicationId, Long dealRoleId,String dealReason) throws Exception {
        AffairMemberApplicationEntity affairMemberApplicationEntity = AffairMemberApplicationEntity.dao.findById(applicationId,affairId);
        if (affairMemberApplicationEntity == null) {
            throw new Exception("找不到此申请");
        }
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId)
                .eq("affair_id",affairId).eq("role_id",affairMemberApplicationEntity.getRoleId()).selectOne();
        if(affairMemberEntity == null){
            throw new Exception("找不到该申请人");
        }
        affairMemberEntity.delete();
        //更新申请信息
        affairMemberApplicationEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairMemberApplicationEntity.setState(2);
        affairMemberApplicationEntity.setDealRoleId(dealRoleId);
        //此处测试时没有currentUserId,运行报错
        affairMemberApplicationEntity.setDealUserId(userService.currentUserId());
        affairMemberApplicationEntity.setDealReason(dealReason);
        affairMemberApplicationEntity.update();
        return affairMemberApplicationEntity;
    }

    @Override
    public boolean disableAffair(Long allianceId,Long affairId) throws Exception{
        AffairEntity affairEntity = AffairEntity.dao.partitionId(allianceId).findById(affairId);
        if(affairEntity == null){
            throw new Exception("找不到该事务"+affairId);
        }
        /*
        if(affairEntity.getLevel()<1) {
            throw new Exception("根事务不能失效");
        }
        */
        affairEntity.setState(AffairState.INVALID);
        affairEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        affairEntity.update();
        return true;
    }


}
