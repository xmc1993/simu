package cn.superid.webapp.service.impl;

import cn.superid.webapp.dao.IAffairDao;
import cn.superid.webapp.dao.IAllianceDao;
import cn.superid.webapp.dao.impl.SQLDao;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.jpa.util.StringUtil;
import cn.superid.utils.PingYinUtil;
import cn.superid.webapp.controller.VO.AffairOverviewVO;
import cn.superid.webapp.controller.forms.AffairInfo;
import cn.superid.webapp.enums.IntBoolean;
import cn.superid.webapp.enums.ResponseCode;
import cn.superid.webapp.enums.SuperIdNumber;
import cn.superid.webapp.enums.state.AffairMoveState;
import cn.superid.webapp.enums.state.TaskState;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.enums.type.PublicType;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.security.AffairPermissions;
import cn.superid.webapp.service.*;
import cn.superid.webapp.service.forms.ModifyAffairInfoForm;
import cn.superid.webapp.service.forms.SimpleRoleForm;
import cn.superid.webapp.service.vo.AffairTreeVO;
import cn.superid.webapp.service.vo.GetRoleVO;
import cn.superid.webapp.utils.TimeUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    private IAffairUserService affairUserService;
    @Autowired
    private IAffairDao affairDao;
    @Autowired
    private IAllianceDao allianceDao;


    @Override
    public String getPermissions(String permissions, int permissionLevel, long affairId) throws Exception {
        /*
        if (StringUtil.isEmpty(permissions)) {
            if ((permissionLevel > 0) || (permissionLevel < 6)) {
                Iterator it = AffairPermissionRoleType.roles.keySet().iterator();
                while (it.hasNext()) {
                    Integer key = (Integer) it.next();
                    if (key.intValue() == permissionLevel) {
                        return AffairPermissionRoleType.roles.get(key);
                    }
                }
            }
            throw new Exception("error permission");
        } else {
            return permissions;
        }
        */
        return permissions;
    }

    private void adjustOrder(long parentId, int index, long allianceId) {
        AffairEntity.execute(" update affair set number = number +1 where alliance_id = ? and parent_id = ? and number>= ?", new ParameterBindings(allianceId, parentId, index));//调整index顺序
        //TODO 如果redis缓存,需要更新缓存
    }

    private void saveAffair(AffairEntity affairEntity) {
        long aid = AffairEntity.dao.getDRDSAutoId();
        affairEntity.setId(aid);
        String superId = StringUtil.generateId(aid, SuperIdNumber.AFFAIR_SUPERID);
        affairEntity.setSuperid(superId);
        affairEntity.save();
    }

    @Override
    @Transactional
    public AffairInfo createAffair(CreateAffairForm createAffairForm) throws Exception {
        long parentAffairId = createAffairForm.getParentAffairId();
        long allianceId = createAffairForm.getAllianceId();
        AffairEntity parentAffair = AffairEntity.dao.findById(parentAffairId, allianceId);
        if (parentAffair == null) {
            throw new Exception("parent affair not found ");
        }
        AffairEntity affairEntity = convert(parentAffair, createAffairForm);

        long aid = AffairEntity.dao.getDRDSAutoId();
        affairEntity.setId(aid);
        String superId = StringUtil.generateId(aid, SuperIdNumber.AFFAIR_SUPERID);
        affairEntity.setSuperid(superId);
        affairEntity.save();

        long folderId = fileService.createRootFolderForAffair(allianceId, affairEntity.getId(), createAffairForm.getOperationRoleId());
        affairEntity.setFolderId(folderId);
        AffairEntity.dao.partitionId(allianceId).id(affairEntity.getId()).set("folderId", folderId);

        AffairMemberEntity member = affairMemberService.addCreator(allianceId, affairEntity.getId(), userService.currentUserId(), createAffairForm.getOperationRoleId());//作为创建者

        return getAffairInfo(allianceId,affairEntity.getId(),0L);
    }

    /**
     * 创建根事务,一般根据盟名称产生,一个盟对应一个根事务
     *
     * @param allianceId
     * @param name
     * @param roleId
     * @param type
     * @return
     */
    @Override
    public AffairEntity createRootAffair(long allianceId, long userId, String name, long roleId, int type, String logo) {

        AffairEntity affairEntity = new AffairEntity();
        affairEntity.setType(type);
        affairEntity.setPublicType(PublicType.TO_ALLIANCE);
        affairEntity.setOwnerRoleId(roleId);
        affairEntity.setAllianceId(allianceId);
        affairEntity.setName(name);
        affairEntity.setShortName(logo != null ? logo : name);
        affairEntity.setLevel(1);
        affairEntity.setPathIndex(1);
        affairEntity.setOwnerRoleId(roleId);
        affairEntity.setPath("/" + affairEntity.getPathIndex());
        affairEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        saveAffair(affairEntity);

        long folderId = fileService.createRootFolderForAffair(allianceId, affairEntity.getId(), roleId);
        affairEntity.setFolderId(folderId);
        AffairEntity.dao.partitionId(allianceId).id(affairEntity.getId()).set("folderId", folderId);
        try {
            affairMemberService.addCreator(affairEntity.getAllianceId(), affairEntity.getId(), userId, roleId);//加入根事务
            //在affair_user表中记录默认角色
        } catch (Exception e) {
            e.printStackTrace();
        }
        return affairEntity;
    }

    @Override
    public List<AffairEntity> getAffairByState(long allianceId, int state) {
        //TODO 参数未定,到时候看前端需要什么数据
        List<AffairEntity> result = AffairEntity.dao.partitionId(allianceId).state(state).selectList();
        return result;
    }

    @Override
    public List<AffairEntity> getAllDirectChildAffair(long allianceId, long affairId) throws Exception {
        boolean isExist = AffairEntity.dao.id(affairId).partitionId(allianceId).exists();
        if (!isExist) {
            throw new Exception("找不到该事务");
        }
        //不包含自己
        List<AffairEntity> result = AffairEntity.dao.partitionId(allianceId).
                eq("parentId", affairId).selectList("id", "allianceId", "name", "level", "path");

        return result;
    }


    @Override
    @Transactional
    public boolean disableAffair(Long allianceId, Long affairId) {
        int isUpdate = AffairEntity.dao.id(affairId).partitionId(allianceId).set("state", ValidState.Invalid);
        if (isUpdate == 0) {
            return false;
        }

        List<TaskEntity> tasks = taskService.getAllValidTask(allianceId, affairId, "id");

        //失效所有子事务
        List<AffairEntity> childAffairs = getAllChildAffairs(allianceId, affairId, "id");
        long id;
        //表示ids的下标
        int index = 0;
        int updateCount;
        if ((childAffairs != null) && (childAffairs.size() != 0)) {
            //id的数组,为了避免多次访问数据库,使用in方法
            Object[] affairIds = new Object[childAffairs.size()];
            for (AffairEntity affairEntity : childAffairs) {
                id = affairEntity.getId();
                // 避免多次访问数据库
                // AffairEntity.dao.partitionId(allianceId).id(id).set("state",ValidState.Invalid);
                //每个子事务下的task
                tasks.addAll(taskService.getAllValidTask(allianceId, id, "id"));
                affairIds[index] = id;
                index++;
            }

            updateCount = AffairEntity.dao.partitionId(allianceId).in("id", affairIds).set("state", ValidState.Invalid);
            if (updateCount != childAffairs.size()) {
                //TODO 事务回滚
                return false;
            }
        }

        if ((tasks.size() != 0) && (tasks != null)) {
            //关闭所有任务,
            Object[] taskIds = new Object[tasks.size()];
            index = 0;
            for (TaskEntity taskEntity : tasks) {
                id = taskEntity.getId();
                taskIds[index] = id;
                //同样避免多次访问数据库
                //TaskEntity.dao.partitionId(allianceId).id(id).set("state", TaskState.ErrorExit);
            }

            updateCount = TaskEntity.dao.partitionId(allianceId).in("id", taskIds).set("state", TaskState.ErrorExit);
            if (updateCount != tasks.size()) {
                return false;
            }
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
    public boolean enableAffair(long allianceId, long affairId) throws Exception {

        AffairEntity affairEntity = AffairEntity.dao.id(affairId).partitionId(allianceId).selectOne("path");
        if (affairEntity == null) {
            return false;
        }
        String basePath = affairEntity.getPath();

        return AffairEntity.dao.partitionId(allianceId).lk("path", basePath + "%").set("state", ValidState.Valid) > 0;
    }

    @Override
    public int canGenerateAffair(long allianceId, long affairId) {
        boolean isExist = AffairEntity.dao.partitionId(allianceId).id(affairId).exists();
        if (!isExist) {
            return ResponseCode.AFFAIR_INVALID;
        }
        boolean hasChild = AffairEntity.dao.partitionId(allianceId).eq("parentId", affairId).exists();
        if (hasChild) {
            return ResponseCode.HAS_CHILD;
        }

        //TODO 检测交易表里有没有affairId是本事务的交易,return
        if (1 == 2) {
            return ResponseCode.HAS_TRADE;
        }
        return ResponseCode.OK;
    }

    private boolean hasPermission(String permissions, int toFindPermission) {
        //JZY Warining : tms,注意边界值判断,你代码没加下面这个*号判断
        //收到
        if (permissions.equals("*")) {
            return true;
        }
        String[] permission = permissions.split(",");
        for (String str : permission) {
            if (str.equals(String.valueOf(toFindPermission))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int moveAffair(long allianceId, long affairId, long targetAffairId, long roleId) throws Exception {
        AffairMemberEntity affairMemberEntity = affairMemberService.getAffairMemberInfo(allianceId, targetAffairId, roleId);
        if (affairMemberEntity == null) {
            //TODO 发给所有有权限接受事务的角色通知

            return AffairMoveState.WAITING;
        }
        String permissions = affairMemberEntity.getPermissions();
        boolean hasMovePermission = hasPermission(permissions, AffairPermissions.ACCEPT_MOVED_AFFAIR);
        if (!hasMovePermission) {
            //TODO 发给所有有权限接受事务的角色通知
            return AffairMoveState.WAITING;
        }

        return shiftAffair(allianceId, affairId, targetAffairId);

    }

    @Override
    public boolean handleMoveAffair(long allianceId, long affairId, long targetAffairId, long roleId, boolean isAgree) {
        if (isAgree == true) {
            //同意了移动事务请求
            shiftAffair(allianceId, affairId, targetAffairId);
            //TODO 给发起人发通知  告知已被同意

        } else {
            //拒绝了请求
            //TODO 给发起人发起通知告知已被拒绝
        }
        return true;
    }

    public boolean modifyAffairInfo(long allianceId, long affairId, ModifyAffairInfoForm modifyAffairInfoForm) {
        Integer isHomepage = modifyAffairInfoForm.getIsHomepage();
        Integer isStuck = modifyAffairInfoForm.getIsStuck();
        modifyAffairInfoForm.setIsHomepage(null);
        modifyAffairInfoForm.setIsStuck(null);
        modifyAffairInfoForm.setNameAbbr(PingYinUtil.getFirstSpell(modifyAffairInfoForm.getName()));
        int isUpdate = AffairEntity.dao.partitionId(allianceId).id(affairId).setByObject(modifyAffairInfoForm);
        int userUpdate = 1;
        if ((isHomepage != null) && (isHomepage == IntBoolean.TRUE)) {
            userUpdate = UserEntity.dao.id(userService.currentUserId()).set("homepageAffairId", affairId);
        }
        int update = 1;
        if (isStuck != null) {
            update = AffairUserEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("userId", userService.currentUserId()).set("isStuck", isStuck);
        }
        return ((isUpdate > 0) && (userUpdate > 0) && (update > 0));
    }

    @Override
    public List<AffairEntity> getAllChildAffairs(long allianceId, long affairId, String... params) {
        return affairDao.getChildAffairs(allianceId, affairId, params);
    }

    @Override
    public boolean updateCovers(long allianceId, long affairId, String urls) {
        return AffairEntity.dao.id(affairId).partitionId(allianceId).set("covers", urls)>0;
    }

    @Override
    public boolean updateTags(long allianceId, long affairId, String tags) {
        return AffairEntity.dao.id(affairId).partitionId(allianceId).set("tag", tags)>0;
    }


    @Override
    public List<SimpleRoleForm> getAllRoles(long allianceId, long affairId) {
        List<SimpleRoleForm> result = new ArrayList<>();
        //第一步,查本盟中的affairmember,防止跨库join
        List<GetRoleVO> selfMember = affairDao.getOfficials(affairId,allianceId);
        for (GetRoleVO g : selfMember) {
            UserBaseInfo user = UserBaseInfo.dao.findById(g.getUserId());
            SimpleRoleForm s = new SimpleRoleForm(g.getRoleId(), user.getUsername(), g.getTitle(), g.getPermissions(), g.getAffairId(), g.getAffairName());
            result.add(s);
        }
        //第二步,把非本盟的官方加入
        List<AffairMemberEntity> otherMember = affairMemberService.getAffairGuestMembers(allianceId, affairId);
        for (AffairMemberEntity a : otherMember) {
            RoleCache role = RoleCache.dao.findById(a.getRoleId());
            UserBaseInfo user = UserBaseInfo.dao.findById(role.getUserId());
            SimpleRoleForm s = new SimpleRoleForm(a.getRoleId(), user.getUsername(), role.getTitle(), a.getPermissions(), -1, "");
            result.add(s);
        }
        return result;
    }

    @Override
    public AffairOverviewVO affairOverview(long allianceId, long affairId) {
        int member = affairMemberService.countAffairMember(allianceId, affairId,null);
        int file = FileEntity.dao.partitionId(allianceId).eq("affair_id", affairId).count();
        int announcement = AnnouncementEntity.dao.partitionId(allianceId).eq("affair_id", affairId).count();
        //TODO:事务这块待确定
        int task = 0;
        AffairOverviewVO vo = new AffairOverviewVO();
        vo.setMembers(member);
        vo.setAnnouncements(announcement);
        vo.setFiles(file);
        vo.setTasks(task);
        return vo;
    }

    @Override
    public boolean isChildAffair(long allianceId, long childAffairId, long parentAffairId) {
        AffairEntity childAffairEntity = AffairEntity.dao.id(childAffairId).partitionId(allianceId).selectOne("level", "path", "parentId");
        if (childAffairEntity == null)
            return false;
        if (childAffairEntity.getParentId() == parentAffairId) {//WARN 狗日的TMS一般情况只会比较相邻父子节点,注意优化
            return true;
        }
        //获取待比较两个事务的level
        AffairEntity parentAffairEntity = AffairEntity.dao.id(parentAffairId).partitionId(allianceId).selectOne("level", "path");
        int parentLevel = parentAffairEntity.getLevel();
        int childLevel = childAffairEntity.getLevel();
        //level同级或者比父事务小肯定不是
        if (childLevel <= parentLevel)
            return false;

        //取父事务的path,与子事务的前缀path逐一比较,完全相同则为父事务
        String[] parentPaths = parentAffairEntity.getPath().split("-");
        String[] childPaths = childAffairEntity.getPath().split("-");
        for (int i = 0; i < parentLevel; i++) {
            if (!(parentPaths[i].equals(childPaths[i]))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public AffairTreeVO getAffairTree(long allianceId) {
        //第一步,得到当前user,然后根据他角色所在的盟,拿出所有事务,并且拿出affairMemberId来检测是否在这个事务中(这边未减少读取数据库次数,将其移入内存处理)
        UserEntity user = userService.getCurrentUser();
        List<AffairTreeVO> affairList = affairDao.getAffairTree(allianceId,user.getId());
        //生成树
        long homepageAffairId = userService.getCurrentUser().getHomepageAffairId();
        for (AffairTreeVO a : affairList) {
            if (homepageAffairId == a.getId()) {
                a.setIsIndex(true);
            } else {
                a.setIsIndex(false);
            }
        }
        AffairTreeVO result = createTree(affairList);
        if(result != null){
            if(user.getPersonalAllianceId() == allianceId){
                result.setIsPersonal(true);
            }
        }
        return result;
    }

    @Override
    public List<AffairTreeVO> getAffairTreeByUser() {
        //第一步,得到当前user,然后根据他角色所在的盟,拿出所有事务,并且拿出affairMemberId来检测是否在这个事务中(这边未减少读取数据库次数,将其移入内存处理)
        UserEntity user = userService.getCurrentUser();
        long allianceId = user.getPersonalAllianceId();
        List<AffairTreeVO> affairList = affairDao.getAffairTreeByUser(user.getId());
        //第二步,取出所有allianceId;
        List<Long> ids = allianceDao.getAllianceIdOfUser(user.getId());

        List<AffairTreeVO> result = new ArrayList<>();
        for (Long id : ids) {
            AffairTreeVO a = createTree(getTreeByAlliance(affairList, id));
            if (a != null) {
                if(allianceId == id){
                    a.setIsPersonal(true);
                }
                result.add(a);
            }
        }
        return result;
    }

    private List<AffairTreeVO> getTreeByAlliance(List<AffairTreeVO> total, long allianceId) {
        List<AffairTreeVO> result = new ArrayList<>();
        for (AffairTreeVO a : total) {
            if (a.getAllianceId() == allianceId) {
                result.add(a);
            }
        }
        return result;
    }

    private AffairTreeVO createTree(List<AffairTreeVO> vos) {
        //第一步,把数组根据path长度排序,把叶节点排在最前面
        Collections.reverse(vos);

        //第二步,将叶节点加入其父亲节点底下
        for (int i = 0; i < vos.size(); i++) {
            long parentId = vos.get(i).getParentId();
            if (parentId != 0) {
                for (int j = i; j < vos.size(); j++) {
                    //父节点肯定在子节点后面,所以直接从子节点位置开始查
                    if (vos.get(j).getId() == parentId) {
                        vos.get(j).getChildren().add(vos.get(i));
                        break;
                    }
                }
            } else {
                //当parentId为0时,说明到根节点了
                return vos.get(i);
            }
        }

        return null;

    }

    private List<AffairTreeVO> filter(List<AffairTreeVO> list) {
        for (AffairTreeVO a : list) {
            if (a.getPublicType() == PublicType.PRIVATE & a.getRoleId() == 0) {

            }
        }
        return null;
    }

    @Override
    public AffairInfo getAffairInfo(long allianceId, long affairId, long roleId) {
        long userId = userService.currentUserId();

        AffairInfo affairInfo = new AffairInfo();

        AffairEntity affairEntity = AffairEntity.dao.findById(affairId, allianceId);
        affairInfo.setDescription(affairEntity.getDescription());
        affairInfo.setId(affairEntity.getId());
        affairInfo.setLogoUrl(affairEntity.getLogoUrl());
        affairInfo.setName(affairEntity.getName());
        affairInfo.setOwnerRoleId(affairEntity.getOwnerRoleId());
        affairInfo.setShortName(affairEntity.getShortName());
        affairInfo.setPublicType(affairEntity.getPublicType());
//        affairInfo.setIsStuck(affairEntity.getIsStuck());
        affairInfo.setSuperid(affairEntity.getSuperid());
        affairInfo.setGuestLimit(affairEntity.getGuestLimit());
        affairInfo.setModifyTime(affairEntity.getModifyTime());
        affairInfo.setTags(affairEntity.getTag());

        affairInfo.setCovers(affairEntity.getCovers());
        affairInfo.setOverView(JSON.toJSON(affairOverview(allianceId, affairId)));

        long tempRoleId = 0;
        AffairUserEntity lastOperateRole = affairUserService.getAffairUser(allianceId, affairId, userId);

        if(roleId != 0){
            //指定roleId就用roleId
            tempRoleId = roleId;
        }else if(roleId == 0 & lastOperateRole != null){
            //没有指定role且affairUser表中有,就按照表中来
            tempRoleId = lastOperateRole.getRoleId();
        }else {
            //没有affairUser的话就返回该用户在这个盟里最先创建的角色
            RoleEntity roleEntity = RoleEntity.dao.partitionId(allianceId).eq("user_id", userService.currentUserId()).asc("create_time").selectOne("id", "title");
            affairInfo.setRoleTitle(roleEntity.getTitle());
            affairInfo.setRoleId(roleEntity.getId());
            affairInfo.setIsStuck(false);
            affairInfo.setAffairMemberId(0L);
            affairInfo.setPermissions("");
            return affairInfo;
        }
        affairInfo.setRoleId(tempRoleId);
        RoleCache tempRole = RoleCache.dao.findById(tempRoleId);
        if (tempRole != null) {
            affairInfo.setRoleTitle(tempRole.getTitle());
            affairInfo.setRoleAllianceId(tempRole.getAllianceId());
        } else {
            affairInfo.setRoleTitle("");
            affairInfo.setRoleAllianceId(0L);
        }
        if(lastOperateRole != null){
            affairInfo.setIsStuck(lastOperateRole.getIsStuck());
        }else{
            affairInfo.setIsStuck(false);
        }
        AffairMemberEntity affairMemberEntity = AffairMemberEntity.dao.partitionId(allianceId).eq("role_id", tempRoleId).eq("affair_id", affairId).selectOne();
        if (affairMemberEntity != null) {
            affairInfo.setAffairMemberId(affairMemberEntity.getId());
            affairInfo.setPermissions(affairMemberEntity.getPermissions());
        } else {
            affairInfo.setAffairMemberId(0L);
            affairInfo.setPermissions("");
        }
        return affairInfo;
    }

    @Override
    public boolean switchRole(long affairId, long allianceId, long newRoleId) {
        return AffairUserEntity.dao.partitionId(allianceId).eq("affairId", affairId).eq("userId", userService.currentUserId()).set("roleId", newRoleId) > 0;
    }

    @Override
    public List<AffairInfo> getOutAllianceAffair() {
        List<AffairInfo> result;
        long userId = userService.currentUserId();
        //TODO 返回格式还没定
        //找到allianceUser里该用户的盟,然后从affairUser里找到不在之前盟中的affair,join affair表
        result = affairDao.getOutAllianceAffair(userId);
        return result;
    }

    @Override
    public boolean isValidAffair(long allianceId, long affairId) {
        return AffairEntity.dao.partitionId(allianceId).id(affairId).state(ValidState.Valid).exists();
    }

    @Override
    public boolean stickAffair(long allianceId,long affairId,boolean isStuck) {
        return AffairUserEntity.dao.partitionId(allianceId).eq("affair_id",affairId).eq("user_id",userService.currentUserId()).set("is_stuck", isStuck)>0;
    }

    @Override
    public boolean setHomepage(long affairId) {
        return UserEntity.dao.id(userService.currentUserId()).set("homepage_affair_id",affairId)>0;
    }


    private int shiftAffair(long allianceId, long affairId, long targetAffairId) {
        //检测不能在把父事务放到子事务底下
        AffairEntity targetAffair = AffairEntity.dao.partitionId(allianceId).id(targetAffairId).selectOne("level", "path");
        AffairEntity sourceAffair = AffairEntity.dao.partitionId(allianceId).id(affairId).selectOne();
        if (sourceAffair == null | targetAffair == null | targetAffair.getPath().contains(sourceAffair.getPath())) {
            return 0;
        }

        //获取目标事务的一级子事务,然后取出最大的number加上1就是待移动事务的number
        int max_number = AffairEntity.dao.partitionId(allianceId).eq("parentId", targetAffairId).count() + 1;

        //获取目标事务的层级,加到待移动的所有事务上
        int targetLevel = targetAffair.getLevel();
        String oldAffairPath = sourceAffair.getPath();
        String targetPath = targetAffair.getPath();

        //根据待移动事务的子事务的level减去待移动事务的level,差值加上目标事务的level,就是待移动事务的所有子事务的level
        //即temp-source+target+1,把target和source的差值算出来
        int sourceLevel = sourceAffair.getLevel();
        int offsetLevel = targetLevel - sourceLevel + 1;


        //需要找到的所有子事务
        List<AffairEntity> allChildAffairs = getAllChildAffairs(allianceId, affairId, "id", "level", "path");
        //子事务方法需要老的path,所以变化放在找到子事务后面
        //待移动事务本身的parentId,level,number和path
        sourceAffair.setParentId(targetAffairId);
        sourceAffair.setLevel(targetLevel + 1);
        sourceAffair.setPath(targetPath + "-" + max_number);
        sourceAffair.update();

        //改变子事务的path和level
        String basePath = sourceAffair.getPath();
        long id;
        int oldLevel;
        String oldPath, newPath;
        for (AffairEntity affairEntity : allChildAffairs) {
            id = affairEntity.getId();
            oldLevel = affairEntity.getLevel();
            oldPath = affairEntity.getPath();
            //根据父事务的新path作为前缀加上父事务的老path相减取path的后几位substring
            newPath = basePath + StringUtil.difference(oldAffairPath, oldPath);
            AffairEntity.dao.id(id).partitionId(allianceId).set("path", newPath, "level", oldLevel + offsetLevel);
        }


        return AffairMoveState.SUCCESS;

    }

    private boolean isExist(String superid) {
        return AffairEntity.dao.eq("superid", superid).exists();
    }

    private AffairEntity convert(AffairEntity parentAffair, CreateAffairForm form) {
        long parentAffairId = form.getParentAffairId();
        long allianceId = form.getAllianceId();
        int count = AffairEntity.dao.eq("parentId", parentAffairId).partitionId(allianceId).count();//已有数目
        AffairEntity affairEntity = new AffairEntity();
        affairEntity.setParentId(parentAffairId);
        affairEntity.setShortName(form.getLogo() != null ? form.getLogo() : form.getName());
        affairEntity.setNameAbbr(PingYinUtil.getFirstSpell(form.getName()));
        affairEntity.setOwnerRoleId(form.getOperationRoleId());
        affairEntity.setState(ValidState.Valid);
        affairEntity.setPublicType(form.getPublicType());
        affairEntity.setAllianceId(parentAffair.getAllianceId());
        affairEntity.setType(parentAffair.getType());
        affairEntity.setDescription(form.getDescription() != null ? form.getDescription() : "");
        affairEntity.setName(form.getName());
        affairEntity.setLevel(parentAffair.getLevel() + 1);
        affairEntity.setPathIndex(count + 1);
        affairEntity.setPath(parentAffair.getPath() + '-' + affairEntity.getPathIndex());
        affairEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        affairEntity.setModifyTime(affairEntity.getCreateTime());
        return affairEntity;
    }

}
