package cn.superid.utils.functions;

/**
 * 2个参数的函数类
 * Created by 维 on 2014/9/5.
 */
public interface Function2<T1, T2, R> {
    public R apply(T1 t1, T2 t2);
}
