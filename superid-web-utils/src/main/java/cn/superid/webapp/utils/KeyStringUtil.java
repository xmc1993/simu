package cn.superid.webapp.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Qing on 2015/9/2.
 */
public class KeyStringUtil {
    public static String generateKeyByTwoIds(String id1, String id2) {
        List<String> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);
        Collections.sort(ids);
        return ids.get(0) + "$" + ids.get(1);
    }
}
