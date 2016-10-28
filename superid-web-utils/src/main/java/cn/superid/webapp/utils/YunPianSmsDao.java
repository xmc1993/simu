package cn.superid.webapp.utils;

import com.yunpian.sdk.model.*;
import com.yunpian.sdk.service.*;

/**
 * Created by njuTms on 16/10/26.
 */

public class YunPianSmsDao {
    public static YunpianRestClient client;
    private static String apiKey = "4defc0878d19d7bd80d6a44c02aff836";


    public static boolean sendSMSMessageToForeignMobile(String mobile, String code,String codeType) {
        client = new YunpianRestClient(apiKey);
        SmsOperator smsOperator = client.getSmsOperator();
        //URLEncoder.encode("+886980377771",)
        String msg ;
        switch (codeType){
            case SmsType.registerCode:
                msg = "【SuperId】验证码"+code+"，您正在注册成为SuperId用户，感谢您的支持！";
                break;
            case SmsType.checkIdentityCode:
                msg = "【SuperId】验证码"+code+"，您正在进行SuperId身份验证，打死不要告诉别人哦！";
                break;
            case SmsType.loginCode:
                msg = "【SuperId】验证码"+code+"，您正在登录${product}，若非本人操作，请勿泄露";
                break;
            default:
                return false;
        }
        ResultDO<SendSingleSmsInfo> resultDO= smsOperator.singleSend(mobile,msg);
        System.out.println(resultDO.getData());
        if(resultDO.getData().getCode() == 0){
            return true;
        }
        return false;
    }

    public static void main(String args[]){
        //sendSMSMessageToForeignMobile("+886980377771","1233");
    }

    /*
    public static void testTpl() {
        client = new YunpianRestClient(apiKey);
        TplOperator tplOperator = client.getTplOperator();
        ResultDO<List<TemplateInfo>> resultDO = tplOperator.getDefault();
        System.out.println(resultDO);
        System.out.println();
        resultDO = tplOperator.get();
        System.out.println(resultDO);
        System.out.println();
        resultDO = tplOperator.getDefault("2");
        System.out.println(resultDO);
        System.out.println();


        ResultDO<TemplateInfo> result = tplOperator.add("【SuperId】验证码#code#，您正在注册成SuperId用户，感谢您的支持！");
        System.out.println(result);
        resultDO = tplOperator.get(String.valueOf(result.getData().getTpl_id()));
        System.out.println(resultDO);
        result = tplOperator.update(String.valueOf(result.getData().getTpl_id()), "【aaa】大倪#asdf#");
        System.out.println(result);
        result = tplOperator.del(String.valueOf(result.getData().getTpl_id()));
        System.out.println(result);
    }
    */
}
