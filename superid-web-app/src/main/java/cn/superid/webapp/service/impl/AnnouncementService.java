package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.SQLDao;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.*;
import cn.superid.webapp.controller.forms.*;
import cn.superid.webapp.dao.impl.IAnnouncementDao;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.*;
import cn.superid.webapp.model.cache.RoleCache;
import cn.superid.webapp.model.cache.UserBaseInfo;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.forms.*;
import cn.superid.webapp.utils.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jizhenya on 16/9/26.
 */
@Service
public class AnnouncementService implements IAnnouncementService{
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAnnouncementDao announcementDao;

    private static final int THUMB_LENTH = 200 ;
    private static final int SNAPSHOT_INTERVAL = 30 ;

    @Override
    public EditDistanceForm compareTwoBlocks(List<TotalBlock> present, List<TotalBlock> history) {
        List<Integer> delete = new ArrayList<>();
        List<InsertForm> insert = new ArrayList<>();
        List<ReplaceForm> replace = new ArrayList<>();
        EditDistanceForm result = new EditDistanceForm();

        //如果被减数为空,则回到过去,每段都是插入
        if(present.size() == 0){
            insert.add(new InsertForm(0,history));
            result.setDelete(delete);
            result.setInsert(insert);
            result.setReplace(replace);
            return result;
        }

        //如果被减数为空,则每段都是删除
        if(history.size() == 0){
            for(int i = 0 ; i < present.size() ; i++){
                delete.add(i+1);
            }
            result.setDelete(delete);
            result.setInsert(insert);
            result.setReplace(replace);
            return result;
        }

        //如果都不为空,需要矩阵比较
        int m = present.size();
        int n = history.size();
        int[][] matrix = new int[m+1][n+1];
        TotalBlock b1,b2;


        //初始化
        for(int i = 0 ; i <= m ; i ++){
            matrix[i][0] = i;
        }
        for(int j = 0 ; j <= n ; j++){
            matrix[0][j] = j;
        }
        for(int i = 1 ; i <= m ; i++){
            TotalBlock p = present.get(i-1);
            for(int j = 1 ; j <= n ; j++){
                int temp = 0 ;
                TotalBlock h = history.get(j-1);
                if(p.getKey().equals(h.getKey()) && p.getText().equals(h.getText())){
                    //两者相同,不增加距离
                    temp = 0;
                }else{
                    temp = 1;
                }

                int min = matrix[i-1][j]+1;
                if(min > (matrix[i][j-1] + 1)){
                    min = matrix[i][j-1] + 1;
                }
                if(min > (matrix[i-1][j-1]+temp)){
                    min = matrix[i-1][j-1]+temp;
                }
                matrix[i][j] = min;
            }
        }

        //矩阵生成完毕,现在需要根据matrix[i][j]的值逆推得到操作过程
        int x = n , y = m;
        int count = 0 ;
        while(count < matrix[m][n]){
            TotalBlock p = present.get(y-1);
            TotalBlock h = history.get(x-1);
            int temp = 0;
            if(p.getKey().equals(h.getKey()) && p.getText().equals(h.getText())){
                temp = 0;
            }else{
                temp = 1;
            }

            int which = 0;
            int min = matrix[y-1][x]+1;
            if(min > (matrix[y][x-1] + 1)){
                min = matrix[y][x-1] + 1;
                which = 1;
            }
            if(min > (matrix[y-1][x-1]+temp)){
                min = matrix[y-1][x-1]+temp;
                which = 2;
            }
            switch (which){
                case 0:
                    //从上方变化而来,比原来多一步删除操作
                    delete.add(y);
                    y = y - 1;
                    count++;
                    break;
                case 1:
                    //从左边变化来,比原来多一步增加操作
                    List<TotalBlock> one = new ArrayList<>();
                    int location = 0;
                    for(int i = 0 ; i < insert.size() ;i++){
                        InsertForm in = insert.get(i);
                        if(in.getPosition() == y){
                            one = in.getContent();
                            location = i;
                            break;
                        }
                    }
                    one.add(0,history.get(x));
                    if(location != 0){
                        insert.set(location,new InsertForm(y,one));
                    }else{
                        insert.add(new InsertForm(y,one));
                    }
                    x = x - 1;
                    count++;
                    break;
                case 2:
                    if(temp == 1){
                        //说明进行了一步替换
                        replace.add(new ReplaceForm(y,history.get(x)));
                        x = x - 1;
                        y = y - 1;
                        count++;
                    }else{
                        //没变说明未进行操作
                        x = x - 1;
                        y = y - 1;
                    }
                    break;
            }
        }

        result.setDelete(delete);
        result.setInsert(insert);
        result.setReplace(replace);
        return result;
    }

