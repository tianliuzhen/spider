package com.aaa.httpclientcarwler.testfirst;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * description: 第一个爬虫
 * 爬取 传智
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class CrawlerFirst {
    public static void main(String[] args) throws Exception {
      //1. 打开浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();

      //2. 输入网址
        HttpGet httpGet = new HttpGet("http://www.itcast.cn/");

     //3. 发起请求，返回响应，使用HttpClient对象发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

     //4. 解析响应，获取数据
        // 判断状态码是不是 200
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity, "utf-8");
            System.out.println(content);

        }
    }
}
