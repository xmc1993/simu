package cn.superid.webapp.security;


/**
 * Created by njuTms on 16/9/7.
 */
public class AffairPermissionRoleType {

    /*
    private final static int[] administrator= {AffairPermissions.CREATE_AFFAIR,
            AffairPermissions.CHECK_AFFAIR_HOMEPAGE, AffairPermissions.EDIT_AFFAIR_INFO,
            AffairPermissions.CREATE_CHILD_AFFAIR,AffairPermissions.GENERATE_CHILD_AFFAIR_PERMISSION,
            AffairPermissions.ADD_AFFAIR_ROLE,AffairPermissions.REMOVE_AFFAIR_ROLE,AffairPermissions.CHECK_AFFAIR_ROLE,AffairPermissions.ALLOCATE_PERMISSION,
            AffairPermissions.CHECK_ANNOUNCEMENT,AffairPermissions.ADD_ANNOUNCEMENT,AffairPermissions.EDIT_ANNOUNCEMENT,AffairPermissions.APPLY_ANNOUNCEMENT,AffairPermissions.INVALID_ANNOUNCEMENT,AffairPermissions.CHECK_CHILD_AFFAIR_ANNOUNCEMENT,
            AffairPermissions.CHECK_TASK_LIST,AffairPermissions.ADD_TASK,AffairPermissions.ADD_TASK_MEMBER,AffairPermissions.REMOVE_TASK_MEMBER,AffairPermissions.EDIT_TASK,AffairPermissions.ADD_TASK_CONTENT,
            AffairPermissions.CHECK_FILE,AffairPermissions.UPLOAD_FILE,AffairPermissions.MOVE_FILE,AffairPermissions.DELETE_FILE,AffairPermissions.DOWNLOAD_FILE,AffairPermissions.UPDATE_FILE_VERSION,AffairPermissions.MANAGE_FOLDER,
            AffairPermissions.ADD_TRADE,AffairPermissions.CHECK_TRADE_FLOW,AffairPermissions.CHECK_TRADE_TO_AND_FROM,
            AffairPermissions.CHECK_CONTRACT,AffairPermissions.ADD_CONTRACT,AffairPermissions.EDIT_CONTRACT,
            //AffairPermissions.GENERATE_PERMISSION_GROUP,AffairPermissions.ADD_PERMISSION_GROUP,AffairPermissions.REMOVE_PERMISSION_GROUP,AffairPermissions.UPDATE_PERMISSION_GROUP,AffairPermissions.SET_DEFAULT_PERMISSION_GROUP,
            AffairPermissions.CHECK_AFFAIR_FUND,
            AffairPermissions.CHECK_AFFAIR_GOODS};

    private final static int[] official = {AffairPermissions.CHECK_AFFAIR_HOMEPAGE,
            AffairPermissions.CHECK_AFFAIR_ROLE,
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


    public final static int OWNER_ID =1;
    public final static int ADMINISTRATOR_ID= 2;
    public final static int OFFICIAL_ID = 3;
    public final static int GUEST_ID = 4;
    public final static int VISITOR_ID = 5;

    public final static HashMap<Integer,String> roles= new HashMap<Integer, String>(){
        {
            put(OWNER_ID,OWNER);
            put(ADMINISTRATOR_ID,ADMINISTRATOR);
            put(OFFICIAL_ID,OFFICIAL);
            put(GUEST_ID,GUEST);
            put(VISITOR_ID,VISITOR);
        }
    };

    */

    private final static int[] participant= {AffairPermissions.CHECK_AFFAIR_HOMEPAGE,
            AffairPermissions.CHECK_AFFAIR_ROLE,
            AffairPermissions.CHECK_ANNOUNCEMENT,AffairPermissions.ADD_ANNOUNCEMENT,AffairPermissions.EDIT_ANNOUNCEMENT,AffairPermissions.APPLY_ANNOUNCEMENT,AffairPermissions.INVALID_ANNOUNCEMENT,AffairPermissions.CHECK_CHILD_AFFAIR_ANNOUNCEMENT,
            AffairPermissions.CHECK_TASK_LIST,AffairPermissions.ADD_TASK,AffairPermissions.ADD_TASK_MEMBER,AffairPermissions.REMOVE_TASK_MEMBER,AffairPermissions.EDIT_TASK,AffairPermissions.ADD_TASK_CONTENT,
            AffairPermissions.CHECK_FILE,AffairPermissions.UPLOAD_FILE,AffairPermissions.MOVE_FILE,AffairPermissions.DELETE_FILE,AffairPermissions.DOWNLOAD_FILE,AffairPermissions.UPDATE_FILE_VERSION,AffairPermissions.MANAGE_FOLDER,
            AffairPermissions.ADD_TRADE,AffairPermissions.CHECK_TRADE_FLOW,AffairPermissions.CHECK_TRADE_TO_AND_FROM,
            AffairPermissions.CHECK_CONTRACT,
            AffairPermissions.CHECK_AFFAIR_FUND,
            AffairPermissions.CHECK_AFFAIR_GOODS};


    private final static int[] menkor= {AffairPermissions.CHECK_AFFAIR_HOMEPAGE,
            AffairPermissions.CREATE_CHILD_AFFAIR,AffairPermissions.GENERATE_CHILD_AFFAIR_PERMISSION,
            AffairPermissions.ADD_AFFAIR_ROLE,AffairPermissions.REMOVE_AFFAIR_ROLE,AffairPermissions.CHECK_AFFAIR_ROLE,AffairPermissions.ALLOCATE_PERMISSION,
            AffairPermissions.CHECK_ANNOUNCEMENT,AffairPermissions.APPLY_ANNOUNCEMENT,
            AffairPermissions.CHECK_TASK_LIST,AffairPermissions.EDIT_TASK,AffairPermissions.ADD_TASK_CONTENT,
            AffairPermissions.CHECK_FILE,AffairPermissions.UPLOAD_FILE,
            AffairPermissions.ADD_TRADE};


    public final static String OWNER = "*";
    public final static String PARTICIPANT = intToString(participant);
    public final static String MENKOR = intToString(menkor);

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
