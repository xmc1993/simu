package cn.superid.admin.service;


import cn.superid.admin.model.AdminEntity;
import cn.superid.admin.model.AllianceCertificationEntity;

import java.util.List;

/**
 * Created by njuTms on 16/10/9.
 */
public interface IAdminService {
    public AdminEntity login(String userName, String password);

}
