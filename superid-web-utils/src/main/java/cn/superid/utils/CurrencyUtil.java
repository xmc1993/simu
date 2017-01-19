package cn.superid.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by zp on 2016/7/9.
 * 每隔一小时取一次汇率，如果保存借结果没有超时，则使用保存结果
 */
public class CurrencyUtil {
//    private static  String basic = "CNY";
//    private static String httpUrl =  "http://apis.baidu.com/apistore/currencyservice/currency";
//    private static HashMap<String,Pair<Double,Calendar>> cache =new HashMap<>();
//
//
//    public static Double convert(String from,String to){
//        Double rate;
//        if(basic.equals(to)){
//           rate= request(from,to);
//        }else {
//            Double rate1= request(from,basic);
//            Double rate2= request(to,basic);
//            rate =(double) (Math.round((rate1/rate2)*10000))/10000;
//        }
//        return rate;
//    }
//
//    public static Double request(String from,String to) {
//        Pair<Double,Calendar> pair=cache.get(from);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
//        if(pair!=null&&pair.getKey()!=null&&pair.getValue().after(calendar)){
//            return pair.getKey();
//        }
//        Double rate=new Double(0);
//        BufferedReader reader = null;
//        StringBuilder stringBuilder=new StringBuilder(httpUrl);
//        stringBuilder.append("?fromCurrency=");
//        stringBuilder.append(from);
//        stringBuilder.append("&toCurrency=");
//        stringBuilder.append(to);
//        stringBuilder.append("&amount=");
//        stringBuilder.append(1);
//        StringBuffer sbf = new StringBuffer();
//        try {
//            URL url = new URL(stringBuilder.toString());
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("apikey",  "10b53994af37351ea8941fb8ca01d305");
//            connection.connect();
//            InputStream is = connection.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String strRead = null;
//            while ((strRead = reader.readLine()) != null) {
//                sbf.append(strRead);
//                sbf.append("\r\n");
//            }
//            reader.close();
//            JSONObject jsonObject=new JSONObject(sbf.toString());
//            jsonObject =jsonObject.getJSONObject("retData");
//            rate = jsonObject.getDouble("currency");
//            Calendar calendar1 = Calendar.getInstance();
//            Pair<Double,Calendar> pair1=new Pair(rate,calendar1);
//            cache.put(from,pair1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return rate;
//    }
}
