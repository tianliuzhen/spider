package com.aaa.httpclientcarwler.type;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class HttpPostParmeTest {
    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 完整的请求地址是 http://resource.boxuegu.com/booklist/find.html?search=java

        // 和 get 请求仅是此外不同
        HttpPost httpPost = new HttpPost("http://resource.boxuegu.com/booklist/find.html");
        System.out.println("发起请求的信息"+httpPost);

        // 声明List集合，封装表单的参数。
        List<NameValuePair> params = new ArrayList();
        params.add(new BasicNameValuePair("search", "java"));
        // 创建表单的Entity 对象,第一个参数封装好的表单参数
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,"utf-8");

        // 设置表单的Entity 对象 到Post请求中
        httpPost.setEntity(formEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 200) {
            String conent = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(conent);

        }

        httpClient.close();
    }
}
