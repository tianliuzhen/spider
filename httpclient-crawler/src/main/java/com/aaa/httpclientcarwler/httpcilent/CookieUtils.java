package com.aaa.httpclientcarwler.httpcilent;

/**
 * @author liuzhen.tian
 * @version 1.0 CookieUtils.java  2020/8/28 20:53
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


public class CookieUtils {

    public static void main(String[] args) {
        Map<String, String> headParamsMap = new HashMap<String, String>();
        headParamsMap.put("Host", "passport.jd.com/uc/loginService");
        headParamsMap.put("Referer",
                "https://passport.jd.com/new/login.aspx");
        Map<String, String> formMap = new HashMap<String, String>();
        formMap.put("TPL_username", "登录账号");
        formMap.put("TPL_password_2", "账号密码");
        formMap.put("TPL_redirect_url", "http://sycm.taobao.com/portal/home.htm");
        String cookieStr = getCookieByDoPost("https://passport.jd.com/uc/loginService", headParamsMap, formMap,
                "utf-8");
        System.out.println(cookieStr);
    }

    public static String getCookieByDoPost(String url, Map<String, String> headParamsMap, Map<String, String> formMap,
                                           String charset) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        StringBuffer cookie = new StringBuffer();

        try {
            CookieStore cookieStore = new BasicCookieStore();
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            httpPost = new HttpPost(url);
            // 设置请求体参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator<Entry<String, String>> iterator = formMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }

            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }

            // 设置请求头通用信息
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");

            Set<Entry<String, String>> entrySet = headParamsMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }

            HttpResponse response = httpClient.execute(httpPost);

            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    // 获得Cookies
                    List<Cookie> cookies = cookieStore.getCookies();
                    for (Cookie c : cookies) {
                        cookie.append(c.getName()).append("=").append(c.getValue()).append(";");
                        if (c.getName().equals("_tb_token_")) {
                             c.getValue();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpPost.abort();
        }
        return cookie.toString();
    }
}
