package cn.superid.webapp.utils;

import cn.superid.utils.StringUtil;

/**
 * Created by Qing on 2015/9/12.
 */
public class SQLKeyWordUtil {
    public static String generateKeyword(String keyword) {
        if (StringUtil.isEmpty(keyword)) {
            return "%";
        }
        return "%" + keyword + "%";
    }
}
