package com.guangxuan.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.guangxuan.enumration.BusinessFailEnum;
import com.guangxuan.exception.BusinessException;


public class SmsUtils {

    public static boolean sendSms(String phone, String randomCode) {
        // 创建DefaultAcsClient实例并初始化
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",          // 地域ID
                "LTAISVigq4EdqDpL",      // RAM账号的AccessKey ID
                "6K2Vkst5UnyTdV292NbanSJ0QUspeI"); // RAM账号AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);
        // 创建API请求并设置参数
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "电线竿");
        request.putQueryParameter("TemplateCode", "SMS_128565186");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + randomCode + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
        } catch (ServerException e) {
            e.printStackTrace();
            throw new BusinessException(BusinessFailEnum.GET_RANDOM_FAIL);
        } catch (ClientException e) {
            e.printStackTrace();
            throw new BusinessException(BusinessFailEnum.GET_RANDOM_FAIL);
        }
        return true;
    }
}
