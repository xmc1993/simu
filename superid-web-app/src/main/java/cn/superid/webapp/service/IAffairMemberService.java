package cn.superid.webapp.service;

import cn.superid.webapp.model.AffairMemberEntity;

/**
 * Created by xiaofengxu on 16/9/2.
 */
public interface IAffairMemberService {
    AffairMemberEntity addMember(Long allianceId,Long affairId,Long roleId,String permissions,long permissionGroupId);//type为
}
