package com.aaa.httpclientcarwler.config;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class Test {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap();
        map.put("search", "java");
        // http://resource.boxuegu.com/booklist/find.html
        HttpClientTool.doGet("https://github.com/",map);

    }


}
