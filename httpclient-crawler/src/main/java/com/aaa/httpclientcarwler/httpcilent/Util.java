package com.aaa.httpclientcarwler.httpcilent;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 Util.java  2020/8/28 20:04
 */
public class Util {

    public static void main(String[] args) throws Exception {
        String result;
        // 获取文件 拼接
        String getTestUrl = "https://passport.jd.com/uc/login";

        try {

            BasicCookieStore cookieStore = new BasicCookieStore();

            // 获取 响应
            HttpGet get = new HttpGet(getTestUrl);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
//            CloseableHttpClient build = HttpClientBuilder.create().build();
//            CloseableHttpResponse execute = build.execute(get);
            CloseableHttpResponse execute = httpClient.execute(get);
            result = EntityUtils.toString(execute.getEntity(), "utf-8");
            System.out.println(result);

            // 获取cookies信息
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                String value = cookie.getValue();
                System.out.println("cookies: key= "+ name + "  value= " + value);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
