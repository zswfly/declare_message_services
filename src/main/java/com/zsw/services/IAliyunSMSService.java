package com.zsw.services;

/**
 * Created by zhangshaowei on 2020/4/19.
 */
public interface IAliyunSMSService extends IBaseService {
    void sendVerifySMS(String phone,String type) throws Exception;
}
