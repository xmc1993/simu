package cn.superid.webapp.controller.VO;

import cn.superid.webapp.controller.forms.AddAllianceUserForm;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModel;

import java.util.List;

/**
 * Created by njuTms on 17/1/3.
 */
@ApiModel
public class AddAllianceUserFormVO {
    private List<AddAllianceUserForm> users;

    public List<AddAllianceUserForm> getUsers() {
        return users;
    }

    public void setUsers(List<AddAllianceUserForm> users) {
        this.users = users;
    }
}