    @Override
    public EditDistanceForm compareTwoPapers(ContentState present, ContentState history) {
        return compareTwoBlocks(present.getBlocks(),history.getBlocks());
    }

    public String caulatePaper(String content , String operations){

        ContentState total = JSON.parseObject(content,ContentState.class);
        List<TotalBlock> blocks = total.getBlocks();

        EditDistanceForm ope = JSON.parseObject(operations,EditDistanceForm.class);

        //该方法处理逻辑:先处理替换,再处理删除,但不真正执行,只是记录下要删除的key,防止打乱add的location,然后执行add方法,最后执行delete
        for(ReplaceForm r : ope.getReplace()){
            if(blocks.size()<r.getPosition()){
                if(r.getPosition() == 1){
                    //直接插入
                    blocks.add(r.getContent());
                }
            }else{
                //完成替换
                blocks.set(r.getPosition()-1,r.getContent());
            }
        }
        List<TotalBlock> deletes = new ArrayList<>();
        //得到要删除的key
        for(Integer i : ope.getDelete()){
            deletes.add(blocks.get(i-1));
        }

        List<TotalBlock> adds = new ArrayList<>();
        //因为插入也会使location变动,所以先记录是跟在谁后面
        for(InsertForm insert : ope.getInsert()){
            if(insert.getPosition() == 0){
                adds.add(null);
            }else{
                adds.add(blocks.get(insert.getPosition()-1));
            }

        }

        //执行insert,因为和上面次序一致,所以可以直接用
        for(int i = 0 ; i < ope.getInsert().size() ; i++){
            InsertForm insert = ope.getInsert().get(i);
            List<TotalBlock> bs = insert.getContent();
            if(adds.get(i) == null){
                //表示是在开头插入
                blocks.addAll(0,bs);
            }else{
                int location = blocks.indexOf(adds.get(i))+1;
                blocks.addAll(location,bs);
            }


        }

        //第三步,执行删除
        blocks.removeAll(deletes);

        total.setBlocks(blocks);

        return JSONObject.toJSONString(total);

    }

    @Override
    public List<Block> getBlock(ContentState content) {
        List<Block> result = new ArrayList<>();
        int i = 0;
        for(TotalBlock t : content.getBlocks()){
            result.add(new Block(t.getKey(),t.getText(),i));
            i++;
        }

        return result;
    }

    @Override
    public boolean save(ContentState contentState, long announcementId, long allianceId , long roleId) {
        RoleCache role = RoleCache.dao.findById(roleId);
        AnnouncementEntity announcementEntity = AnnouncementEntity.dao.findById(announcementId,allianceId);
        if(announcementEntity == null){
            return false;
        }
        //第一步,更新最近一条历史记录的increment
        ContentState old = JSON.parseObject(announcementEntity.getContent(),ContentState.class);
        int result = AnnouncementHistoryEntity.dao.partitionId(allianceId).eq("announcementId",announcementId).eq("version",announcementEntity.getVersion()).set("increment",compareTwoPapers(old,contentState));

        //第二步,改变原有记录
        announcementEntity.setVersion(announcementEntity.getVersion()+1);
        announcementEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setModifierId(roleId);
        announcementEntity.setModifierUserId(role.getUserId());
        announcementEntity.setDecrement(JSONObject.toJSONString(compareTwoPapers(contentState,old)));
        announcementEntity.setContent(JSONObject.toJSONString(contentState));
        announcementEntity.update();

        //第三步,把这条新记录存在历史记录里
        AnnouncementHistoryEntity history = new AnnouncementHistoryEntity(announcementEntity);
        history.setEntityMap(JSONObject.toJSONString(contentState.getEntityMap()));
        history.save();


        //如果满足记录条件,就存一条快照
        if(announcementEntity.getVersion()%SNAPSHOT_INTERVAL == 0){
            //如果是三十的倍数
            generateSnapshot(announcementId,announcementEntity.getVersion(),announcementEntity.getContent(),allianceId,history.getId());
        }
        return result>0;
    }


