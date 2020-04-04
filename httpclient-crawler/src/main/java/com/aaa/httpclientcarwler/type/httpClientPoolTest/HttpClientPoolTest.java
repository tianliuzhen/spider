package com.aaa.httpclientcarwler.type.httpClientPoolTest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class HttpClientPoolTest {
    public static void main(String[] args) {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // 设置最大连接数
        cm.setMaxTotal(100);
        // 设置主机最大连接数，
        /**
         * 这里的意思是 假如  我们的爬虫机器 总共 100个 连接
         * 我们在爬数据时，可能会涉及到多个 主机 如：taobao、badu、jingdong ......
         * 避免 一个 主机 占用过多 过的连接
         */
        cm.setDefaultMaxPerRoute(10);

        //调用
        doPost(cm);
        doPost(cm);
    }

    private static void doPost(PoolingHttpClientConnectionManager cm) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        try {
            HttpPost httpPost = new HttpPost("http://www.itcast.cn/");
            System.out.println("发起请求的信息"+httpPost);
            CloseableHttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                String conent = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(conent);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //不能关闭  httpClient.close(); 由连接池管理

        }


    }
}
