package cn.superid.webapp.service;

/**
 * Created by jizhenya on 16/9/18.
 */
public interface IContractService {

    /**
     *
     * @param operatorId 操作者id
     * @param contractId 合同id
     * @return 0代表找不到结果，该发起已被其他用户拒绝，1表示同意成功，但不是最后一个，2表示同意并且是最后一个同意
     */
    public int confirm(long operatorId,long contractId);
}
