package cn.superid.admin;

import cn.superid.webapp.utils.PathUtil;
import cn.superid.webapp.utils.jetty.JettyServer;

/**
 * Created by njuTms on 16/10/9.
 */
public class Main {
    private static String detectWebAppDir() {
        return PathUtil.getWebRootPath();
    }

    public static void main(String[] args) {
        JettyServer jettyServer = new JettyServer(detectWebAppDir(), 9600, "/", 5);
        jettyServer.start();
    }

}
