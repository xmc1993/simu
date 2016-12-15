package cn.superid.webapp.service.impl;

import cn.superid.jpa.orm.SQLDao;
import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.SimpleAnnouncementIdVO;
import cn.superid.webapp.controller.VO.SimpleAnnouncementVO;
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
    public EditDistanceForm compareTwoBlocks(List<Block> present, List<Block> history) {
        //注意,该方法是计算将现有的文章变为任意一个版本的变量,参数中,prenset表示现有文章,history表示要变的文章
//        List<ReplaceForm> substitute = new ArrayList<>();
//        List<InsertForm> insert = new ArrayList<>();
//        List<Integer> delete = new ArrayList<>();
//        List<Block> replace = new ArrayList<>();
//        for(int i = 0 ; i < present.size() ; i++){
//            boolean isExit = false;
//            for( int j = 0 ; j < history.size() ; j++){
//                if(present.get(i).getKey().equals(history.get(j).getKey())){
//                    replace.add(new Block(present.get(i).getKey(),"",i,j));
//                    if(!present.get(i).getContent().equals(history.get(j).getContent())){
//                        //如果两个key相同的block的content不相同,则需要替换操作,相同的话则不需要动
//                        substitute.add(new ReplaceForm(i+1,history.get(j).getContent()));
//                    }
//                    isExit = true ;
//                    break;
//                }
//            }
//            if(isExit == false){
//                //表示该block在老文章中存在,但在新文章中不存在,应该删除
//                delete.add(i+1);
//            }
//        }
//
//        if(replace.size() == 0){
//            //如果没有重复的block,则在开头把所有的按顺序加上就好
//            List<EasyBlock> list = new ArrayList<>();
//            for(Block b : history){
//                list.add(new EasyBlock(b.getContent(),b.getKey()));
//            }
//            insert.add(new InsertForm(0,list));
//        }else{
//            //第三步,增加没有的block
//            for(int i = 0 ; i < replace.size() ; i++){
//                List<EasyBlock> list = new ArrayList<>();
//                if(i == 0){
//                    //如果是第一个,则取出0~j-1的block执行insert,第一次取出来的应该插入在0之后,有的话就插入
//                    if(replace.get(i).getNewlocation() > 1){
//                        for(int z = 0 ; z < replace.get(i).getNewlocation() ; z++){
//                            list.add(new EasyBlock(history.get(z).getContent(),history.get(z).getKey()));
//                        }
//                        insert.add(new InsertForm(0,list));
//                        list = new ArrayList<>();
//                    }
//                }
//                if(i == replace.size()-1){
//                    //如果是最后一个,则把最后那部分加入到最后
//                    if(!(replace.get(i).getNewlocation() == history.size()-1)){
//                        //如果之后都没有段落了,则不用插入
//                        for(int z = replace.get(i).getNewlocation()+1 ; z < history.size() ; z++ ){
//                            list.add(new EasyBlock(history.get(z).getContent(),history.get(z).getKey()));
//                        }
//                        insert.add(new InsertForm(replace.get(i).getLocation()+1,list));
//                        list = new ArrayList<>();
//                    }
//                }
//                if(i > 0 & i < replace.size()-1){
//
//                    //中间部分,取出history j与j+1之间的所有段落,将其插入i+1的后方
//                    if(replace.get(i).getNewlocation()+1 < replace.get(i+1).getNewlocation()){
//                        for(int z = replace.get(i).getNewlocation()+1 ; z < replace.get(i+1).getNewlocation() ; z++){
//                            list.add(new EasyBlock(history.get(z).getContent(),history.get(z).getKey()));
//                        }
//                        insert.add(new InsertForm(replace.get(i).getLocation()+1,list));
//                    }
//
//                }
//            }
//        }
//        EditDistanceForm result = new EditDistanceForm(delete,insert,substitute);
        return null;
    }

    @Override
    public EditDistanceForm compareTwoPapers(ContentState present, ContentState history) {
        List<Block> presentBlock = getBlock(present);
        List<Block> historyBlock = getBlock(history);
        return compareTwoBlocks(presentBlock,historyBlock);
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
    public boolean saveDraft(String delta, long draftId, long allianceId, long affairId, long roleId, int publicType, String title, long taskId, String entityMap) {

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
            return false;
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

        announcementDraftEntity.setTitle(title);
        if(draftId == 0){
            announcementDraftEntity.save();
        }else{
            announcementDraftEntity.update();
        }
        return true;
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
        announcementEntity.setState(1);
        announcementEntity.setCreatorId(roleId);
        announcementEntity.setCreateTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setModifyTime(TimeUtil.getCurrentSqlTime());
        announcementEntity.setVersion(1);
        announcementEntity.setDecrement("0");
        announcementEntity.setAllianceId(allianceId);
        announcementEntity.setCreatorId(roleId);
        announcementEntity.save();

        return true;
    }

    @Override
    public boolean deleteAnnouncement(long announcementId, long allianceId) {
        AnnouncementEntity.dao.id(announcementId).partitionId(allianceId).set("state",0);
        //把这条加入announcement中
        AnnouncementHistoryEntity announcementHistoryEntity = new AnnouncementHistoryEntity();
        return true;
    }

    @Override
    public List<SimpleAnnouncementIdVO> getIdByAffair(long affairId, long allianceId , boolean isContainChild) {
        List<SimpleAnnouncementIdVO> simpleAnnouncementIdVOList = null;
        if(isContainChild == false){
            StringBuilder sql = new StringBuilder("select id as announcementId,modify_time,affair_id from announcement  where alliance_id = ? and affair_id = ? and state = 0 order by modify_time desc ");
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
        return simpleAnnouncementIdVOList;
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

    public List<SimpleAnnouncementIdVO> getHistoryIds(Timestamp time , long affairId  ){
        return null;
    }
}
