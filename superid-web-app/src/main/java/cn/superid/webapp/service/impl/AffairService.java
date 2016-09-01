package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.service.IAffairService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AffairService implements IAffairService {
    @Override
    public String getPermissions(long affairId, long roleId) {
        return "39,34,1,2,3,32,4";
    }

    private void JustIndex(long affairId,int index){


    }

    @Override
    @Transactional
    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception{
        AffairEntity parentAffair = AffairEntity.dao.findById(createAffairForm.getParentId());
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

        AffairEntity.dao.eq("id",affairEntity.getId()).set("index",2);


        return null;
    }
}