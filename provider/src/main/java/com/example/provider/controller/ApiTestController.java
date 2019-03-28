package com.example.provider.controller;

import com.example.provider.utils.*;
import com.qcloud.Common.Sign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author shenzm
 * @date 2019-3-18
 * @description 作用
 */

@RestController
public class ApiTestController {

    private static final Logger logger = LoggerFactory.getLogger(ApiTestController.class);

    /**通过注册中心获取*/
    private String SecretId = "12345678";

    /**通过注册中心获取*/
    private String SecretKey ="abcdefg";

    private static JsonBinder jsonBinder = JsonBinder.buildNormalBinder();

    @RequestMapping("/api/test")
    public Map<String, Object> test() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            TreeMap<String, Object> paramMap = getCommParams("sayHello");
            paramMap.put("name", "java");
            String signUrl = Sign.makeSignPlainText(paramMap, "GET", "127.0.0.1:9091", "/commonApi/sayHello");
            String signature = Sign.sign(signUrl, SecretKey, "HmacSHA256");
            paramMap.put("Signature", signature);
            HttpResult httpResult = HttpClientUtil.doGet("http://127.0.0.1:9091/commonApi/sayHello", paramMap);
            String responseBody = httpResult.getBody();
            Map<String, Object> jsonMap = jsonBinder.fromJson(responseBody, HashMap.class);
            resultMap.put("jsonMap", jsonMap);
        } catch (Exception e) {
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;


    }
    @RequestMapping("/api/test2")
    public Map<String, Object> test2() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            TreeMap<String, Object> paramMap = getCommParams("sayGoodbye");
            paramMap.put("name", "java");
            paramMap.put("age", "20");
            String signUrl = Sign.makeSignPlainText(paramMap, "GET", "127.0.0.1:9091", "/commonApi/sayGoodbye");
            String signature = Sign.sign(signUrl, SecretKey, "HmacSHA256");
            paramMap.put("Signature", signature);
            HttpResult httpResult = HttpClientUtil.doGet("http://127.0.0.1:9091/commonApi/sayGoodbye", paramMap);
            String responseBody = httpResult.getBody();
            Map<String, Object> jsonMap = jsonBinder.fromJson(responseBody, HashMap.class);
            resultMap.put("jsonMap", jsonMap);
        } catch (Exception e) {
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;


    }

    /**
     * 签名的公共的请求参数 （必须携带）
     * @param Action
     * @return
     */
    private TreeMap<String, Object> getCommParams(String Action) {
        TreeMap<String, Object> paramMap = new TreeMap<String, Object>();
        paramMap = new TreeMap<String, Object>();
        paramMap.put("Action", Action);
        paramMap.put("Nonce", NumberUtil.getRandomNum());
        paramMap.put("Region", "sh");
        paramMap.put("SecretId", SecretId);
        paramMap.put("SignatureMethod", "HmacSHA256");
        paramMap.put("Timestamp", DateUtil.getTimeStamp());
        return paramMap;
    }
}
