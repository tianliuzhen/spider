package com.aaa.httpclientcarwler.type.httpClientPoolTest;

import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
public class HttpConfigTest {
    public static void main(String[] args) throws Exception {
        //创建httpclient 对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建HttpGet对象，设置Url访问地址
        HttpGet httpGet = new HttpGet("http://www.itcast.cn/");

        //配置请求信息
        RequestConfig.Builder builder = RequestConfig.custom()
                //设置连接最长时间，默认是 毫秒
                .setConnectTimeout(1000)
                //设置获取连接的最长时间，默认是 毫秒
                .setConnectionRequestTimeout(500)
                //设置数据传输的最长时间，默认是 毫秒
                .setSocketTimeout(10*1000)
                ;


        // 使用httpclient 发起请求，获取response
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            // 解析请求
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
