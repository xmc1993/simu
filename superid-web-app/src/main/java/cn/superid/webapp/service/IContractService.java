package cn.superid.webapp.service;

import cn.superid.webapp.service.forms.SignForm;

/**
 * Created by jizhenya on 16/9/18.
 */
public interface IContractService {

    /**
     *
     * @param operatorId 操作者id
     * @param contractId 合同id
     * @param allianceId 盟id
     * @return 0代表找不到结果，该发起已被其他用户拒绝，1表示同意成功，但不是最后一个，2表示同意并且是最后一个同意
     */
    public int confirm(long allianceId,long operatorId,long contractId);

    /**
     *
     * @param operatorId
     * @param contractId
     * @return false表示已经被其他人拒绝，合同已不存在，true表示拒绝成功
     */
    public boolean refuseConfirm(long operatorId,long contractId);

    /**
     *
     * @param allianceId
     * @param operatorId
     * @param contractId
     * @return
     */
    public SignForm sign(long allianceId, long operatorId, long contractId);
}
