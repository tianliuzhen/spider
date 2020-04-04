package com.aaa.httpclientcarwler.type;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * description: get 请求
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class HttpGetParmeTest {
    public static void main(String[] args) throws Exception {
        //1. 创建httpclient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();


        // 完整的请求地址是 http://resource.boxuegu.com/booklist/find.html?search=java

        // 创建 URIBuilder
        URIBuilder uriBuilder = new URIBuilder("http://resource.boxuegu.com/booklist/find.html");
        // 设置参数
        uriBuilder.setParameter("search", "java");

        // 2. 创建HttpGet对象，设置Url访问地址
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        System.out.println("发起请求的信息"+httpGet);


        try {
            //3. 使用httpclient 发起请求，获取response
            CloseableHttpResponse   response = httpClient.execute(httpGet);
            //4. 解析请求
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }

    }
}
