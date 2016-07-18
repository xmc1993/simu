package cn.superid.jpa.util;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 *  on 14-12-23.
 */
public class ModelUtils {
    /**
     * 从model类中找到对应的数据库表名
     *
     * @param cls cls to find
     * @return table name found
     */
    public static String getTableNameFromModelClass(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        Table table = cls.getAnnotation(Table.class);
        if (table == null || table.name() == null || table.name().trim().length() < 1) {
            return cls.getSimpleName();
        }
        return table.name().trim();
    }

    private static final Object modelQuerySqlsLock = new Object();
    private static Map<Class<?>, Map<String, String>> modelQueryRawSqlsMappings = new HashMap<Class<?>, Map<String, String>>();
    private static Map<Class<?>, Map<String, String>> modelQuerySqlsMappings = new HashMap<Class<?>, Map<String, String>>();

    public static Object tryNewInstanceForClass(Class<?> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            return new RuntimeException(e);
        }
    }
}
