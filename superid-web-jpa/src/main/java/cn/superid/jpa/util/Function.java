package cn.superid.jpa.util;

/**
 * Created by zp on 2016/7/25.
 */
public interface Function<T,R> {
    public R apply(T t);
}
