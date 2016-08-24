package cn.superid.webapp.service.impl;

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
}
