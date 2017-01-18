package cn.superid.webapp.dao.impl;

import cn.superid.jpa.util.ParameterBindings;
import cn.superid.webapp.controller.VO.InvitationVO;
import cn.superid.webapp.dao.IInvitationDao;
import cn.superid.webapp.enums.state.ValidState;
import cn.superid.webapp.model.InvitationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jizhenya on 17/1/18.
 */
@Component
public class InvitationDao implements IInvitationDao{
    @Override
    public List<InvitationVO> getInvitationList(long userId) {
        StringBuilder sb = new StringBuilder("select id as invitationId , alliance_id from invitation where be_invited_user_id = ? and state = ? ");
        ParameterBindings p = new ParameterBindings();
        p.addIndexBinding(userId);
        p.addIndexBinding(ValidState.Valid);
        return InvitationEntity.getSession().findListByNativeSql(InvitationVO.class,sb.toString(),p);
    }
}
