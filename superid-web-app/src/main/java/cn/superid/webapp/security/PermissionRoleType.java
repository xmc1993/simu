package cn.superid.webapp.security;

/**
 * Created by njuTms on 16/9/7.
 */
public class  PermissionRoleType {
    /*
    public final static String OWNER = "13,26,39,14,27,40,53,15,28,16,29,17,30,43,56,69,18,31,44,57,70,83,19,32,45,58,71,84,97,20,33,46,21,34,47,22,35,48,61,74,23,24";
    public final static String ADMINISTRATOR = "13,26,39,14,27,15,28,16,29,17,30,43,56,69,18,31,44,57,70,83,19,32,45,58,71,84,97,20,33,46,21,34,47,22,35,48,61,74,23,24";
    public final static String OFFICIAL = "14,17,30,43,56,69,18,31,44,57,70,83,19,32,45,58,71,84,97,20,33,46,21,34,47,23,24";
    public final static String GUEST = "14,17,56,18,70,83,19,32,20";
    public final static String VISITOR = "14,17";
    */
    private final static int[] administrator= {AffairPermissions.CREATE_AFFAIR,AffairPermissions.MOVE_AFFAIR,AffairPermissions.INVALID_AFFAIR,
            AffairPermissions.CHECK_AFFAIR_HOMEPAGE, AffairPermissions.EDIT_AFFAIR_INFO,
            AffairPermissions.CREATE_CHILD_AFFAIR,AffairPermissions.GENERATE_CHILD_AFFAIR_PERMISSION,
            AffairPermissions.ADD_AFFAIR_MEMBER,AffairPermissions.INVALID_AFFAIR_MEMBER,
            AffairPermissions.CHECK_ANNOUNCEMENT,AffairPermissions.ADD_ANNOUNCEMENT,AffairPermissions.EDIT_ANNOUNCEMENT,AffairPermissions.APPLY_ANNOUNCEMENT,AffairPermissions.INVALID_ANNOUNCEMENT,
            AffairPermissions.CHECK_TASK_LIST,AffairPermissions.ADD_TASK,AffairPermissions.ADD_TASK_MEMBER,AffairPermissions.REMOVE_TASK_MEMBER,AffairPermissions.EDIT_TASK,AffairPermissions.ADD_TASK_CONTENT,
            AffairPermissions.CHECK_FILE,AffairPermissions.UPLOAD_FILE,AffairPermissions.MOVE_FILE,AffairPermissions.DELETE_FILE,AffairPermissions.DOWNLOAD_FILE,AffairPermissions.UPDATE_FILE_VERSION,AffairPermissions.MANAGE_FOLDER,
            AffairPermissions.ADD_TRADE,AffairPermissions.CHECK_TRADE_FLOW,AffairPermissions.CHECK_TRADE_TO_AND_FROM,
            AffairPermissions.CHECK_CONTRACT,AffairPermissions.ADD_CONTRACT,AffairPermissions.EDIT_CONTRACT,
            AffairPermissions.GENERATE_PERMISSION_GROUP,AffairPermissions.ADD_PERMISSION_GROUP,AffairPermissions.REMOVE_PERMISSION_GROUP,AffairPermissions.UPDATE_PERMISSION_GROUP,AffairPermissions.SET_DEFAULT_PERMISSION_GROUP,
            AffairPermissions.CHECK_AFFAIR_FUND,
            AffairPermissions.CHECK_AFFAIR_GOODS};
    private final static int[] official = {AffairPermissions.CHECK_AFFAIR_HOMEPAGE,
            AffairPermissions.CHECK_ANNOUNCEMENT,AffairPermissions.ADD_ANNOUNCEMENT,AffairPermissions.EDIT_ANNOUNCEMENT,AffairPermissions.APPLY_ANNOUNCEMENT,AffairPermissions.INVALID_ANNOUNCEMENT,
            AffairPermissions.CHECK_TASK_LIST,AffairPermissions.ADD_TASK,AffairPermissions.ADD_TASK_MEMBER,AffairPermissions.REMOVE_TASK_MEMBER,AffairPermissions.EDIT_TASK,AffairPermissions.ADD_TASK_CONTENT,
            AffairPermissions.CHECK_FILE,AffairPermissions.UPLOAD_FILE,AffairPermissions.MOVE_FILE,AffairPermissions.DELETE_FILE,AffairPermissions.DOWNLOAD_FILE,AffairPermissions.UPDATE_FILE_VERSION,AffairPermissions.MANAGE_FOLDER,
            AffairPermissions.ADD_TRADE,AffairPermissions.CHECK_TRADE_FLOW,AffairPermissions.CHECK_TRADE_TO_AND_FROM,
            AffairPermissions.CHECK_CONTRACT,AffairPermissions.ADD_CONTRACT,AffairPermissions.EDIT_CONTRACT,
            AffairPermissions.CHECK_AFFAIR_FUND,
            AffairPermissions.CHECK_AFFAIR_GOODS};

    private final static int[] guest = {AffairPermissions.CHECK_AFFAIR_HOMEPAGE,
            AffairPermissions.CHECK_ANNOUNCEMENT,AffairPermissions.APPLY_ANNOUNCEMENT,
            AffairPermissions.CHECK_TASK_LIST,AffairPermissions.EDIT_TASK,AffairPermissions.ADD_TASK_CONTENT,
            AffairPermissions.CHECK_FILE,AffairPermissions.UPLOAD_FILE,
            AffairPermissions.ADD_TRADE,};

    private final static int[] visitor = {AffairPermissions.CHECK_AFFAIR_HOMEPAGE,
            AffairPermissions.CHECK_ANNOUNCEMENT};


    public final static String OWNER = "*";
    public final static String ADMINISTRATOR = intToString(administrator);
    public final static String OFFICIAL = intToString(official);
    public final static String GUEST = intToString(guest);
    public final static String VISITOR = intToString(visitor);

    private static String intToString(int[] a) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < a.length; i++) {
            if (i < a.length - 1) {
                sb.append(a[i] + ",");
            } else if (i == a.length - 1) {
                sb.append(a[i]);
            }
        }
        return sb.toString();
    }
}
