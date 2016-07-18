package cn.superid.webapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * java bean的属性的field/get-method/set-method的wrapper
 * Created by zoowii on 14/10/24.
 */
public class FieldAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(FieldAccessor.class);
    private Field field;
    private Method getMethod;
    private Method setMethod;
    private final Class cls;
    private final String name;

    public static class FieldAccessException extends RuntimeException {
        public FieldAccessException(Exception e) {
            super(e);
        }

        public FieldAccessException(String msg) {
            super(msg);
        }
    }

    public FieldAccessor(Class cls, @NotNull String name) {
        this.cls = cls;
        this.name = name;
        try {
            field = cls.getDeclaredField(name);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LOG.error(String.format("field %s not found", name));
            field = null;
        }
        getMethod = getMethodOfClassByName(cls, getGetMethodName());
        setMethod = getMethodOfClassByName(cls, getSetMethodName());
    }

    private Method getMethodOfClassByName(Class cls, String name) {
        for (Method method : cls.getMethods()) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    public Object getProperty(Object obj) {
        if (getMethod != null) {
            try {
                return getMethod.invoke(obj);
            } catch (Exception e) {
                throw new FieldAccessException(e);
            }
        }
        if (field != null) {
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                throw new FieldAccessException(e);
            }
        }
        throw new FieldAccessException(String.format("Can't find accessor of get property value of %s", name));
    }

    public void setProperty(Object obj, Object value) {
        if (setMethod != null) {
            try {
                setMethod.invoke(obj, value);
                return;
            } catch (Exception e) {
                throw new FieldAccessException(e);
            }
        }
        if (field != null) {
            try {
                field.set(obj, value);
                return;
            } catch (IllegalAccessException e) {
                throw new FieldAccessException(e);
            }
        }
        throw new FieldAccessException(String.format("Can't find accessor of set property %s", name));
    }

    private String upperFirstChar(String str) {
        if (str == null) {
            return null;
        }
        String s = str.trim();
        if (s.length() < 1) {
            return s;
        }
        return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
    }

    /**
     * 获取get属性方法的名称
     *
     * @return
     */
    private String getGetMethodName() {
        assert name != null;
        if (field != null && field.getType() == Boolean.class) {
            return "is" + upperFirstChar(name);
        }
        return "get" + upperFirstChar(name);
    }

    /**
     * 获取set属性方法的名称
     *
     * @return
     */
    private String getSetMethodName() {
        assert name != null;
        return "set" + upperFirstChar(name);
    }
}
