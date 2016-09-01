package cn.superid.webapp.service.impl;

import cn.superid.jpa.util.Expr;
import cn.superid.webapp.forms.CreateAffairForm;
import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.service.IAffairService;
import org.springframework.stereotype.Service;

/**
 * Created by zp on 2016/8/8.
 */
@Service
public class AffairService implements IAffairService {
    @Override
    public String getPermissions(long affairId, long roleId) {
        return "39,34,1,2,3,32,4";
    }

    @Override
    public AffairEntity createAffair(CreateAffairForm createAffairForm) throws Exception{
        AffairEntity parentAffair = AffairEntity.dao.findById(createAffairForm.getParentId());
        if(parentAffair==null){
            throw new Exception("parent affair not found ");
        }

        int count = AffairEntity.dao.eq("parentId",parentAffair.getId()).count();//已有数目

        AffairEntity affairEntity=new AffairEntity();
        affairEntity.setType(parentAffair.getType());
        affairEntity.setPublicType(createAffairForm.getPublicType());


        return null;
    }
}
