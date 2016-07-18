package cn.superid.utils;

import com.google.common.base.Function;
import org.jsoup.nodes.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hlr@superid.cn on 2014/9/5.
 */
public class BeanUtil {

    public static <T> T createBeanFromElement(Element element) {
        String className = element.attr("class");
        List<Element> properties = element.select("property");
        try {
            Class cls = Class.forName(className);
            Constructor constructor = cls.getConstructor();
            Object instance = constructor.newInstance();
            Method[] methods = cls.getDeclaredMethods();
            for (Element ele : properties) {
                String propertyName = ele.attr("name");
                String topCharacter = propertyName.substring(0, 1);
                final String methodName = "set" + StringUtil.replaceWithSubStr(propertyName, topCharacter.toUpperCase(), 0);
                Method methodToInvoke = ListUtil.first(Arrays.asList(methods), new Function<Method, Boolean>() {
                    @Override
                    public Boolean apply(Method method) {
                        return method.getName().equals(methodName);
                    }
                });
                Class parameterType = methodToInvoke.getParameterTypes()[0];
                Object arg = getArgFromType(parameterType, ele.text());
                methodToInvoke.invoke(instance, arg);
            }
            return (T) instance;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getArgFromType(Class parameterType, String text) {
        String paraTypeName = parameterType.getName();
        if (paraTypeName.equals(Integer.class.getName())
                || paraTypeName.equals("int")) {
            return Integer.parseInt(text);
        } else if (paraTypeName.equals(Double.class.getName())
                || paraTypeName.equals("double")) {
            return Double.parseDouble(text);
        } else {
            return text;
        }
    }
}
