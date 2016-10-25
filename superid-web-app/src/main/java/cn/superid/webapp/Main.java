package cn.superid.webapp;

import cn.superid.webapp.forms.CountryCode;
import cn.superid.webapp.utils.PathUtil;
import cn.superid.webapp.utils.jetty.JettyServer;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String detectWebAppDir() {
        return PathUtil.getWebRootPath();
    }

    public static void main(String[] args) {

        String log4jConfPath = detectWebAppDir()+"/src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        JettyServer jettyServer = new JettyServer(detectWebAppDir(), 9855, "/", 5);
        jettyServer.start();
    }


}
