package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.SQLDao;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.*;
import cn.superid.webapp.controller.forms.EasyBlock;
import cn.superid.webapp.controller.forms.EditDistanceForm;
import cn.superid.webapp.controller.forms.InsertForm;
import cn.superid.webapp.controller.forms.ReplaceForm;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.*;
import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.IRoleService;
import cn.superid.webapp.service.forms.*;
import cn.superid.webapp.service.vo.UserNameAndRoleNameVO;
import cn.superid.webapp.utils.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
@Service
public class AnnouncementService implements IAnnouncementService{
    @Autowired
    private IRoleService roleService;

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

    public List<Block> paperToBlockList(String content){
        List<Block> result = new ArrayList<>();
        JSONObject total = JSON.parseObject(content);
        JSONArray bs = total.getJSONArray("blocks");
        for(int i = 0 ; i < bs.size() ; i++){
            JSONObject one = bs.getJSONObject(i);
            result.add(new Block(one.getString("key"),one.getString("text"),i));
        }
        return result;
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
        AnnouncementEntity announcementEntity = AnnouncementEntity.dao.findById(announcementId,allianceId);
        if(announcementEntity == null){
            return false;
        }
        //将现在的一条存为历史
        AnnouncementHistoryEntity history = new AnnouncementHistoryEntity();
        history.setAnnouncementId(announcementEntity.getId());
        history.setModifierId(announcementEntity.getModifierId());
        history.setTitle(announcementEntity.getTitle());
        history.setVersion(announcementEntity.getVersion());
        history.setCreateTime(TimeUtil.getCurrentSqlTime());
        history.setDecrement(announcementEntity.getDecrement());
        ContentState old = JSON.parseObject(announcementEntity.getContent(),ContentState.class);
        history.setIncrement(JSONObject.toJSONString(compareTwoPapers(old,contentState)));
        history.save();

        //如果满足记录条件,就存一条快照
        if(announcementEntity.getVersion()%SNAPSHOT_INTERVAL == 0){
            //如果是三十的倍数
            generateSnapshot(announcementId,announcementEntity.getVersion(),announcementEntity.getContent(),roleId,announcementEntity.getTitle(),history.getId());
        }


        //改变原有记录
        announcementEntity.setVersion(announcementEntity.getVersion()+1);
        announcementEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setModifierId(roleId);
        announcementEntity.setDecrement(JSONObject.toJSONString(compareTwoPapers(contentState,old)));
        announcementEntity.setContent(JSONObject.toJSONString(contentState));
        announcementEntity.update();
        return true;
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
    public boolean createAnnouncement(String title, long affairId, long allianceId, long taskId, long roleId, int isTop, int publicType, ContentState content) {
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
        announcementEntity.setCreatorId(roleId);
        announcementEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setVersion(1);
        announcementEntity.setDecrement("0");
        announcementEntity.setAllianceId(allianceId);
        announcementEntity.setCreatorId(roleId);
        announcementEntity.setSessionSum(0);
        announcementEntity.save();

        return true;
    }

    @Override
    public boolean deleteAnnouncement(long announcementId, long allianceId, long roleId) {
        AnnouncementEntity announcementEntity = AnnouncementEntity.dao.findById(announcementId,allianceId);
        //把这条加入announcement中
        AnnouncementHistoryEntity announcementHistoryEntity = new AnnouncementHistoryEntity();
        announcementHistoryEntity.setAnnouncementId(announcementEntity.getId());
        announcementHistoryEntity.setModifierId(roleId);
        return true;
    }

    @Override
    public List<SimpleAnnouncementIdVO> getIdByAffair(long affairId, long allianceId , boolean isContainChild) {
        List<SimpleAnnouncementIdVO> simpleAnnouncementIdVOList = null;
        if(isContainChild == false){
            StringBuilder sql = new StringBuilder("select id as announcementId,modify_time,affair_id,is_top from announcement  where alliance_id = ? and affair_id = ? and state = 0 order by modify_time desc ");
            ParameterBindings p = new ParameterBindings();
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affairId);
            simpleAnnouncementIdVOList = SimpleAnnouncementIdVO.dao.findList(sql.toString(),p);
        }else{
            AffairEntity affair = AffairEntity.dao.findById(affairId,allianceId);
            if(affair == null){
                return null;
            }
            StringBuilder sql = new StringBuilder(SQLDao.GET_ANNOUNCEMENT_ID);
            ParameterBindings p = new ParameterBindings();
            p.addIndexBinding(allianceId);
            p.addIndexBinding(allianceId);
            p.addIndexBinding(affair.getPath()+"%");

            simpleAnnouncementIdVOList = SimpleAnnouncementIdVO.dao.findList(sql.toString(),p);
        }
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
    public List<SimpleDraftIdVO> getDraftByAffair(long affairId, long allianceId, long roleId) {
        StringBuilder sql = new StringBuilder("select id ,modify_time,title from announcement_draft  where alliance_id = ? and affair_id = ? and creator_id = ? and state = 0 order by modify_time desc ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        p.addIndexBinding(roleId);
        return SimpleDraftIdVO.dao.findList(sql.toString(),p);
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
    public List<SimpleAnnouncementVO> getOverview(String ids,  long allianceId) {
        String[] idList = ids.split(",");

        StringBuilder sql = new StringBuilder("select a.* , b.name as affairName from (select title , id , affair_id , thumb_content as content, creator_id from announcement where id in ( 0 ");
        ParameterBindings p = new ParameterBindings();

        for(String id : idList){
            if(id.matches("[0-9]+")){
                sql.append(","+id);
            }
        }
        sql.append(" ) ) a join affair b on a.affair_id = b.id ");

        List<SimpleAnnouncementVO> result = AnnouncementEntity.getSession().findList(SimpleAnnouncementVO.class,sql.toString(),p);
        if(result == null ){
            return null;
        }
        for(SimpleAnnouncementVO s : result){
            UserNameAndRoleNameVO name = roleService.getUserNameAndRoleName(s.getCreatorId());
            s.setRoleName(name.getRoleName());
            s.setUsername(name.getUserName());
            s.setAvatar(name.getAvatar());
        }

        return result;
    }

    @Override
    public List<SimpleAnnouncementIdVO> searchAnnouncement(String content, Long affairId, Long allianceId) {
        StringBuilder sql = new StringBuilder(SQLDao.SEARCH_ANNOUNCEMENT);
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(allianceId);
        p.addIndexBinding(affairId);
        p.addIndexBinding(allianceId);
        p.addIndexBinding("%"+content+"%");
        p.addIndexBinding(content);
        p.addIndexBinding(content);


        return AnnouncementEntity.getSession().findList(SimpleAnnouncementIdVO.class,sql.toString(),p);
    }

    @Override
    public AnnouncementEntity getDetail(long announcementId, long allianceId) {
        AnnouncementEntity result = AnnouncementEntity.dao.findById(announcementId,allianceId);
        if(result != null){
            UserNameAndRoleNameVO userNameAndRoleNameVO = roleService.getUserNameAndRoleName(result.getModifierId());
            if(userNameAndRoleNameVO != null){
                result.setRoleName(userNameAndRoleNameVO.getRoleName());
                result.setUsername(userNameAndRoleNameVO.getUserName());
            }
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

    private boolean generateSnapshot(long announcementId , int version , String content , long modifierId , String title , long historyId){
        AnnouncementSnapshotEntity announcementSnapshotEntity = new AnnouncementSnapshotEntity();
        announcementSnapshotEntity.setTitle(title);
        announcementSnapshotEntity.setContent(content);
        announcementSnapshotEntity.setAnnouncementId(announcementId);
        announcementSnapshotEntity.setVersion(version);
        announcementSnapshotEntity.setModifierId(modifierId);
        announcementSnapshotEntity.setHsitoryId(historyId);
        announcementSnapshotEntity.save();

        return true;
    }


}
