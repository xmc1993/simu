package cn.superid.jpa.annotation;

import java.lang.annotation.ElementType;

/**
 * Created by xiaofengxu on 17/1/10.
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface CacheField {
    int order() ;//must >0
}
