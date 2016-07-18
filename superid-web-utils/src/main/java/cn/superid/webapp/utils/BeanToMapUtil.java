package cn.superid.webapp.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simu-hq on 2015/4/10.
 */
public class BeanToMapUtil {
    public static Map<String, Object> beanToMap(Object object) {
        Class c = object.getClass();
        Map<String, Object> map = new HashMap<>();
        Field field[] = c.getDeclaredFields();
        for (Field f : field) {
            try {
                map.put(f.getName(), getFieldValueByName(f.getName(), object));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 根据属性名获取属性值
     */
    private static Object getFieldValueByName(String fieldName, Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        try {
            Method method = o.getClass().getMethod(getter, new Class[]{});
            return method.invoke(o);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