    @Override
    public boolean createAnnouncement(String title, long affairId, long allianceId, long taskId, long roleId, int isTop, int publicType, ContentState content) {
        RoleCache role = RoleCache.dao.findById(roleId);

        //第一步在announcement表中生成一条记录
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        announcementEntity.setTitle(title);
        announcementEntity.setContent(JSONObject.toJSONString(content));
        announcementEntity.setAffairId(affairId);
        announcementEntity.setTaskId(taskId);
        announcementEntity.setModifierId(roleId);
        announcementEntity.setThumbContent(getThumb(getBlock(content)));
        announcementEntity.setIsTop(isTop);
        announcementEntity.setPublicType(publicType);
        announcementEntity.setState(0);
        announcementEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setVersion(1);
        announcementEntity.setAllianceId(allianceId);
        announcementEntity.setSessionSum(0);
        announcementEntity.setModifierUserId(role.getUserId());
        //生成从第一版本到零的delta,都是删除操作,它的逆操作可以用来从底下往上推
        ContentState empty = new ContentState();
        EditDistanceForm decrement = compareTwoPapers(content,empty);
        announcementEntity.setDecrement(JSONObject.toJSONString(decrement));
        announcementEntity.save();

        //第二步,在历史表中生成一条记录
        AnnouncementHistoryEntity history = new AnnouncementHistoryEntity(announcementEntity);
        history.setEntityMap(JSONObject.toJSONString(content.getEntityMap()));
        history.save();

        return true;
    }

    @Override
    public boolean deleteAnnouncement(long announcementId, long allianceId, long roleId) {
        RoleCache role = RoleCache.dao.findById(roleId);
        //第一步,公告表置为失效
        AnnouncementEntity announcementEntity = AnnouncementEntity.dao.id(announcementId).partitionId(allianceId).selectOne();
        if(announcementEntity == null){
            return false;
        }
        announcementEntity.setModifierId(roleId);
        announcementEntity.setModifierUserId(role.getUserId());
        announcementEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setState(ValidState.Invalid);
        announcementEntity.update();

        //第二步,把history表中最上面一条状态置为0
        AnnouncementHistoryEntity history = AnnouncementHistoryEntity.dao.partitionId(allianceId).eq("version",announcementEntity.getVersion()).eq("announcement_id",announcementId).selectOne();
        if(history == null){
            return false;
        }
        history.setState(ValidState.Invalid);
        history.setModifyTime(announcementEntity.getModifyTime());
        history.setModifierId(roleId);
        history.setModifierUserId(role.getUserId());
        history.update();

        return true;
    }

    @Override
    public List<SimpleAnnouncementIdVO> getIdByAffair(long affairId, long allianceId , boolean isContainChild) {
        List<SimpleAnnouncementIdVO> simpleAnnouncementIdVOList = announcementDao.getIdByAffair(affairId,allianceId,isContainChild);
        //n复杂度排序
        List<SimpleAnnouncementIdVO> result = new ArrayList<>();
        //记录下置顶的插入到第几位
        int location = 0 ;
        for(int i = 0 ; i < simpleAnnouncementIdVOList.size() ; i++){
            if(simpleAnnouncementIdVOList.get(i).getIsTop() == 0){
                result.add(simpleAnnouncementIdVOList.get(i));
            }else{
                result.add(location,simpleAnnouncementIdVOList.get(i));
                location++;
            }
        }

        return simpleAnnouncementIdVOList;
    }

    @Override
    public List<SimpleAnnouncementVO> getOverview(String ids,  long allianceId) {
        List<SimpleAnnouncementVO> result = announcementDao.getOverview(ids,allianceId);
        if(result == null ){
            return null;
        }
        //显示现在的扮演人
//        for(SimpleAnnouncementVO s : result){
//            UserNameAndRoleNameVO name = roleService.getUserNameAndRoleName(s.getCreatorId());
//            s.setRoleTitle(name.getRoleTitle());
//            s.setRealname(name.getUserName());
//            s.setAvatar(name.getAvatar());
//        }
        //显示以前的扮演人
        for(SimpleAnnouncementVO s : result){
            RoleCache role = RoleCache.dao.findById(s.getCreatorId());
            UserBaseInfo user = UserBaseInfo.dao.findById(s.getCreatorUserId());
            s.setRoleName(role.getTitle());
            s.setUsername(user.getUsername());
            s.setAvatar(user.getAvatar());
        }

        return result;
    }

