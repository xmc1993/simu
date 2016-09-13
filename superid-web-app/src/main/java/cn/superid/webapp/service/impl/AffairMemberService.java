package cn.superid.webapp.service.impl;

import cn.superid.webapp.model.AffairEntity;
import cn.superid.webapp.model.AffairMemberEntity;
import cn.superid.webapp.service.IAffairMemberService;
import org.springframework.stereotype.Service;

/**
 * Created by xiaofengxu on 16/9/2.
 */
@Service
public class AffairMemberService implements IAffairMemberService {
    @Override
    public AffairMemberEntity addMember(Long allianceId,Long affairId, Long roleId,  String permissions,long permissionGroupId) {
        AffairMemberEntity affairMemberEntity = new AffairMemberEntity();
        affairMemberEntity.setAffairId(affairId);
        affairMemberEntity.setRoleId(roleId);
        affairMemberEntity.setPermissions(permissions);
        affairMemberEntity.setPermissionGroupId(permissionGroupId);
        affairMemberEntity.setAllianceId(allianceId);
        affairMemberEntity.save();
        return affairMemberEntity;
    }
}
