package cn.superid.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zoowii on 2014/9/1.
 */
public class ConfigUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigUtil.class);
    private static final String configFilePath = "META-INF/config.properties";

    public static Properties getProperties(String filePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getConfig(String key) {
        Properties properties = getProperties(configFilePath);
        if (properties == null) {
            throw new RuntimeException("config file " + configFilePath + " not found or is not a properties file");
        }
        return properties.getProperty(key);
    }

    public static String getConfig(String key, String defaultValue) {
        String value = getConfig(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    public static Integer getIntConfig(String key, int defaultValue) {
        String str = getConfig(key, "0");
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer getIntConfig(String key) {
        return getIntConfig(key, 0);
    }

    /**
     * 首先从ENV中读取,没有再从配置文件中读取配置
     *
     * @param key
     * @return
     */
    public static String getConfigFromEnvOrProperties(String key) {
        String value = System.getenv(key);
        if (value == null) {
            return getConfig(key);
        } else {
            return value;
        }
    }
}
