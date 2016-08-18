package cn.superid.webapp.utils;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AliSmsDao {
    private static final Logger LOG = LoggerFactory.getLogger(AliSmsDao.class);
    public static final String registerCode= "SMS_4455521"; //验证码${code}，您正在注册成为${product}用户，感谢您的支持！
    public static final String checkIdentityCode = "SMS_4455524"; // 身份验证验证码模板,验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！
    public static final String loginCode = "SMS_4455523";//验证码${code}，您正在登录${product}，若非本人操作，请勿泄露



    private static String getSupportTemplateSign(String templateCode) {
        switch (templateCode) {
            case registerCode: return "注册验证";
            case  checkIdentityCode: return "身份验证";
            case loginCode: return "登录验证";

        }
        return "系统通知";
    }

    private static String appKey= "23297961";
    private static String appSecret = "dd1ea0e84b67f70d7cc01f8320cca079" ;
    private static String apiUrl ="http://gw.api.taobao.com/router/rest";

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }


    public static boolean sendSMSMessageToMobileWithTemplate(String mobile, String templateId, Map<String, Object> params) {
        TaobaoClient client = new DefaultTaobaoClient(apiUrl, appKey, appSecret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend(mobile);
        req.setSmsType("normal");
        req.setSmsFreeSignName(getSupportTemplateSign(templateId));
        if(params == null) {
            params = new HashMap<>();
        }
        params.put("product", "SuperId");
        JSONObject paramsJson = new JSONObject();
        paramsJson.putAll(params);
        req.setSmsParamString(paramsJson.toJSONString());
        req.setRecNum(mobile);
        req.setSmsTemplateCode(templateId);
        AlibabaAliqinFcSmsNumSendResponse rsp;
        try {
            rsp = client.execute(req);
//            System.out.println(rsp.getBody());
            return rsp.getResult().getSuccess();
        } catch (ApiException e) {
            LOG.error(e.getMessage());
            return false;
        }
    }
}
