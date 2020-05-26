package com.zsw.controllers;

import com.google.gson.Gson;
import com.zsw.controller.BaseController;
import com.zsw.entitys.common.ResponseJson;
import com.zsw.entitys.user.LoginTemp;
import com.zsw.services.IAliyunSMSService;
import com.zsw.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhangshaowei on 2020/4/21.
 */
//@RestController
//@RequestMapping(MessageStaticURLUtil.aliyunSMSController)
public class AliyunSMSController extends BaseController {
//    @Autowired
//    IAliyunSMSService iAliyunSMSService;

//    private static final Logger LOG = LoggerFactory.getLogger(AliyunSMSController.class);

//
//    @Autowired
//    RestTemplate restTemplate;

//    @RequestMapping(value= MessageStaticURLUtil.aliyunSMSController_sendVerifyCode,
//            method= RequestMethod.GET)
//    public String sendVerifyCode(String phone,String type) throws Exception {
//        try {
//            ResponseJson responseJson = new ResponseJson();
//            Gson gson = new Gson();
//
//            if (StringUtils.isBlank(phone)
//                    ||!CommonUtils.isMobileNO(phone)
//                    ) {
//                responseJson.setCode(ResponseCode.Code_500);
//                responseJson.setMessage("手机号码有误");
//                return gson.toJson(responseJson);
//            }
//
//            if("login".equals(type)){
//                type = CommonStaticWord.CacheServices_Redis_VerifyCode_Type_LOGIN;
//            }else if("restPassword".equals(type)){
//                type = CommonStaticWord.CacheServices_Redis_VerifyCode_Type_REST_PASSWORD;
//            }else{
//                responseJson.setCode(ResponseCode.Code_500);
//                responseJson.setMessage("短信验证码类型有误");
//                return gson.toJson(responseJson);
//            }
//
//            //this.iAliyunSMSService.sendVerifySMS(phone,type);
//            responseJson.setCode(ResponseCode.Code_200);
//            return gson.toJson(responseJson);
//        }catch (Exception e){
//            e.printStackTrace();
//            return CommonUtils.ErrorResposeJson();
//        }
//    }

//    @RequestMapping(value= MessageStaticURLUtil.aliyunSMSController_sendVerifyCode,
//            method= RequestMethod.GET)
//    @ResponseBody
//    public String sendVerifyCode() throws Exception {
//       return null;
//    }

//    @Override
//    public Logger getLOG(){
//        return this.LOG;
//    }

}
