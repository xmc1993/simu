package cn.superid.webapp.security;

import org.springframework.security.web.PortResolverImpl;

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
    private final static int[] administrator= {AffairPermissions.CREATEAFFAIR,AffairPermissions.MOVEAFFAIR,AffairPermissions.INVALIDAFFAIR,
            AffairPermissions.CHECKAFFAIRMAINPAGE, AffairPermissions.EDITAFFAIRINFO,
            AffairPermissions.CREATECHILDAFFAIR,AffairPermissions.GENERATECHILDAFFAIRPERMISSION,
            AffairPermissions.ADDAFFAIRMEMBER,AffairPermissions.INVALIDAFFAIRMEMBER,
            AffairPermissions.CHECKANNOUNCEMENT,AffairPermissions.ADDANNOUNCEMENT,AffairPermissions.EDITANNOUNCEMENT,AffairPermissions.APPLYANNOUNCEMENT,AffairPermissions.INVALIDANNOUNCEMENT,
            AffairPermissions.CHECKTASKLIST,AffairPermissions.ADDTASK,AffairPermissions.ADDTASKMEMBER,AffairPermissions.REMOVETASKMEMBER,AffairPermissions.EDITTASK,AffairPermissions.ADDTASKCONTENT,
            AffairPermissions.CHECKFILE,AffairPermissions.UPLOADFILE,AffairPermissions.MOVEFILE,AffairPermissions.DELETEFILE,AffairPermissions.DOWNLOADFILE,AffairPermissions.UPDATEFILEVERSION,AffairPermissions.MANAGEFOLDER,
            AffairPermissions.ADDTRADE,AffairPermissions.CHECKTRADEFLOW,AffairPermissions.CHECKTRADETOANDFROM,
            AffairPermissions.CHECKCONTRACT,AffairPermissions.ADDCONTRACT,AffairPermissions.EDITCONTRACT,
            AffairPermissions.GENERATEPERMISSIONGROUP,AffairPermissions.ADDPERMISSIONGROUP,AffairPermissions.REMOVEPERMISSIONGROUP,AffairPermissions.UPDATEPERMISSIONGROUP,AffairPermissions.SETDEFAULTPERMISSIONGROUP,
            AffairPermissions.CHECKAFFAIRFUND,
            AffairPermissions.CHECKAFFAIRGOODS};
    private final static int[] official = {AffairPermissions.CHECKAFFAIRMAINPAGE,
            AffairPermissions.CHECKANNOUNCEMENT,AffairPermissions.ADDANNOUNCEMENT,AffairPermissions.EDITANNOUNCEMENT,AffairPermissions.APPLYANNOUNCEMENT,AffairPermissions.INVALIDANNOUNCEMENT,
            AffairPermissions.CHECKTASKLIST,AffairPermissions.ADDTASK,AffairPermissions.ADDTASKMEMBER,AffairPermissions.REMOVETASKMEMBER,AffairPermissions.EDITTASK,AffairPermissions.ADDTASKCONTENT,
            AffairPermissions.CHECKFILE,AffairPermissions.UPLOADFILE,AffairPermissions.MOVEFILE,AffairPermissions.DELETEFILE,AffairPermissions.DOWNLOADFILE,AffairPermissions.UPDATEFILEVERSION,AffairPermissions.MANAGEFOLDER,
            AffairPermissions.ADDTRADE,AffairPermissions.CHECKTRADEFLOW,AffairPermissions.CHECKTRADETOANDFROM,
            AffairPermissions.CHECKCONTRACT,AffairPermissions.ADDCONTRACT,AffairPermissions.EDITCONTRACT,
            AffairPermissions.CHECKAFFAIRFUND,
            AffairPermissions.CHECKAFFAIRGOODS};

    private final static int[] guest = {AffairPermissions.CHECKAFFAIRMAINPAGE,
            AffairPermissions.CHECKANNOUNCEMENT,AffairPermissions.APPLYANNOUNCEMENT,
            AffairPermissions.CHECKTASKLIST,AffairPermissions.EDITTASK,AffairPermissions.ADDTASKCONTENT,
            AffairPermissions.CHECKFILE,AffairPermissions.UPLOADFILE,
            AffairPermissions.ADDTRADE,};

    private final static int[] visitor = {AffairPermissions.CHECKAFFAIRMAINPAGE,
            AffairPermissions.CHECKANNOUNCEMENT};


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
