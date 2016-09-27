package cn.superid.webapp;

import cn.superid.webapp.utils.PathUtil;
import cn.superid.webapp.utils.jetty.JettyServer;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by xiaofengxu on 16/9/9.
 */
public class DevelopMode {
    private static String detectWebAppDir() {
        return PathUtil.getWebRootPath();
    }

    public static void main(String[] args) {
        String log4jConfPath = detectWebAppDir()+"/src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        JettyServer jettyServer = new JettyServer(detectWebAppDir(), 9000, "/", 5);
        jettyServer.start();
    }
}
