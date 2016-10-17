package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

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
    public String getPermissions(String permissions,long permissionGroupId,long affairId) throws Exception{

        if(StringUtil.isEmpty(permissions)){
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
            return permissions;
        }
    }

    private void JustIndex(long parentId,int index,long allianceId){
        AffairEntity.execute(" update affair set number = number +1 where alliance_id = ? and parent_id = ? and number>= ?",new ParameterBindings(allianceId,parentId,index));//调整index顺序
        //TODO 如果redis缓存,需要更新缓存
    }

    @Override
    @Transactional
    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception{

        AffairEntity parentAffair = AffairEntity.dao.findById(createAffairForm.getAffairId(),createAffairForm.getAllianceId());
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
        affairEntity.setPath(parentAffair.getPath()+'-'+affairEntity.getPathIndex());
        affairEntity.save();

        this.JustIndex(parentAffair.getId(),createAffairForm.getNumber(),affairEntity.getAllianceId());//调整事务顺序

        affairMemberService.addCreator(affairEntity.getAllianceId(),affairEntity.getId(),createAffairForm.getOperationRoleId());//作为创建者

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
        long folderId = fileService.createRootFolderForAffair(allianceId,affairEntity.getId(),roleId);
        affairEntity.setFolderId(folderId);
        AffairEntity.dao.partitionId(allianceId).id(affairEntity.getId()).set("folderId",folderId);
        try{
            affairMemberService.addCreator(affairEntity.getAllianceId(),affairEntity.getId(),roleId);//加入根事务
        }catch (Exception e){
            e.printStackTrace();
        }


        return affairEntity;
    }

    @Override
    public List<AffairEntity> getAllChildAffair(long allianceId, long affairId) throws Exception {
        AffairEntity affairEntity = AffairEntity.dao.findById(affairId,allianceId);
        if(affairEntity == null)
            throw new Exception("找不到该事务");
        List<AffairEntity> result = AffairEntity.dao.partitionId(allianceId).eq("parent_id",affairId).selectList();

        return result;
    }


    @Override
    public boolean disableAffair(Long allianceId,Long affairId) throws Exception{
        AffairEntity affairEntity = AffairEntity.dao.findById(affairId,allianceId);
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
