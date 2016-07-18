package cn.superid.utils.functions;

/**
 * Created by zoowii on 2014/11/21.
 */
public interface Function3<T1, T2, T3, R> {
    R apply(T1 param1, T2 param2, T3 param3);
}
