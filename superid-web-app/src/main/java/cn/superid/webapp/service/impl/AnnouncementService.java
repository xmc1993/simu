package cn.superid.webapp.service.impl;

import cn.superid.webapp.service.IAnnouncementService;
import cn.superid.webapp.service.forms.Block;
import cn.superid.webapp.service.forms.Operation;
import cn.superid.webapp.service.forms.OperationListForm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
@Service
public class AnnouncementService implements IAnnouncementService{
    @Override
    public OperationListForm compareTwoPapers(List<Block> present, List<Block> history) {
        //注意,该方法是计算将现有的文章变为任意一个版本的变量,参数中,prenset表示现有文章,history表示要变的文章
        List<Operation> substitute = new ArrayList<>();
        List<Operation> insert = new ArrayList<>();
        List<Operation> delete = new ArrayList<>();
        List<Block> replace = new ArrayList<>();
        for(int i = 0 ; i < present.size() ; i++){
            boolean isExit = false;
            for( int j = 0 ; j < history.size() ; j++){
                if(present.get(i).getKey().equals(history.get(j).getKey())){
                    replace.add(new Block(present.get(i).getKey(),"",i,j));
                    if(!present.get(i).getContent().equals(history.get(j).getContent())){
                        //如果两个key相同的block的content不相同,则需要替换操作,相同的话则不需要动
                        substitute.add(new Operation("substitute",i+1,history.get(j).getContent()));
                    }
                    isExit = true ;
                    break;
                }
            }
            if(isExit == false){
                //表示该block在老文章中存在,但在新文章中不存在,应该删除
                delete.add(new Operation("delete",i+1,""));
            }
        }

        if(replace.size() == 0){
            //如果没有重复的block,则在开头把所有的按顺序加上就好
            for(Block b : history){
                insert.add(new Operation("insert",0,b.getContent()));
            }
        }else{
            //第三步,增加没有的block
            for(int i = 0 ; i < replace.size() ; i++){
                if(i == 0){
                    //如果是第一个,则取出0~j-1的block执行insert,第一次取出来的应该插入在0之后
                    for(int z = 0 ; z < replace.get(i).getNewlocation() ; z++){
                        insert.add(new Operation("insert",0,history.get(z).getContent()));
                    }
                }else if(i == replace.size()-1){
                    //如果是最后一个,则把最后那部分加入到最后
                    if(!(replace.get(i).getNewlocation() == history.size()-1)){
                        //如果之后都没有段落了,则不用插入
                        for(int z = replace.get(i).getNewlocation()+1 ; z < history.size() ; z++ ){
                            insert.add(new Operation("insert",replace.get(i).getLocation()+1,history.get(z).getContent()));
                        }
                    }
                }else{
                    //中间部分,取出history j与j+1之间的所有段落,将其插入i+1的后方
                    for(int z = replace.get(i).getNewlocation()+1 ; z < replace.get(i+1).getNewlocation() ; z++){
                        insert.add(new Operation("insert",replace.get(i).getLocation()+1,history.get(z).getContent()));
                    }
                }
            }
        }

        OperationListForm result = new OperationListForm(insert,substitute,delete);



        return result;
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
}
