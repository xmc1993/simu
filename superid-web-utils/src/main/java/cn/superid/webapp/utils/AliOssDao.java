package cn.superid.webapp.utils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.api.AliyunClient;
import com.aliyun.api.AliyunRequest;
import com.aliyun.api.AliyunResponse;
import com.aliyun.api.DefaultAliyunClient;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.ServiceSignature;
import com.aliyun.oss.common.comm.RequestMessage;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.internal.OSSMultipartOperation;
import com.aliyun.oss.internal.OSSRequestMessageBuilder;
import com.aliyun.oss.model.*;
import com.taobao.api.ApiException;
import com.taobao.api.ApiRuleException;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import com.taobao.api.internal.util.RequestCheckUtils;
import com.taobao.api.internal.util.TaobaoHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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
    private final static AliyunClient aliyunClient = new DefaultAliyunClient(MTS_SERVER_URL,accessId,accessKey);

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
        System.out.println(expireEndTime+" "+signature);
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
        System.out.println(expireEndTime);
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

    /**
     * @author tms
     * @param videoUrl,userType
     */
    public static String snapshotVideoJobFlow(String videoUrl){
        int pos = videoUrl.indexOf("user/");//userType表示存放在user下还是affair下
        String path = videoUrl.substring(pos);//截取除了endpoint外的路径
        OSSFileUtil inputVideoFile = new OSSFileUtil(location,bucket,path);
        //只提交不管结果如何
//        String snapshotJobId = submitSnapshotJob(inputVideoFile,path);
//        SnapshotJob job = waitSnapshotComplete(snapshotJobId);
//
//        SnapshotJob.SnapshotConfig.OutputFile outputFile = job.getSnapshotConfig().getOutputFile();
        return host+"/"+submitSnapshotJob(inputVideoFile,path);

    }

    /**
     * @author tms
     * @param videoFile,path
     * @return snapshotJobID
     */
    private static String submitSnapshotJob(OSSFileUtil videoFile,String temp) {
        String path = temp.substring(0, temp.length() - 3);//将后缀名除去
        OSSFileUtil outputSnapShotFile = new OSSFileUtil();
        outputSnapShotFile.setBucket(bucket);
        outputSnapShotFile.setLocation(location);
        outputSnapShotFile.setObject(path + "jpg");
        JSONObject jobConfig = new JSONObject();
        jobConfig.put("OutputFile", outputSnapShotFile.toJson());
        jobConfig.put("Time", 10);

        SubmitSnapshotJobRequest request = new SubmitSnapshotJobRequest();
        request.setSnapshotConfig(jobConfig.toJSONString());
        request.setInput(videoFile.toJsonString());
        try {
            SubmitSnapshotJobResponse response = aliyunClient.execute(request);
            if (response.getErrorCode() != null) {
                throw new RuntimeException(response.getMessage());
            }
            return outputSnapShotFile.getObject();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @author tms
     * @param snapshotJobId
     * @return snapshotJob
     */
    private static SnapshotJob waitSnapshotComplete(String snapshotJobId){
        QuerySnapshotJobListRequest request = new QuerySnapshotJobListRequest();
        request.setSnapshotJobIds(snapshotJobId);

        try{
            while (true){
                QuerySnapshotJobListResponse response = aliyunClient.execute(request);
                if(response.getSnapshotJobList()==null) {
                    throw new RuntimeException("QuerySnapshotJobListRequest failed");
                }

                SnapshotJob job = response.getSnapshotJobList().get(0);
                String status = job.getState();

                if ("Fail".equals(status)) {
                    throw new RuntimeException("QuerySnapshotJobListRequest Failed");
                }
                if ("Success".equals(status)) {
                    return job;
                }

                Thread.sleep(2 * 1000);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static class SubmitSnapshotJobRequest implements AliyunRequest<SubmitSnapshotJobResponse> {
        private Map<String, String> headerMap = new TaobaoHashMap();
        private TaobaoHashMap udfParams;
        private Long timestamp;
        private String ownerId;
        private String ownerAccount;
        private String resourceOwnerAccount;
        private String input;
        private String snapshotConfig;

        public SubmitSnapshotJobRequest() {
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getInput() {
            return this.input;
        }

        public void setSnapshotConfig(String snapshotConfig) {
            this.snapshotConfig = snapshotConfig;
        }

        public String getSnapshotConfig() {
            return this.snapshotConfig;
        }

        public String getOwnerId() {
            return this.ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getOwnerAccount() {
            return this.ownerAccount;
        }

        public void setOwnerAccount(String ownerAccount) {
            this.ownerAccount = ownerAccount;
        }

        public String getResourceOwnerAccount() {
            return this.resourceOwnerAccount;
        }

        public void setResourceOwnerAccount(String resourceOwnerAccount) {
            this.resourceOwnerAccount = resourceOwnerAccount;
        }

        public Long getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getApiMethodName() {
            return "mts.aliyuncs.com.SubmitSnapshotJob.2014-06-18";
        }

        public Map<String, String> getTextParams() {
            TaobaoHashMap txtParams = new TaobaoHashMap();
            txtParams.put("OwnerId", this.ownerId);
            txtParams.put("OwnerAccount", this.ownerAccount);
            txtParams.put("ResourceOwnerAccount", this.resourceOwnerAccount);
            txtParams.put("Input", this.input);
            txtParams.put("SnapshotConfig", this.snapshotConfig);
            if(this.udfParams != null) {
                txtParams.putAll(this.udfParams);
            }

            return txtParams;
        }

        public void putOtherTextParam(String key, String value) {
            if(this.udfParams == null) {
                this.udfParams = new TaobaoHashMap();
            }

            this.udfParams.put(key, value);
        }

        public Class<SubmitSnapshotJobResponse> getResponseClass() {
            return SubmitSnapshotJobResponse.class;
        }

        public void check() throws ApiRuleException {
            RequestCheckUtils.checkNotEmpty(this.input, "input");
            RequestCheckUtils.checkNotEmpty(this.snapshotConfig, "snapshotConfig");
        }

        public Map<String, String> getHeaderMap() {
            return this.headerMap;
        }
    }

    public static class SubmitSnapshotJobResponse extends AliyunResponse {
        private String requestId;
        private SnapshotJob snapshotJob;

        public SubmitSnapshotJobResponse() {
        }

        public String getRequestId() {
            return this.requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public SnapshotJob getSnapshotJob() {
            return this.snapshotJob;
        }

        public void setSnapshotJob(SnapshotJob snapshotJob) {
            this.snapshotJob = snapshotJob;
        }


    }

    public   static class QuerySnapshotJobListRequest implements AliyunRequest<QuerySnapshotJobListResponse> {
        private Map<String, String> headerMap = new TaobaoHashMap();
        private TaobaoHashMap udfParams;
        private Long timestamp;
        private String ownerId;
        private String ownerAccount;
        private String resourceOwnerAccount;
        private String snapshotJobIds;

        public QuerySnapshotJobListRequest() {
        }

        public void setSnapshotJobIds(String snapshotJobIds) {
            this.snapshotJobIds = snapshotJobIds;
        }

        public String getSnapshotJobIds() {
            return this.snapshotJobIds;
        }

        public String getOwnerId() {
            return this.ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getOwnerAccount() {
            return this.ownerAccount;
        }

        public void setOwnerAccount(String ownerAccount) {
            this.ownerAccount = ownerAccount;
        }

        public String getResourceOwnerAccount() {
            return this.resourceOwnerAccount;
        }

        public void setResourceOwnerAccount(String resourceOwnerAccount) {
            this.resourceOwnerAccount = resourceOwnerAccount;
        }

        public Long getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getApiMethodName() {
            return "mts.aliyuncs.com.QuerySnapshotJobList.2014-06-18";
        }

        public Map<String, String> getTextParams() {
            TaobaoHashMap txtParams = new TaobaoHashMap();
            txtParams.put("OwnerId", this.ownerId);
            txtParams.put("OwnerAccount", this.ownerAccount);
            txtParams.put("ResourceOwnerAccount", this.resourceOwnerAccount);
            txtParams.put("SnapshotJobIds", this.snapshotJobIds);
            if(this.udfParams != null) {
                txtParams.putAll(this.udfParams);
            }

            return txtParams;
        }

        public void putOtherTextParam(String key, String value) {
            if(this.udfParams == null) {
                this.udfParams = new TaobaoHashMap();
            }

            this.udfParams.put(key, value);
        }

        public Class<QuerySnapshotJobListResponse> getResponseClass() {
            return QuerySnapshotJobListResponse.class;
        }

        public void check() throws ApiRuleException {
            RequestCheckUtils.checkNotEmpty(this.snapshotJobIds, "snapshotJobIds");
        }

        public Map<String, String> getHeaderMap() {
            return this.headerMap;
        }
    }

    public static class QuerySnapshotJobListResponse extends AliyunResponse {
        private static final long serialVersionUID = 2356914861756121633L;
        @ApiListField("NonExistSnapshotJobIds")
        @ApiField("String")
        private List<String> nonExistSnapshotJobIds;
        @ApiField("RequestId")
        private String requestId;
        @ApiListField("SnapshotJobList")
        @ApiField("SnapshotJob")
        private List<SnapshotJob> snapshotJobList;

        public QuerySnapshotJobListResponse() {
        }

        public void setNonExistSnapshotJobIds(List<String> nonExistSnapshotJobIds) {
            this.nonExistSnapshotJobIds = nonExistSnapshotJobIds;
        }

        public List<String> getNonExistSnapshotJobIds() {
            return this.nonExistSnapshotJobIds;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getRequestId() {
            return this.requestId;
        }

        public void setSnapshotJobList(List<SnapshotJob> snapshotJobList) {
            this.snapshotJobList = snapshotJobList;
        }

        public List<SnapshotJob> getSnapshotJobList() {
            return this.snapshotJobList;
        }
    }



}