    @Override
    public AnnouncementEntity getDetail(long announcementId, long allianceId) {
        AnnouncementEntity result = AnnouncementEntity.dao.findById(announcementId,allianceId);
        if(result != null){
//            //显示最新的user
//            UserNameAndRoleNameVO userNameAndRoleNameVO = roleService.getUserNameAndRoleName(result.getModifierId());
//            if(userNameAndRoleNameVO != null){
//                result.setRoleTitle(userNameAndRoleNameVO.getRoleTitle());
//                result.setRealname(userNameAndRoleNameVO.getUserName());
//                result.setAvatar(userNameAndRoleNameVO.getAvatar());
//            }
            //显示老user
            RoleCache role = RoleCache.dao.findById(result.getModifierId());
            UserBaseInfo user = UserBaseInfo.dao.findById(result.getModifierUserId());
            result.setAvatar(user.getAvatar());
            result.setRoleName(role.getTitle());
            result.setUsername(user.getUsername());
        }
        return result;
    }

    @Override
    public Map<String, Object> getDetails(long announcementId, int offsetHead, int offsetTail, int version, long allianceId) {
        List<EditDistanceForm> operations = new ArrayList<>();
        List<String> entityMaps = new ArrayList<>();

        AnnouncementEntity announcement = AnnouncementEntity.dao.findById(announcementId,allianceId);
        if(announcement == null){
            return null;
        }
        String present = announcement.getContent();
        String content = present;
        //以下用于得到首要的版本的contentState
        if(version < 1 | version > announcement.getVersion()){ version = announcement.getVersion();}
        if(version != announcement.getVersion()){
            List<AnnouncementHistoryEntity> hs = AnnouncementHistoryEntity.dao.partitionId(allianceId).eq("announcement_id",announcementId).gt("version",version).desc("version").selectList();
            content = present;
            for(AnnouncementHistoryEntity h : hs){
                content = caulatePaper(content,h.getDecrement());
            }
        }

        //处理取前几位
        int upper = version+offsetHead;
        int over = upper-announcement.getVersion();

        if(over > 0){
            //表示请求的越过上限,则在开头填上相同位数的null
            for(int i = 0 ; i < over ; i++){
                operations.add(null);
                entityMaps.add(null);
            }

        }
        List<AnnouncementHistoryEntity> histories = AnnouncementHistoryEntity.dao.partitionId(allianceId).eq("announcement_id",announcementId).gt("version",version-1).lt("version",upper).asc("version").selectList();
        if(histories != null){
            for(AnnouncementHistoryEntity a : histories){
                //如果是最上面一条,则跳过,因为它没有Increament
                if(a.getVersion() == announcement.getVersion()){
                    continue;
                }
                EditDistanceForm e = JSON.parseObject(a.getIncrement(),EditDistanceForm.class);
                operations.add(e);
                entityMaps.add(a.getEntityMap());
            }
        }


        //处理取后几位
        int lower = version-offsetTail;
        List<AnnouncementHistoryEntity> lowHistories = AnnouncementHistoryEntity.dao.partitionId(allianceId).eq("announcement_id",announcementId).lt("version",version+1).gt("version",lower).desc("version").selectList();
        for(AnnouncementHistoryEntity a : lowHistories){
            EditDistanceForm e = JSON.parseObject(a.getDecrement(),EditDistanceForm.class);
            operations.add(e);
            entityMaps.add(a.getEntityMap());
        }
        if(lower < 0){
            for(int i = lower ; i < 0 ; i++){
                operations.add(null);
                entityMaps.add(null);
            }
        }
        AnnouncementForm result = new AnnouncementForm();
        result.setId(announcement.getId());
        result.setCreateTime(announcement.getModifyTime());
        result.setCreatorId(announcement.getModifierId());
        result.setState(announcement.getState());
        //组织返回结果
        AnnouncementHistoryEntity h = AnnouncementHistoryEntity.dao.partitionId(allianceId).eq("announcement_id",announcementId).eq("version",version).selectOne();
        RoleCache role = RoleCache.dao.findById(announcement.getModifierId());
        UserBaseInfo user = UserBaseInfo.dao.findById(announcement.getModifierUserId());
        if(h != null){
            result.setTitle(h.getTitle());
            result.setModifierId(h.getModifierId());
            result.setIsTop(h.getIsTop());
            result.setPublicType(h.getPublicType());
            result.setModifyTime(h.getCreateTime());
            result.setAvatar(user.getAvatar());
            result.setRoleName(role.getTitle());
            result.setUsername(user.getUsername());
            //这边得替换entityMap
            ContentState c = JSON.parseObject(content,ContentState.class);
            c.setEntityMap(JSON.parseObject(h.getEntityMap(), Object.class));
            result.setContent(JSONObject.toJSONString(c));
        }

        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("announcement", result);
        rsMap.put("history",operations);
        rsMap.put("entityMaps",entityMaps);
        return rsMap;
    }

