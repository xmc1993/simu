package cn.superid.utils;

import java.lang.management.ManagementFactory;

/**
 * Created by zoowii on 2014/9/9.
 */
public class ProcessUtil {
    public static int getCurrentProcessId() {

        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            return Integer.valueOf(pid);
        } catch (Exception e) {
            return -1;
        }
    }
}
