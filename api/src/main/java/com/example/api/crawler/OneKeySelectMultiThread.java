package com.example.api.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;


/**
 * @author shenzm
 * @date 2019-3-20
 * @description 作用
 */
public class OneKeySelectMultiThread {


    private static final Logger logger = LoggerFactory.getLogger(OneKeySelectMultiThread.class);

    public static void main(String[] args) {
        logger.info("开始爬数据");
        Map<Integer, List<Integer>> indexMap = new HashMap<>();
        indexMap.put(1, Arrays.asList(1, 2, 3, 4, 5));
        indexMap.put(2, Arrays.asList(6, 7, 8, 9, 10, 11));
        indexMap.put(3, Arrays.asList(12, 13, 14, 15, 16));
        Map<String, Map<String, List<String>>> dataMap = new HashMap<>();

        int len = 16;
        CountDownLatch latch = new CountDownLatch(len);
        for (int i = 1; i <= len; i++) {
            final int k = i;
            new Thread(()->{
                extractRowMap(k, dataMap, indexMap,latch);
            }).start();

        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String key : dataMap.keySet()) {
            for (String liKey : dataMap.get(key).keySet()) {
                logger.info(key + "-->" + liKey + "-->" + dataMap.get(key).get(liKey));
            }
        }

    }

    private static void extractRowMap(int k, Map<String, Map<String, List<String>>> dataMap, Map<Integer, List<Integer>> indexMap,CountDownLatch latch) {
        WebClient webClient = null;
        try {
            webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setTimeout(8000);
            webClient.setJavaScriptTimeout(8000);
            HtmlPage page = (HtmlPage) webClient.getPage("https://e9qto0gtp.lightyy.com/index.html");
            webClient.waitForBackgroundJavaScript(10000);
            List<HtmlElement> menuList = page.getByXPath("//section[@class='nav_box']/span");

            int tabIndex = 0;
            int rowIndex = 0;
            outer:
            for (Integer ti : indexMap.keySet()) {
                List<Integer> list = indexMap.get(ti);
                inner:
                for (int index = 0; index < list.size(); index++) {
                    if (list.get(index) == k) {
                        tabIndex = ti;
                        rowIndex = index;
                        break outer;
                    }
                }
            }
            HtmlElement menuTabHtml = menuList.get(tabIndex);

            //获取首页的tab的类型热门，短线，淘金三个类型
            String tabName = menuTabHtml.asText();
            Map<String, List<String>> menuMap = dataMap.get(tabName);
            if (menuMap == null) {
                menuMap = new HashMap<>();
            }

            HtmlPage clickTabPage = menuTabHtml.click();
            List<HtmlElement> liList = clickTabPage.getByXPath("//ul[@class='model_box']/li");
            HtmlElement rowHtmlElement = liList.get(rowIndex);
            List<HtmlElement> liRows = rowHtmlElement.getByXPath("//h3");
            // 每个tab类型下面拥有的li
            String rowText = liRows.get(rowIndex).asText();
            final HtmlElement htmlElement = liList.get(rowIndex);
            List<String> list = menuMap.get(rowText);
            if (list == null) {
                list = new ArrayList<>();
            }
            HtmlPage liPage = htmlElement.click();
            List<HtmlElement> domList = liPage.getByXPath("//li[@class='share_list']/div/span[@class='num']");
            for (HtmlElement dom : domList) {
                list.add(dom.asText());
            }
            menuMap.put(rowText, list);
            dataMap.put(tabName, menuMap);
            latch.countDown();
        } catch (Exception e) {
            logger.error(e.getMessage());
            latch.countDown();
        } finally {
            if (webClient != null) {
                webClient.close();
            }
        }
    }

}
