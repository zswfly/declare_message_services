package com.zsw.services;

import com.zsw.utils.CacheStaticURLUtil;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.HuaweiyunMessageUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

//如果JDK版本是1.8,可使用原生Base64类

/**
 * Created by zhangshaowei on 2020/4/19.
 */
@Service
public class HwyMessageServiceImpl implements HwyMessageService{

    private static final long serialVersionUID = -319832845159453580L;

    @Value("${huawei.message.WSSE_HEADER_FORMAT}")
    private String WSSE_HEADER_FORMAT;

    @Value("${huawei.message.AUTH_HEADER_VALUE}")
    private String AUTH_HEADER_VALUE;

    @Value("${huawei.message.url}")
    private String url;

    @Value("${huawei.message.appKey}")
    private String appKey;

    @Value("${huawei.message.appSecret}")
    private String appSecret;


    @Value("${huawei.message.sender_test}")
    private String sender_test;

    @Value("${huawei.message.signature_test}")
    private String signature_test;

    @Value("${huawei.message.templateId_test}")
    private String templateId_test;


    @Value("${huawei.message.signature_linghui}")
    private String signature_linghui;

    @Value("${huawei.message.sender_linghui}")
    private String sender_linghui;

    @Value("${huawei.message.templateId_login}")
    private String templateId_login;

    @Value("${huawei.message.templateId_resetPassword}")
    private String templateId_resetPassword;


    //TODO
    //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
    private String statusCallBack = "";



    @Autowired
    RestTemplate restTemplate;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { Exception.class })
    public void sendVerifySMS(String phone,String type) throws Exception{

        String code = CommonUtils.getVerifyCode_6number();
        String templateParas = "[\""+ code +"\"]";
        Map<String, String > param = new HashMap<>();
        param.put("phone",phone);
        param.put("verifyCode",code);
        param.put("type",type);
        //TODO 查询验证码是否有不用再新建发送
        //TODO 查询用户是否超过使用当日数量
        //TODO 设置超时
        this.restTemplate.postForEntity(
                CommonStaticWord.HTTP + CommonStaticWord.cacheServices
                        + CacheStaticURLUtil.redisController
                        + CacheStaticURLUtil.redisController_setVerifyCode
                ,param,null);


        String templateId = null;
        if(CommonStaticWord.CacheServices_Redis_VerifyCode_Type_LOGIN.equals(type)){
            templateId = templateId_login;
        }else if(CommonStaticWord.CacheServices_Redis_VerifyCode_Type_REST_PASSWORD.equals(type)){
            templateId = templateId_resetPassword;
        }

        //请求Body,不携带签名名称时,signature请填null
        String body = HuaweiyunMessageUtils.buildRequestBody(
                sender_linghui,
                "+86" + phone,
                templateId,
                templateParas,
                statusCallBack,
                signature_linghui
        );
        if (null == body || body.isEmpty()) {
            System.out.println("body is null.");
            return;
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = HuaweiyunMessageUtils.buildWsseHeader(appKey, appSecret,WSSE_HEADER_FORMAT);
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            System.out.println("wsse header is null.");
            return;
        }


        //如果JDK版本是1.8,可使用如下代码
        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                        (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        HttpResponse response = client.execute(RequestBuilder.create("POST")//请求方法POST
                .setUri(url)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                .addHeader("X-WSSE", wsseHeader)
                .setEntity(new StringEntity(body)).build());

        System.out.println(response.toString()); //打印响应头域信息
        System.out.println(EntityUtils.toString(response.getEntity())); //打印响应消息实体


    }
}


