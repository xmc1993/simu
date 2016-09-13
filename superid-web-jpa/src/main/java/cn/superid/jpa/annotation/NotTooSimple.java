package cn.superid.jpa.annotation;

import java.lang.annotation.ElementType;

/**
 * Created by zp on 2016/7/18.续命1s
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface NotTooSimple {
}
