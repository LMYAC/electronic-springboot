package com.qf.electronic.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;

import javax.servlet.http.HttpSession;
import java.util.Random;

public class SmsSender {

    private static final Random RANDOM = new Random();

    public static void sendSms(String phone, HttpSession session){
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取

            //API密钥管理中的secretId，secretKey
            Credential cred = new Credential("AKIDg7JCqQc3WlmmhIEuNcgVCfumUAeyYqvn", "mKivZy7mAJDwW1g5E8pGX6PVM5piAQZU");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            //端点：默认为腾讯云的端点
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumbers = { phone };//手机号码
            req.setPhoneNumberSet(phoneNumbers);
            //这里填写自己的APP ID
            req.setSmsSdkAppId(""); //SDK APP ID
            //这里填写短信的签名的名称
            req.setSignName("");// 签名的名称
            //这里填写短信的模板ID
            req.setTemplateId(""); // 短信模板的ID
            //生成一个6位的验证码
            String code = generateVerifyCode(6);
            String[] templateParams = {code}; //验证码
            req.setTemplateParamSet(templateParams); // 短信模板的参数，也就是验证码
            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
            SendStatus sendStatusSet = resp.getSendStatusSet()[0];
            if("ok".equalsIgnoreCase(sendStatusSet.getCode())){
                session.setAttribute("code", code);
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }

    private static String generateVerifyCode(int length){
        StringBuilder builder= new StringBuilder();
        for(int i=0; i<length; i++){
            builder.append(RANDOM.nextInt(10));
        }
        return builder.toString();
    }
}
