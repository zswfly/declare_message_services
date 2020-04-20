package com.zsw.services;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zsw.utils.CacheStaticURLUtil;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.UserStaticURLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangshaowei on 2020/4/19.
 */
@Service
public class AliyunSMSSErviceImpl implements IAliyunSMSService{

    private static final long serialVersionUID = -319832845159453580L;

//    @Autowired
//    DefaultProfile defaultProfile;

    @Autowired
    IClientProfile iClientProfile;

    @Autowired
    RestTemplate restTemplate;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void sendVerifySMS(String phone,String type) throws Exception{
        IAcsClient acsClient = new DefaultAcsClient(iClientProfile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();

        //使用post提交
        //request.setMethod(MethodType.POST);

        request.setSysMethod(MethodType.POST);
//        request.setSysDomain("dysmsapi.aliyuncs.com");
//        request.setSysVersion("2017-05-25");
//        request.setSysAction("SendSms");



        //1，必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(phone);
        //2，必填:短信签名-可在短信控制台中找到
        request.setSignName("");
        //3，必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode("");


        String code = CommonUtils.getVerifyCode();

        Map<String, String > param = new HashMap<>();
        param.put("phone",phone);
        param.put("verifyCode",code);
        param.put("type",type);
        this.restTemplate.postForEntity(
                "http://cache-services"
                        + CacheStaticURLUtil.redisController
                        + CacheStaticURLUtil.redisController_setVerifyCode
                ,param,null);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("");

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        // 判断是否发送成功
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println("返回的状态码：" + sendSmsResponse.getCode());
            System.out.println("返回的信息：" + sendSmsResponse.getMessage());
        }

    }
}








        /*
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        //final String accessKeyId = "yourAccessKeyId";//你的accessKeyId,参考本文档步骤2
        // final String accessKeySecret = "yourAccessKeySecret";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
        accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();


        //使用post提交
        request.setMethod(MethodType.POST);
        //1，必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers("");
        //2，必填:短信签名-可在短信控制台中找到
        request.setSignName("");
        //3，必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode("");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        // 判断是否发送成功
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
        //请求成功
        System.out.println("返回的状态码：" + sendSmsResponse.getCode());
        System.out.println("返回的信息：" + sendSmsResponse.getMessage());
        }
        */
