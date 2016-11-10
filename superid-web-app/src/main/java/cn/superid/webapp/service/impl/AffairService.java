package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.ConditionalDao;
import cn.superid.jpa.util.Expr;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import cn.superid.webapp.enums.AffairSpecialCondition;
import cn.superid.webapp.enums.AffairState;
import cn.superid.webapp.enums.PublicType;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.AffairMemberCache;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.AffairPermissionRoleType;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.service.*;
import cn.superid.webapp.service.forms.SimpleRoleForm;
import cn.superid.webapp.service.vo.GetRoleVO;
import cn.superid.webapp.utils.TimeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private ITaskService taskService;
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

    private void adjustOrder(long parentId,int index,long allianceId){
        AffairEntity.execute(" update affair set number = number +1 where alliance_id = ? and parent_id = ? and number>= ?",new ParameterBindings(allianceId,parentId,index));//调整index顺序
        //TODO 如果redis缓存,需要更新缓存
    }

    @Override
    @Transactional
    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception{
        long parentAffairId = createAffairForm.getAffairId();
        long parentAllianceId = createAffairForm.getAllianceId();
        AffairEntity parentAffair = AffairEntity.dao.findById(parentAffairId,parentAllianceId);
        if(parentAffair==null){
            throw new Exception("parent affair not found ");
        }
        int count = AffairEntity.dao.eq("parentId",parentAffairId).partitionId(parentAllianceId).count();//已有数目

        //this.adjustOrder(parentAffair.getId(),createAffairForm.getNumber(),parentAllianceId);//调整事务顺序

        AffairEntity affairEntity=new AffairEntity();
        affairEntity.setParentId(parentAffairId);
        affairEntity.setCreateRoleId(createAffairForm.getOperationRoleId());
        affairEntity.setState(AffairState.VALID);
        affairEntity.setType(parentAffair.getType());
        affairEntity.setPublicType(createAffairForm.getPublicType());
        affairEntity.setAllianceId(parentAffair.getAllianceId());
        affairEntity.setName(createAffairForm.getName());
        affairEntity.setLevel(parentAffair.getLevel()+1);
        affairEntity.setPathIndex(count+1);
        affairEntity.setNumber(count+1);
        affairEntity.setPath(parentAffair.getPath()+'-'+affairEntity.getPathIndex());
        affairEntity.save();

        long folderId = fileService.createRootFolderForAffair(createAffairForm.getAllianceId(),affairEntity.getId(),createAffairForm.getOperationRoleId());
        affairEntity.setFolderId(folderId);
        AffairEntity.dao.partitionId(createAffairForm.getAllianceId()).id(affairEntity.getId()).set("folderId",folderId);


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
    public List<AffairEntity> getAffairByState(long allianceId, int state) throws Exception {
        //TODO 参数未定,到时候看前端需要什么数据
        List<AffairEntity> result = AffairEntity.dao.partitionId(allianceId).state(state).selectList();
        return result;
    }

    @Override
    public List<AffairEntity> getAllDirectChildAffair(long allianceId, long affairId) throws Exception {
        boolean isExist  = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if(!isExist)
            throw new Exception("找不到该事务");

        List<AffairEntity> result = AffairEntity.dao.partitionId(allianceId).eq("parentId",affairId).selectList("id","allianceId","name","level","path");

        return result;
    }


    @Override
    public boolean disableAffair(Long allianceId,Long affairId) throws Exception{
        int isUpdate = AffairEntity.dao.id(affairId).partitionId(allianceId).set("state",AffairState.INVALID);
        if(isUpdate == 0){
            return false;
        }


        List<TaskEntity> tasks = taskService.getAllValidAffair(allianceId,affairId,"id");

        //失效所有子事务
        List<AffairEntity> childAffairs = getAllChildAffairs(allianceId,affairId,"id");
        long id;
        for(AffairEntity affairEntity : childAffairs){
            id = affairEntity.getId();
            AffairEntity.dao.partitionId(allianceId).id(id).set("state",AffairState.INVALID);
            //每个子事务下的task
            tasks.addAll(taskService.getAllValidAffair(allianceId,id,"id"));
        }

        //关闭所有任务
        for(TaskEntity taskEntity : tasks){
            id = taskEntity.getId();
            TaskEntity.dao.partitionId(allianceId).id(id).set("state",0);
        }

        //TODO 关闭本事务以及子事务下的交易

        /*
        if(affairEntity.getLevel()<1) {
            throw new Exception("根事务不能失效");
        }
        */
        return true;
    }

    @Override
    public boolean validAffair(long allianceId, long affairId) throws Exception {
        int isUpdate = AffairEntity.dao.id(affairId).partitionId(allianceId).set("state",AffairState.VALID);
        if(isUpdate == 0){
            return false;
        }

        List<AffairEntity> childAffairs = getAllChildAffairs(allianceId,affairId,"id");
        long id;
        for(AffairEntity affairEntity : childAffairs){
            id = affairEntity.getId();
            AffairEntity.dao.partitionId(allianceId).id(id).set("state",AffairState.VALID);
        }

        return true;
    }

    @Override
    public int canGenerateAffair(long allianceId, long affairId) throws Exception {
        boolean hasChild = AffairEntity.dao.partitionId(allianceId).eq("parentId",affairId).exists();
        if(hasChild){
            return AffairSpecialCondition.HAS_CHILD;
        }
        //TODO 检测交易表里有没有affairId是本事务的交易,return 2

        return AffairSpecialCondition.NO_SPECIAL;
    }

    private boolean hasPermission(String permissions,int toFindPermission){
        String[] permission = permissions.split(",");
        for(String str : permission){
            if(str.equals(toFindPermission+"")){
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean moveAffair(long allianceId,long affairId,long targetAffairId,long roleId) throws Exception{
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId).eq("affairId",targetAffairId).eq("roleId",roleId).selectOne("id","permissions","permissionGroupId");
        if(affairMemberEntity == null){
            //TODO 发给所有有权限接受事务的角色通知
        }
        String permissions = getPermissions(affairMemberEntity.getPermissions(),affairMemberEntity.getPermissionGroupId(),targetAffairId);
        boolean hasMovePermission = hasPermission(permissions, AffairPermissions.MOVE_AFFAIR);
        if(!hasMovePermission){
            //TODO 发给所有有权限接受事务的角色通知
        }


        //获取目标事务的一级子事务,然后取出最大的number加上1就是待移动事务的number
        int max_number = AffairEntity.dao.partitionId(allianceId).eq("parentId",targetAffairId).desc("number").selectOne("number").getNumber()+1;
        AffairEntity targetAffair = AffairEntity.dao.partitionId(allianceId).id(targetAffairId).selectOne("level","path");
        //获取目标事务的层级,加到待移动的所有事务上
        int targetLevel = targetAffair.getLevel();
        String targetPath = targetAffair.getPath();

        //根据待移动事务的子事务的level减去待移动事务的level,差值加上目标事务的level,就是待移动事务的所有子事务的level
        //即temp-source+target+1,把target和source的差值算出来
        int sourceLevel = AffairEntity.dao.partitionId(allianceId).id(affairId).selectOne("level").getLevel();
        int offsetLevel = targetLevel-sourceLevel+1;
        //待移动事务本身的parentId,level,number和path
        AffairEntity.dao.partitionId(allianceId).id(affairId).set("parent_id",targetAffairId,"level",targetLevel+1,"number",max_number,"path",targetPath+"-"+max_number);


        //需要找到的所有子事务
        List<AffairEntity> allChildAffairs = getAllChildAffairs(allianceId,affairId,"id","level","path");
        String basePath = AffairEntity.dao.findById(affairId,allianceId).getPath();
        long id;
        int oldLevel,remainingLengthOfPath;
        String oldPath,newPath;
        for(AffairEntity affairEntity : allChildAffairs){
            id = affairEntity.getId();
            oldLevel = affairEntity.getLevel();
            oldPath = affairEntity.getPath();
            //将当前事务的level和待移动事务的level相减,然后取path的后几位substring,长度为相减后的值
            remainingLengthOfPath = (oldLevel-sourceLevel)*2;
            newPath = basePath+oldPath.substring(oldPath.length()-remainingLengthOfPath);
            AffairEntity.dao.id(id).partitionId(allianceId).set("path",newPath,"level",oldLevel+offsetLevel);
            /*
            affairEntity.setLevel(oldLevel+offsetLevel);
            affairEntity.setPath(newPath);
            affairEntity.update();
            */
        }
        return false;
    }

    @Override
    public boolean modifyAffairInfo(long allianceId, long affairId, int attribute, Object value) throws Exception {
        ConditionalDao<AffairEntity> conditionalDao = AffairEntity.dao.partitionId(allianceId).id(affairId);
        switch (attribute){
            case 1:
                if(value instanceof Integer){
                    conditionalDao.set("publicType",value);
                }
                else
                    throw new Exception("类型错误,应该为int类型");
                break;
            case 2:
                if(value instanceof String){
                    conditionalDao.set("name",value);
                }
                else
                    throw new Exception("类型错误,应该为String类型");
                break;
            case 3:
                if(value instanceof String){
                    conditionalDao.set("description",value);
                }
                else
                    throw new Exception("类型错误,应该为String类型");
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean modifyAffairInfo(long allianceId, long affairId, Integer publicType, String affairName, String description) throws Exception {
        ConditionalDao<AffairEntity> conditionalDao = AffairEntity.dao.partitionId(allianceId).id(affairId);
        if(publicType != null){
            conditionalDao.set("publicType",publicType);
        }
        if((affairName != null)||(!affairName.equals(""))){
            conditionalDao.set("affairName",affairName);
        }
        return false;
    }

    @Override
    public List<AffairEntity> getAllChildAffairs(long allianceId, long affairId,String... params) {
        String basePath = AffairEntity.dao.findById(affairId,allianceId).getPath();
        List<AffairEntity> result = AffairEntity.dao.partitionId(allianceId).lk("path",basePath+"-%").selectList(params);
        return result;
    }
    @Override
    public boolean addCovers(long allianceId, long affairId, String urls) {
        String[] urlList = urls.split(",");
        boolean isFirst = !CoverEntity.dao.eq("affair_id",affairId).partitionId(allianceId).exists();
        for(int i = 0 ; i < urlList.length ; i++){
            CoverEntity coverEntity = new CoverEntity();
            if(i == 0 && isFirst == true){
                //是第一,则设为默认封面
                coverEntity.setIsDefault(1);
            }else{
                coverEntity.setIsDefault(0);
            }
            coverEntity.setAffairId(affairId);
            coverEntity.setAllianceId(allianceId);
            coverEntity.setUrl(urlList[i]);
            coverEntity.save();
        }
        return true;
    }

    @Override
    public boolean setDefaultCover(long allianceId, long affairId, long coverId) {
        //先找出之前的默认图片,将其设为非默认
        CoverEntity.dao.partitionId(allianceId).eq("affair_id",affairId).eq("is_default",1).set("is_default",0);
        //改变默认值
        int result = CoverEntity.dao.id(coverId).partitionId(allianceId).set("is_default",1);
        if(result == 0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public List<SimpleRoleForm> getAllRoles(long allianceId , long affairId) {
        List<SimpleRoleForm> result = new ArrayList<>();
        //第一步,查本盟中的affairmember,防止跨库join
        StringBuilder sql = new StringBuilder("select a.role_id as roleId , a.permissions as permissions , b.user_id as userId , b.title as title , d.name as affairName , d.id as affairId from " +
                "(select af.role_id , af.permissions from affair_member af where af.state = 1 and af.affair_id = ? and af.alliance_id = ? and af.permission_group_id < 4 ) a " +
                " join (select bf.title , bf.id , bf.belong_affair_id , bf.user_id from role bf where bf.alliance_id = ? ) b " +
                " join (select df.id , df.name from affair df where df.alliance_id = ? ) d " +
                " on a.role_id = b.id and b.belong_affair_id = d.id ");
        ParameterBindings p1 = new ParameterBindings();
        p1.addIndexBinding(affairId);
        p1.addIndexBinding(allianceId);
        p1.addIndexBinding(allianceId);
        p1.addIndexBinding(allianceId);

        List<GetRoleVO> selfMember = RoleEntity.dao.getSession().findList(GetRoleVO.class, sql.toString(), p1);
        for(GetRoleVO g : selfMember){
            UserBaseInfo user = UserBaseInfo.dao.findById(g.getUserId());
            SimpleRoleForm s = new SimpleRoleForm(g.getRoleId(),user.getUsername(),g.getTitle(),g.getPermissions(),g.getAffairId(),g.getAffairName());
            result.add(s);
        }


        //第二步,把非本盟的官方加入
        List<AffairMemberEntity> otherMmeber = AffairMemberEntity.dao.eq("affair_id",affairId).state(1).neq("alliance_id",allianceId).selectList();
        for(AffairMemberEntity a : otherMmeber){
            RoleCache role = RoleCache.dao.findById(a.getRoleId());
            UserBaseInfo user = UserBaseInfo.dao.findById(role.getUserId());
            SimpleRoleForm s = new SimpleRoleForm(a.getRoleId(),user.getUsername(),role.getTitle(),a.getPermissions(),-1,"");
            result.add(s);
        }



        return result;
    }

    @Override
public List<CoverEntity> getCovers(long allianceId, long affairId) {
        return CoverEntity.dao.partitionId(allianceId).eq("affair_id",affairId).selectList();
    }

    @Override
    public List<Integer> affairOverview(long allianceId, long affairId) {
        List<Integer> result = new ArrayList<>();
        int member = AffairMemberEntity.dao.partitionId(allianceId).eq("affair_id",affairId).count();
        int file = FileEntity.dao.partitionId(allianceId).eq("affair_id",affairId).count();
        int announcement = AnnouncementEntity.dao.partitionId(allianceId).eq("affair_id",affairId).count();
        //TODO:事务这块待确定
        int task = 0;
        result.add(member);
        result.add(file);
        result.add(announcement);
        result.add(task);

        return result;
    }

}
