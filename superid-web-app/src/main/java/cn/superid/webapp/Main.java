package cn.superid.webapp;

import cn.superid.webapp.utils.PathUtil;
import cn.superid.webapp.utils.jetty.JettyServer;

public class Main {
    private static String detectWebAppDir() {
        return PathUtil.getWebRootPath();
    }

    public static void main(String[] args) {
        JettyServer jettyServer = new JettyServer(detectWebAppDir(), 8080, "/", 5);
        jettyServer.start();
    }

}
