package cn.superid.admin.service.impl;

import cn.superid.admin.model.AdminEntity;
import cn.superid.admin.model.AllianceCertificationEntity;
import cn.superid.admin.model.AllianceEntity;
import cn.superid.admin.security.MyAuth;
import cn.superid.admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by njuTms on 16/10/9.
 */
@Service
public class AdminService implements IAdminService {
    @Autowired
    private MyAuth auth;
    @Override
    public boolean login(String userName, String password) {
        if(AdminEntity.dao.eq("name",userName).eq("password",password).exists()){
            auth.authUser(userName);
            return true;
        }
        return false;
    }

}