    @Override
    public List<SimpleAnnouncementIdVO> searchAnnouncement(String content, Long affairId, Long allianceId, boolean containChild) {
        StringBuilder sql = null;
        ParameterBindings p = new ParameterBindings();
        AffairEntity affairEntity = AffairEntity.dao.findById(affairId,allianceId);
        if(affairEntity == null){
            return null;
        }


        if(containChild == true){
            sql = new StringBuilder(SQLDao.SEARCH_ANNOUNCEMENT_CONTAIN_CHILD);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affairEntity.getPath()+"%");
            p.addIndexBinding("%"+content+"%");
            p.addIndexBinding(content);
            p.addIndexBinding(content);
        }else{
            sql = new StringBuilder(SQLDao.SEARCH_ANNOUNCEMENT);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affairId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding("%"+content+"%");
            p.addIndexBinding(content);
            p.addIndexBinding(content);
        }


        return AnnouncementEntity.getSession().findListByNativeSql(SimpleAnnouncementIdVO.class,sql.toString(),p);
    }

    @Override
    public List<SimpleDraftIdVO> getDraftByAffair(long affairId, long allianceId, long roleId) {
        return announcementDao.getDraftByAffair(affairId,allianceId,roleId);
    }

    @Override
    public DraftDetailVO getDraftDetail(long draftId) {
        AnnouncementDraftEntity announcementDraftEntity = AnnouncementDraftEntity.dao.id(draftId).selectOne("content","title","public_type","edit_mode");
        DraftDetailVO result = new DraftDetailVO();
        if(announcementDraftEntity != null){
            result.setContent(announcementDraftEntity.getContent());
            result.setTitle(announcementDraftEntity.getTitle());
            result.setPublicType(announcementDraftEntity.getPublicType());
            result.setEditMode(announcementDraftEntity.getEditMode());
        }

        return result;
    }

    @Override
    public boolean deleteDraft(long draftId , long allianceId) {
        int result = AnnouncementDraftEntity.dao.partitionId(allianceId).id(draftId).remove();
        if(result < 1){
            return false;
        }else{
            return true;
        }

    }

    @Override
    public long saveDraft(String delta, long draftId, long allianceId, long affairId, long roleId, int publicType, String title, long taskId, String entityMap, int editMode) {

        AnnouncementDraftEntity announcementDraftEntity = null ;
        ContentState contentState = null;
        if(draftId == 0){
            //新建的草稿
            announcementDraftEntity = new AnnouncementDraftEntity();
            contentState = new ContentState();
        }else{
            announcementDraftEntity = AnnouncementDraftEntity.dao.findById(draftId,allianceId);
            contentState = JSON.parseObject(announcementDraftEntity.getContent(),ContentState.class);
        }
        if(announcementDraftEntity == null | contentState == null){
            return 0L;
        }
        if(!entityMap.equals("")){
            contentState.setEntityMap(entityMap);
        }
        announcementDraftEntity.setContent(caulatePaper(JSONObject.toJSONString(contentState),delta));
        announcementDraftEntity.setThumbContent(getThumb(getBlock(JSON.parseObject(announcementDraftEntity.getContent(),ContentState.class))));
        announcementDraftEntity.setTaskId(taskId);
        announcementDraftEntity.setAffairId(affairId);
        announcementDraftEntity.setAllianceId(allianceId);
        announcementDraftEntity.setState(ValidState.Valid);
        announcementDraftEntity.setCreatorId(roleId);
        announcementDraftEntity.setAllianceId(allianceId);
        announcementDraftEntity.setEditMode(editMode);

        announcementDraftEntity.setTitle(title);
        if(draftId == 0){
            announcementDraftEntity.save();
        }else{
            announcementDraftEntity.update();
        }
        return announcementDraftEntity.getId();
    }

    @Override
    public List<SimpleAnnouncementHistoryVO> getHistoryOverview(long affairId, long allianceId, int count, Timestamp time) {
        StringBuilder sql = new StringBuilder(SQLDao.GET_ANNOUNCEMENT_HISTORY_LIST);
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        p.addIndexBinding(time);
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        p.addIndexBinding(time);
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        return AnnouncementEntity.getSession().findListByNativeSql(SimpleAnnouncementHistoryVO.class,sql.toString(),p);
    }

    @Override
    public SimpleAnnouncementVO getHistoryVersion(long announcementId, int version, long allianceId) {
        AnnouncementEntity announcementEntity = AnnouncementEntity.dao.findById(announcementId,allianceId);
        SimpleAnnouncementVO result = new SimpleAnnouncementVO();
        if(announcementEntity == null){
            return null;
        }
        int totalVersion = announcementEntity.getVersion();
        if(version > totalVersion){
            return null;
        }
        //计算出最近的快照
        int remainder = version % SNAPSHOT_INTERVAL;
        int n = version / SNAPSHOT_INTERVAL;
        boolean isLarge = false ;
        if(remainder >= SNAPSHOT_INTERVAL/2 | (remainder < SNAPSHOT_INTERVAL/2 & n == 0)){
            n++;
            isLarge = true;
        }

        int lastVersion = n * SNAPSHOT_INTERVAL;

        AnnouncementSnapshotEntity announcementSnapshotEntity = AnnouncementSnapshotEntity.dao.partitionId(allianceId).eq("announcement_id",announcementId).eq("version",lastVersion).selectOne();
        String content = null;
        List<AnnouncementHistoryEntity> histories = null;
        if(announcementSnapshotEntity != null){
            //找到了最近记录
            content = announcementSnapshotEntity.getContent();
            if(isLarge == true){
                histories = AnnouncementHistoryEntity.dao.gt("version",version-1).lt("version",lastVersion+1).partitionId(allianceId).eq("announcement_id",announcementId).desc("version").selectList();
            }else{
                histories = AnnouncementHistoryEntity.dao.lt("version",version+1).gt("version",lastVersion-1).partitionId(allianceId).eq("announcement_id",announcementId).asc("version").selectList();
            }

        }else{
            //未找到最近记录,则说明要么lastversion大于了现在最大version,要么就是0
            content = announcementEntity.getContent();
            histories = AnnouncementHistoryEntity.dao.gt("version",version-1).partitionId(allianceId).eq("announcement_id",announcementId).desc("version").selectList();

        }

        for(AnnouncementHistoryEntity history : histories){
            if(history.getVersion() == version){
                //把entityMap替换
                ContentState contentState = JSON.parseObject(content,ContentState.class);
                contentState.setEntityMap(JSON.parseObject(history.getEntityMap(),Object.class));
                result.setAffairId(history.getAffairId());
                result.setContent(JSONObject.toJSONString(contentState));
                result.setCreatorId(history.getCreatorId());
                result.setCreatorUserId(history.getCreatorUserId());
                result.setTitle(history.getTitle());
                result.setId(history.getAnnouncementId());
                break;
            }
            if(lastVersion > totalVersion){
                content = caulatePaper(content,history.getDecrement());
            }else if(isLarge == true){
                content = caulatePaper(content,history.getIncrement());
            }
        }



        return result;
    }

    private String getThumb(List<Block> blocks){
        StringBuilder result = new StringBuilder("");
        for(Block b : blocks){
            if((result.length()+b.getContent().length())>200){
                int remain = THUMB_LENTH - result.length();
                result.append(b.getContent().substring(0,remain));
            }else{
                result.append(b.getContent());
            }
        }
        return result.toString();
    }

    private boolean generateSnapshot(long announcementId , int version , String content , long allianceId , long historyId){
        AnnouncementSnapshotEntity announcementSnapshotEntity = new AnnouncementSnapshotEntity();
        announcementSnapshotEntity.setContent(content);
        announcementSnapshotEntity.setAnnouncementId(announcementId);
        announcementSnapshotEntity.setVersion(version);
        announcementSnapshotEntity.setHistoryId(historyId);
        announcementSnapshotEntity.setAllianceId(allianceId);
        announcementSnapshotEntity.save();

        return true;
    }

}
