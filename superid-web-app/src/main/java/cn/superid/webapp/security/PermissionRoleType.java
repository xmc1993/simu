package cn.superid.webapp.security;

import org.springframework.security.web.PortResolverImpl;

import java.util.HashMap;

/**
 * Created by njuTms on 16/9/7.
 */
public class  PermissionRoleType {
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


    public final static long OWNER_ID =1L;
    public final static long ADMINISTRATOR_ID= 2L;
    public final static long OFFICIAL_ID = 3L;
    public final static long GUEST_ID = 4L;
    public final static long VISITOR_ID = 5L;

//    public static String getPermissions(Long id){
//        switch (id){
//
//        }
//    }


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
