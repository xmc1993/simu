package cn.superid.jpa.annotation;

import java.lang.annotation.ElementType;

/**
 * Created by xiaofengxu on 16/9/9.
 */

@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface PartitionId {
}
