package cn.superid.webapp.enums.state;

/**
 * Created by njuTms on 16/11/30.
 * 文件状态
 */
public class FileState {
    public static int Invalid = 0; //失效
    public static int LatestVersion = 1; //生效且是最新版本
    public static int HistoryVersion = 2; //生效且是历史版本
}
