package cn.superid.webapp.service;

import cn.superid.webapp.model.ContractTemplateEntity;
import cn.superid.webapp.service.forms.ContractTemplateForm;
import cn.superid.webapp.service.forms.SignForm;

import java.util.List;

/**
 * Created by jizhenya on 16/9/18.
 */
public interface IContractService {

    /**
     *
     * @param operationRoleId 操作者id
     * @param contractId 合同id
     * @param allianceId 盟id
     * @return 0代表找不到结果，该发起已被其他用户拒绝，1表示同意成功，但不是最后一个，2表示同意并且是最后一个同意
     */
    public int confirm(long allianceId,long operationRoleId,long contractId);

    /**
     *
     * @param operationRoleId
     * @param contractId
     * @return false表示已经被其他人拒绝，合同已不存在，true表示拒绝成功
     */
    public boolean refuseConfirm(long operationRoleId,long contractId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param contractId
     * @return
     */
    public SignForm sign(long allianceId, long operationRoleId, long contractId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param contractId
     * @return
     */
    public boolean undoSign(long allianceId, long operationRoleId,long contractId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param contractId
     * @return
     */
    public boolean terminate(long allianceId, long operationRoleId,long contractId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param contractId
     * @return
     */
    public SignForm agreeTerminate(long allianceId, long operationRoleId,long contractId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param contractId
     * @return
     */
    public SignForm refuseTerminate(long allianceId, long operationRoleId,long contractId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param contractId
     * @param additionId
     * @return
     */
    public SignForm signAddition(long allianceId,long operationRoleId,long contractId,long additionId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param contractId
     * @param additionId
     * @return
     */
    public boolean undoAddition(long allianceId,long operationRoleId,long contractId,long additionId);

    /**
     *
     * @param allianceId
     * @param operationRoleId
     * @param affairId
     * @param title
     * @param content
     * @param thumbContent
     * @return
     */
    public boolean addTemplate(long allianceId,long operationRoleId ,long affairId , String title,String content,String thumbContent);

    /**
     *
     * @param affairId
     * @param operationRoleId
     * @param id
     * @return
     */
    public boolean deleteTemplate(long affairId,long operationRoleId,long id);

    /**
     *
     * @param operationRoleId
     * @return
     */
    public List<ContractTemplateForm> listTemplate(long operationRoleId, long allianceId);

    /**
     *
     * @param id
     * @param affairId
     * @return
     */
    public String getContentOfTemplate(long id , long affairId);

    /**
     *
     * @param operationRoleId
     * @param id
     * @param affairId
     * @param title
     * @param content
     * @param thumbContent
     * @return
     */
    public boolean editTemplate(long operationRoleId ,long id ,long affairId ,String title ,String content ,String thumbContent);

}
