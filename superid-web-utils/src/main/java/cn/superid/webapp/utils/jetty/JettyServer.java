package cn.superid.webapp.utils.jetty;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import cn.superid.utils.StringUtil;
import cn.superid.webapp.utils.PathUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by zoowii on 14/10/29.
 */
public class JettyServer implements IServer {
    private String webAppDir;
    private int port;
    private String context;
    private int scanIntervalSeconds;
    private boolean running = false;
    private Server server;
    private WebAppContext webApp;

    public JettyServer(String webAppDir, int port, String context, int scanIntervalSeconds) {
        if (webAppDir == null)
            throw new IllegalStateException("Invalid webAppDir of web server: " + webAppDir);
        if (port < 0 || port > 65536)
            throw new IllegalArgumentException("Invalid port of web server: " + port);
        if (StringUtil.isEmpty(context))
            throw new IllegalStateException("Invalid context of web server: " + context);

        this.webAppDir = webAppDir;
        this.port = port;
        this.context = context;
        this.scanIntervalSeconds = scanIntervalSeconds;
    }

    public void start() {
        if (!running) {
            try {
                doStart();
            } catch (Exception e) {
                e.printStackTrace();
            }
            running = true;
        }
    }

    public void stop() {
        if (running) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            running = false;
        }
    }

    private WebAppContext createWebAppContext() {
        WebAppContext webApp = new WebAppContext();
        webApp.setContextPath(context);
        webApp.setResourceBase(webAppDir + "/target/classes");    // webApp.setWar(webAppDir);
        webApp.setDescriptor(webAppDir + "/target/classes/WEB-INF/web.xml");

        webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");    // webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));

        persistSession(webApp);
        return webApp;
    }

    private boolean holding = false;

    public boolean isHolding() {
        return holding;
    }

    private void doStart() {
        if (!available(port))
            throw new IllegalStateException("port: " + port + " already in use!");

        deleteSessionData();

        System.out.println("Starting Webd App ");
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);
        webApp = createWebAppContext();

        server.setHandler(webApp);
        changeClassLoader(webApp);

        // configureScanner

        if (scanIntervalSeconds > 0) {
            Scanner scanner = new Scanner(PathUtil.getWebRootPath(), scanIntervalSeconds) {
                public void onChange() {
                    try {
                        System.err.println("\nLoading changes ......");
                        webApp.stop();
                        holding = true;
                        server.stop();
                        doStart();
//                        webApp = createWebAppContext();
//                        server.setHandler(webApp);
                        changeClassLoader(webApp);
//                        server.start();
//                        ClassLoader classLoader = changeClassLoader(webApp);
//                        webApp.setClassLoader(classLoader);
//                        webApp.start();
                        System.err.println("Loading complete.");
                    } catch (Exception e) {
                        System.err.println("Error reconfiguring/restarting webapp after change in watched files");
                        e.printStackTrace();
                    }
                }
            };
            System.out.println("Starting scanner at interval of " + scanIntervalSeconds + " seconds.");
//            scanner.start();
        }

        try {
            System.out.println("Starting web server on port: " + port);
            server.start();
            System.out.println("Starting Complete. Welcome To The Java Web World :)");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
        return;
    }

    @SuppressWarnings("resource")
    private ClassLoader changeClassLoader(WebAppContext webApp) {
        try {
            String classPath = getClassPath();
            JettyClassLoader wacl = new JettyClassLoader(webApp, classPath);
            wacl.addClassPath(classPath);
            return wacl;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getClassPath() {
        return System.getProperty("java.class.path");
    }

    private void  deleteSessionData() {
        try {
            PathUtil.delete(new File(getStoreDir()));
        } catch (Exception e) {
        }
    }

    private String getStoreDir() {
        String storeDir = PathUtil.getWebRootPath() + "/../../session_data" + context;
        if ("\\".equals(File.separator))
            storeDir = storeDir.replaceAll("/", "\\\\");
        return storeDir;
    }

    private void persistSession(WebAppContext webApp) {
        String storeDir = getStoreDir();

        SessionManager sm = webApp.getSessionHandler().getSessionManager();
        if (sm instanceof HashSessionManager) {
            ((HashSessionManager) sm).setStoreDirectory(new File(storeDir));
            return;
        }

        HashSessionManager hsm = new HashSessionManager();
        hsm.setStoreDirectory(new File(storeDir));
        SessionHandler sh = new SessionHandler();
        sh.setSessionManager(hsm);
        webApp.setSessionHandler(sh);
    }

    private static boolean available(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    // should not be thrown, just detect port available.

                }
            }
        }
        return false;
    }
}
