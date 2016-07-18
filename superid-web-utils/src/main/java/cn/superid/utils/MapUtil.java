package cn.superid.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 维 on 2014/9/4.
 */
public class MapUtil {
    public static Map<String, Object> hashmap(Object... params) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < params.length; i += 2) {
            if (i + 1 >= params.length) {
                break;
            }
            Object param1 = params[i];
            Object param2 = params[i + 1];
            if (param1 == null) {
                continue;
            }
            String key = param1.toString();
            result.put(key, param2);
        }
        return result;
    }

    public static Map<String, Object> fromBean(Object bean) {
        if(bean == null) {
            return null;
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(bean);
        Map<String, Object> result = new HashMap<>();
        for(String key : jsonObject.keySet()) {
            result.put(key, jsonObject.get(key));
        }
        return result;
    }
}
