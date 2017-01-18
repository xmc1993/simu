package cn.superid.utils;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;

/**
 * Created by jessiechen on 18/01/17.
 */
public class Converter {

    private static Map<CopierIdentity, BeanCopier> copierCache = Maps.newConcurrentMap();
    private static PrimitiveConverter primitiveConverter = new PrimitiveConverter();

    public static <T> T copy(T target, Object source) {
        BeanCopier copier = getCopier(source.getClass(), target.getClass());
        copier.copy(source, target, primitiveConverter);
        return target;
    }

    public static <T> T convert(Class<T> targetClass, Object source) {
        try {
            T target = targetClass.newInstance();
            BeanCopier copier = getCopier(source.getClass(), targetClass);
            copier.copy(source, target, primitiveConverter);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static BeanCopier getCopier(Class<?> source, Class<?> target) {
        CopierIdentity identity = new CopierIdentity(source, target);
        BeanCopier copier;
        if (copierCache.containsKey(identity)) {
            copier = copierCache.get(identity);
        } else {
            copier = BeanCopier.create(source, target, true);
            if (copierCache.get(identity) == null)
                copierCache.put(identity, copier);
        }
        return copier;
    }

    public static class PrimitiveConverter implements org.springframework.cglib.core.Converter {
        @Override
        @SuppressWarnings("unchecked")
        public Object convert(Object value, Class target, Object context) {
            if (value == null) return null;
            if (target.equals(String.class)
                    && !String.class.isAssignableFrom(value.getClass()))
                return value.toString();
            if (Number.class.isAssignableFrom(value.getClass())) {
                Number num = (Number) value;
                if (target.equals(int.class) || target.equals(Integer.class))
                    return num.intValue();
                else if (target.equals(long.class) || target.equals(Long.class))
                    return num.longValue();
                else if (target.equals(short.class) || target.equals(Short.class))
                    return num.shortValue();
                else if (target.equals(float.class) || target.equals(Float.class))
                    return num.floatValue();
                else if (target.equals(double.class) || target.equals(Double.class))
                    return num.doubleValue();
                else if (target.equals(byte.class) || target.equals(Byte.class))
                    return num.byteValue();
            } else if (target.isAssignableFrom(value.getClass()))
                return value;
            return null;
        }
    }

    private static class CopierIdentity {
        private Class<?> source;
        private Class<?> target;

        public CopierIdentity(Class<?> source, Class<?> target) {
            this.source = source;
            this.target = target;
        }

        public Class<?> getSource() {
            return source;
        }

        public void setSource(Class<?> source) {
            this.source = source;
        }

        public Class<?> getTarget() {
            return target;
        }

        public void setTarget(Class<?> target) {
            this.target = target;
        }
    }

}
