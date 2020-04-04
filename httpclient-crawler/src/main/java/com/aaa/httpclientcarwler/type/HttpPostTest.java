package com.aaa.httpclientcarwler.type;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class HttpPostTest {
    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 和 get 请求仅是此外不同
        HttpPost httpPost = new HttpPost("http://www.itcast.cn/");
        System.out.println("发起请求的信息"+httpPost);
        CloseableHttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 200) {
            String conent = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(conent);

        }

        httpClient.close();
    }
}
