package cn.superid.webapp;

import cn.superid.webapp.utils.PathUtil;
import cn.superid.webapp.utils.jetty.JettyServer;

/**
 * Created by xiaofengxu on 16/9/9.
 */
public class DevelopMode {
    private static String detectWebAppDir() {
        return PathUtil.getWebRootPath();
    }

    public static void main(String[] args) {
        JettyServer jettyServer = new JettyServer(detectWebAppDir(), 9000, "/", 5);
        jettyServer.start();
    }
}
