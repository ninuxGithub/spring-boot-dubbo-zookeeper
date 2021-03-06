package com.example.consumer.config;

import com.alibaba.fastjson.JSONObject;
import com.example.consumer.bean.Message;
import com.qcloud.Common.Sign;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 对请求路径符合： /commonApi/* 的所有的路径进行过滤
 * <p>
 * 在过滤器的doFilter 方法进行签名的过滤
 *
 * @author shenzm
 * @date 2019-3-18
 * @description 作用
 */

@Component
@WebFilter(urlPatterns = "/commonApi/*", filterName = "apiAccessFilter")
public class ApiAccessFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ApiAccessFilter.class);

    private static final String SIGNATURE = "Signature";

    private static final String SIGNATUREMETHOD = "SignatureMethod";

    @Autowired
    private ScanBeanService scanBeanService;


    /**
     * 公共的参数列表  是有序的
     */
    private List<String> paramList = new ArrayList<>(Arrays.asList("Action", "Nonce", "Region", "SecretId", "SignatureMethod", "Timestamp"));

    private Set<String> commonParams = new HashSet<>(paramList);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 通过请求的参数secretId 查询对应的用户的SecretKey
     *
     * @param secretId
     * @return
     */
    public String mockQuerydb(String secretId) {
        if ("12345678".equals(secretId)) {
            return "abcdefg";
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        //如果请求的不是是公共api接口， 直接通过请求， 如果是commonApi的请求，需要验证签名
        //swagger2 页面可见，但是无法请求
        if (!requestURI.contains("/commonApi/")){
            chain.doFilter(request, response);
            return;
        }
        logger.info("ApiAccessFilter 拦截url :{}",requestURI);

        String errJson = null;
        boolean valid = true;

        TreeMap<String, Object> paramMap = new TreeMap<>();

        String action = request.getParameter("Action");
        String secretId = request.getParameter("SecretId");
        if (StringUtils.isNoneBlank(action) && StringUtils.isNoneBlank(secretId)) {
            Map<String, List<String>> beanName2ParamsTable = scanBeanService.getMethodName2ParamsTable();
            if (beanName2ParamsTable.containsKey(action)) {
                List<String> paramsTable = beanName2ParamsTable.get(action);
                Map<String, String> name2value = new HashMap<>();

                //将方法的参数列表加入到共工的参数里面的集合里面去
                paramList.addAll(paramsTable);

                //开始获取请求的参数， 判断非空
                for (String paramName : paramList) {
                    String paramValue = request.getParameter(paramName);
                    if (paramName.equals(SIGNATURE)) {
                        continue;
                    }
                    if (null == paramValue || "".equals(paramValue.trim())) {
                        errJson = "请求参数：" + paramName + "不可以为null或空字符串";
                        valid = false;
                        break;
                    }
                    paramMap.put(paramName, paramValue);
                }

                if (valid) {

                    String remoteHost = request.getRemoteHost();
                    int serverPort = request.getServerPort();
                    String requestHost = remoteHost + ":" + serverPort;
                    //String requestURI = req.getRequestURI();
                    String requestMethod = req.getMethod();
                    String signatureMethod = req.getParameter(SIGNATUREMETHOD);
                    String reqestSignature = req.getParameter(SIGNATURE);

                    logger.info("requestHost : {}", requestHost);
                    logger.info("requestURI :{}", requestURI);
                    logger.info("requestMethod:{}", requestMethod);
                    logger.info("signatureMethod:{}", signatureMethod);
                    logger.info("reqestSignature:{}", reqestSignature);

                    try {
                        String signUrl = Sign.makeSignPlainText(paramMap, requestMethod, requestHost, requestURI);
                        String signature = Sign.sign(signUrl, mockQuerydb(secretId), signatureMethod);

                        logger.info("server calculate signature :{}", signature);

                        //对比签名是否一致
                        if (reqestSignature.equals(signature)) {
                            valid = true;
                            logger.info("签名验证通过，调用=====>chain.doFilter--->{}",requestMethod);
                        } else {
                            valid = false;
                            errJson = "签名不正确";
                        }
                    } catch (Exception e) {
                        valid = false;
                        errJson = e.getMessage();
                    }

                }

            } else {
                valid = false;
                errJson = "不存在的Action名称";
            }
        } else {
            valid = false;
            errJson = "请求未携带签名";
        }

        if (!valid) {
            writeErrJson(response, new Message(errJson));
        } else {
            chain.doFilter(request, response);
        }
    }

    private void writeErrJson(ServletResponse response, Message message) throws IOException {
        response.setCharacterEncoding("utf-8");
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("content-type", "text/html;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(JSONObject.toJSONString(message).getBytes());
    }

    @Override
    public void destroy() {

    }
}
