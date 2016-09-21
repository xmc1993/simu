package cn.superid.webapp.service.forms;

/**
 * Created by njuTms on 16/8/25.
 */
public class ContractMemberResult {
    private String name;
    private int kind;
    private int isInKind = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getIsInKind() {
        return isInKind;
    }

    public void setIsInKind(int isInKind) {
        this.isInKind = isInKind;
    }

    public static class UserAndRoles{
        private String userName;
        private String roleName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }
    }
}
