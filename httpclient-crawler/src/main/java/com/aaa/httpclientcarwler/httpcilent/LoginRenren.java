package com.aaa.httpclientcarwler.httpcilent;

/**
 * @author liuzhen.tian
 * @version 1.0 LoginRenren.java  2020/8/28 21:06
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class LoginRenren {

    private static String USER_NAME = "用户名";
    private static String PASS_WORD = "你的密码";
    private static String RED_URL = "http://blog.renren.com/blog/304317577/449470467";

    private static String LOGIN_URL = "http://www.renren.com/PLogin.do";

    // 用于取得重定向地址
    private HttpResponse response;
    // 在一个会话中用到的 httpClient 对象
    private DefaultHttpClient httpClient = new DefaultHttpClient();

    public static void main(String[] args) {
        LoginRenren renren = new LoginRenren();
        renren.printText();
    }

    /**
     * 登录到页面
     * @return
     */
    private boolean login(){
        boolean result = false;
        HttpPost httpPost = new HttpPost(LOGIN_URL);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("origURL", RED_URL));
        parameters.add(new BasicNameValuePair("domain", "renren.com"));
        parameters.add(new BasicNameValuePair("isplogin", "true"));
        parameters.add(new BasicNameValuePair("formName", ""));
        parameters.add(new BasicNameValuePair("method", ""));
        parameters.add(new BasicNameValuePair("submit", "登录"));
        parameters.add(new BasicNameValuePair("email", USER_NAME));
        parameters.add(new BasicNameValuePair("password", PASS_WORD));

        try {
            HttpEntity entity = new UrlEncodedFormEntity(parameters,HTTP.UTF_8);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            return result;
        } finally {
            httpPost.abort();
        }
        result = true;
        return result;
    }

    /**
     * 取得重定向地址
     * @return
     */
    private String getRedirectLocation(){
        String result = "";
        Header locationHeader = response.getFirstHeader("Location");
        if(locationHeader==null){
            return null;
        }
        result = locationHeader.getValue();
        return result;
    }

    /**
     * 根据重定向地址返回内容
     * @param redirectLocation
     * @return
     */
    private String getText(String redirectLocation){
        HttpGet httpGet = new HttpGet(redirectLocation);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = "";
        try {
            responseBody = httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = null;
            return responseBody;
        } finally {
            httpGet.abort();
            httpClient.getConnectionManager().shutdown();// 关闭连接
        }
        return responseBody;
    }

    public void printText(){
        if(login()){
            String redirectLocation = getRedirectLocation();
            if(redirectLocation!=null){
                System.out.println(getText(redirectLocation));
            }else{
                System.out.println("获取redirectLocation失败！");
            }
        }else{
            System.out.println("登录失败！");
        }
    }

}
