package com.example.provider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author shenzm
 * @date 2019-3-14
 * @description 作用
 */

@RestController
public class PdfController {

    private static final Logger logger =LoggerFactory.getLogger(PdfController.class);

    @RequestMapping(value = "/downloadPdf")
    public Map<String,String> downloadPdf(@RequestParam("url") String url, HttpServletRequest request) {
        //http://static.sse.com.cn/disclosure/listedinfo/announcement/c/2019-03-01/600654_20190301_1.pdf
        Map<String,String> map = new HashMap<>();
        BufferedOutputStream bos = null;
        InputStream is = null;
        byte[] buffer = new byte[1024];
        try {
            String suffix = url.substring(url.lastIndexOf(".") + 1);
            is = new URL(url).openStream();
            String uuid = UUID.randomUUID().toString().substring(0, 16);
            String path = ResourceUtils.getURL("classpath:static/generic/web/").getPath();
            String fileName = uuid+"."+suffix;
            File file = new File(path, fileName);
            file.getParentFile().mkdirs();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int count = 0;
            while ((count = is.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            map.put("name",fileName);
            logger.info("产生的pdf文件名称为：{}{}",path,fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return  map;
    }
}
