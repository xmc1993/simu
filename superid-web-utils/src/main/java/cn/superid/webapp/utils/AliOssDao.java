package cn.superid.webapp.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyun.oss.common.utils.BinaryUtil;

import com.aliyun.oss.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zp on 2016/4/21.
 */
public class AliOssDao {
    private static final Logger LOG = LoggerFactory.getLogger(AliOssDao.class);
    private static String MTS_SERVER_URL = "http://mts.aliyuncs.com/";
    private final static String endpoint ="oss-cn-shanghai.aliyuncs.com";
    private final static String accessId ="LTAIJ1mKhbLQyBnK";
    private final static String accessKey="DHnYFEI9gF1CMhh1fzSlwj8MkjjPnK";
    private final static String bucket = "simucy";
    private final static String host = "http://simucy.oss-cn-shanghai.aliyuncs.com";
    private final static String location = "oss-cn-shanghai";

    private final static OSSClient client = new OSSClient(endpoint, accessId, accessKey);
    public static Map<String, String> generateToken(String dir){
        try {
            long expireTime = 300;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0,500000000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            return respMap;
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    public static Map<String,String> getVideoUploadSignature(String objectKey,String method){
        long expireTime = 300;
        long expireEndTime = System.currentTimeMillis()+expireTime*1000;
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        StringBuilder canonicalString = new StringBuilder();
        canonicalString.append(method + "\n\n\n");
       // canonicalString.append(DateUtil.formatRfc822Date(new Date()));

        canonicalString.append(expireEndTime);
        canonicalString.append("\n");
        switch (method){
            case "POST":
                canonicalString.append("/"+bucket+"/"+objectKey+"?uploads");
                break;
            case "PUT":
                canonicalString.append("/"+bucket+"/"+objectKey);
                break;
            default:
                break;
        }

        String signature = ServiceSignature.create().computeSignature(accessKey, canonicalString.toString());
        respMap.put("signature",signature);
        respMap.put("host", host);
        respMap.put("accessid", accessId);
        respMap.put("expire",expireEndTime+"");
        respMap.put("dir","/"+objectKey);
        return respMap;
    }

    public static Map<String,String> getVideoUploadSignature(String verb,String md5,String type,String header,String resource){
        long expireTime = 300;
        long expireEndTime = System.currentTimeMillis()+expireTime*1000;
        Map<String, String> respMap = new LinkedHashMap<String, String>();
        StringBuilder canonicalString = new StringBuilder();
        md5 = md5 == null ? "" : md5;
        type = type ==null ? "":type;
        header = header ==null ? "":header;
        canonicalString.append(verb + "\n"+md5+"\n"+type+"\n");

        // canonicalString.append(DateUtil.formatRfc822Date(new Date()));
        canonicalString.append(expireEndTime);
        canonicalString.append("\n");
        canonicalString.append(header+resource);
        String signature = ServiceSignature.create().computeSignature(accessKey, canonicalString.toString());
        respMap.put("host",host);
        respMap.put("signature",signature);
        respMap.put("expireTime",expireEndTime+"");
        respMap.put("accessId",accessId);
        return respMap;
    }


    public static PutObjectResult uploadFile(File file, String dir){
        OSSClient ossClient = new OSSClient(endpoint,accessId,accessKey);
        PutObjectResult putResult = null;
        try{
            putResult = ossClient.putObject(bucket,dir,file);
        }catch(Exception e){
            LOG.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }

        return putResult;

    }


    /**
     * @author tms
     * @param fileUrl
     */
    public static void deleteOldFile(String fileUrl){
        //OSSClient client = new OSSClient(endpoint, accessId, accessKey);
        try{
            client.deleteObject(bucket,fileUrl);
        } catch (Exception e){
            LOG.error(e.getMessage());
        }
    }


}
