package cn.superid.webapp.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zp on 2016/8/4.
 */
public class CheckFrequencyUtil {

    private static class ValueAttr{

        private int limit;
        private Date begin;
        private int count;
        private int seconds;

        private ValueAttr( int count, int limit, Date begin,int seconds) {

            this.limit = limit;
            this.begin = begin;
            this.count = count;
            this.seconds = seconds;
        }

        private ValueAttr(){}



        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public Date getBegin() {
            return begin;
        }

        public void setBegin(Date begin) {
            this.begin = begin;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }
    }

    private  final static int defaultLimit =5;
    private final static int defaultTime = 24*60*60;

    private static final Map<String,  ValueAttr> cache = new ConcurrentHashMap<>();
    
    public static boolean isFrequent(String key){
        ValueAttr valueAttr = new ValueAttr(1,defaultLimit,new Date(), defaultTime);
        return checkFrequent(key,valueAttr);
    }

    public static boolean isFrequent(String key,int limit){
        ValueAttr valueAttr = new ValueAttr(1,limit,new Date(), defaultTime);
        return checkFrequent(key,valueAttr);
    }

    public static boolean isFrequent(int timeoutSeconds,String key){
        ValueAttr valueAttr = new ValueAttr(1,defaultLimit,new Date(), timeoutSeconds);
        return checkFrequent(key,valueAttr);
    }

    public static boolean isFrequent(String key,int limit,int  timeoutSeconds){
        ValueAttr valueAttr = new ValueAttr(1,limit,new Date(), timeoutSeconds);
        return checkFrequent(key,valueAttr);
    }

    public static void reset(String token){
        ValueAttr cachedValue = cache.get(token);
        cachedValue.setBegin(new Date());
        cachedValue.setCount(1);
    }

    public static int getCounts(String key){
        ValueAttr cachedValue = cache.get(key);
        return cachedValue.getCount();
    }

    private static boolean checkFrequent(String key,ValueAttr valueAttr){
        ValueAttr cachedValue = cache.get(key);
        if(cachedValue==null){
            cache.put(key,valueAttr);
            return false;
        }else{
            int time = cachedValue.getSeconds();
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cachedValue.getBegin());
            calendar.add(Calendar.SECOND, time);


            if(calendar.after(date)){//过了规定时间再访问，则重新计数
                cachedValue.setCount(1);
                cachedValue.setBegin(date);
                return false;
            }

            int count =cachedValue.getCount();
            count++;
            if(count<cachedValue.getLimit()){
                cachedValue.setCount(count);
                return false;
            }else{
                return true;
            }
        }
    }
}
