package cn.superid.webapp.utils.cache.annotations;

/**
 * Created by zoowii on 14/10/20.
 */
public @interface Cache {
    String value() default "default"; // cache组名
}
