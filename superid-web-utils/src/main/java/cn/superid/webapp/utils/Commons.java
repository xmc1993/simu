package cn.superid.webapp.utils;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zoowii on 15/4/30.
 */
public class Commons {
    public static Map<String, Object> toMap(Object bean) {
        if(bean == null) {
            return null;
        }
        return BeanToMapUtil.beanToMap(bean);
//        Map<String, Object> map = new HashMap<>();
//        PropertyDescriptor[] propertyDescriptors = org.springframework.beans.BeanUtils.getPropertyDescriptors(bean.getClass());
//        for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//            try {
//                map.put(propertyDescriptor.getName(), propertyDescriptor.getValue(propertyDescriptor.getName()));
//            }catch(Exception e) {}
//        }
//        return map;
    }

    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
