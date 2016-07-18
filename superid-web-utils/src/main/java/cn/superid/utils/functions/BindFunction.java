package cn.superid.utils.functions;

import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Method;


/**
 * Created by hlr@superid.cn on 2014/9/11.
 */
@SuppressWarnings("unchecked")
public class BindFunction<T> {
    private MethodAccess methodAccess;
    private Object methodInstance;
    private int methodIndex;
    private Object[] passedArgs;
    private int argIndex;
    private int argsSize;
    private boolean isDirect = false;
    private Method directMethod = null;

    public static <T> BindFunction<T> bind(Object methodInstance, String methodName, Object... args) {
        return new BindFunction<>(methodInstance, methodName, args);
    }

    public static <T> BindFunction<T> bindDirect(Object methodInstance, Method method, Object... args) {
        return new BindFunction<T>(methodInstance, method, args);
    }

    public static <T> BindFunction<T> bind(Object methodInstance, Class[] parameterType, String methodName, Object... args) {
        return new BindFunction<>(methodInstance, methodName, parameterType, args);
    }

    private BindFunction(Object methodInstance, String methodName, Class[] parameterType, Object... args) {
        MethodAccess ma = MethodAccess.get(methodInstance.getClass());
        int index = ma.getIndex(methodName, parameterType);
        initFiled(ma, index, methodInstance, args);
    }

    private BindFunction(Object methodInstance, String methodName, Object... args) {
        MethodAccess ma = MethodAccess.get(methodInstance.getClass());
        int index = ma.getIndex(methodName);
        checkMethodDuplicated(ma, methodName);
        initFiled(ma, index, methodInstance, args);
    }

    private BindFunction(Object methodInstance, Method method, Object... args) {
        isDirect = true;
        this.methodInstance = methodInstance;
        directMethod = method;
        argIndex = args.length;
        argsSize = method.getParameterTypes().length;
        passedArgs = new Object[argsSize];
        System.arraycopy(args, 0, passedArgs, 0, args.length);
    }

    private void initFiled(MethodAccess methodAccess, int methodIndex, Object methodInstance, Object... args) {
        this.methodAccess = methodAccess;
        this.methodInstance = methodInstance;
        this.methodIndex = methodIndex;
        this.argIndex = args.length;
        this.argsSize = methodAccess.getParameterTypes()[methodIndex].length;
        this.passedArgs = new Object[argsSize];
        checkArgsSize(argsSize, args);
        System.arraycopy(args, 0, passedArgs, 0, args.length);
    }

    public T call(Object... args) {
        injectArgs(args);
        if (isDirect && directMethod != null) {
            try {
                return (T) directMethod.invoke(methodInstance, passedArgs);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return (T) methodAccess.invoke(methodInstance, methodIndex, passedArgs);
        }
    }

    private void checkArgsSize(int argsSize, Object... args) {
        if (args.length > argsSize)
            throw new RuntimeException("parameters size is less than passedArgs size!");
    }

    private void checkMethodDuplicated(MethodAccess methodAccess, String methodName) {
        String names[] = methodAccess.getMethodNames();
        int count = 0;
        for (String name : names) {
            if (name.equals(methodName)) {
                count++;
                if (count > 1)
                    throw new RuntimeException("Duplicated method name! methodName = " + methodName);
            }
        }
    }

    private void injectArgs(Object... args) {
        System.arraycopy(args, 0, passedArgs, argIndex, argsSize - argIndex);
    }
}
