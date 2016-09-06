package cn.superid.webapp.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zoowii on 14/10/13.
 */
public class TimeUtil {
    public static int getCurrentTimeSeconds() {
        return (int) (new Date().getTime() / 1000);
    }

    public Date getDateFromSeconds(int seconds) {
        return new Date(seconds * 1000);
    }

    public static Timestamp getCurrentSqlTime() {
        return new Timestamp(new Date().getTime());
    }

    public static Timestamp getCurrentSqlTimeDeltaBefore(int delta) {
        return new Timestamp(new Date().getTime() - delta);
    }

    public static  String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return  df.format(new Date());
    }




}
