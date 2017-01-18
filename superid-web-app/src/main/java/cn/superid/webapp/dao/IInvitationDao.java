package cn.superid.webapp.dao;

import cn.superid.webapp.controller.VO.InvitationVO;

import java.util.List;

/**
 * Created by jizhenya on 17/1/18.
 */
public interface IInvitationDao {

    public List<InvitationVO> getInvitationList(long userId);
}
