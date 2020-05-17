package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.services.HwyMessageService;
import com.zsw.utils.CommonStaticWord;
import com.zsw.utils.CommonUtils;
import com.zsw.utils.MessageStaticURLUtil;
import com.zsw.utils.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshaowei on 2020/5/17.
 */
@RestController
@RequestMapping(MessageStaticURLUtil.hwyMessageController)
public class HwyMessageController {

    @Autowired
    HwyMessageService hwyMessageService;

    @RequestMapping(value= MessageStaticURLUtil.aliyunSMSController_sendVerifyCode,
            method= RequestMethod.GET)
    public String sendVerifyCode(String phone,String type) throws Exception {
        try {

            ResponseJson responseJson = new ResponseJson();
            Gson gson = new Gson();

            if (StringUtils.isBlank(phone)
                    ||!CommonUtils.isMobileNO(phone)
                    ) {
                responseJson.setCode(ResponseCode.Code_500);
                responseJson.setMessage("手机号码有误");
                return gson.toJson(responseJson);
            }

            if("login".equals(type)){
                type = CommonStaticWord.CacheServices_Redis_VerifyCode_Type_LOGIN;
            }else if("restPassword".equals(type)){
                type = CommonStaticWord.CacheServices_Redis_VerifyCode_Type_REST_PASSWORD;
            }else{
                responseJson.setCode(ResponseCode.Code_500);
                responseJson.setMessage("短信验证码类型有误");
                return gson.toJson(responseJson);
            }

            this.hwyMessageService.sendVerifySMS(phone,type);
            responseJson.setCode(ResponseCode.Code_200);
            return gson.toJson(responseJson);
        }catch (Exception e){
            e.printStackTrace();
            return CommonUtils.ErrorResposeJson();
        }
    }
}
