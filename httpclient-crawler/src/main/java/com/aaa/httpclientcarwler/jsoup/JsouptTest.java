package com.aaa.httpclientcarwler.jsoup;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * description:
 *  虽然 jsoup 可以代替 Httpclient 直接发起请求解析数据，但是往往不会这么用，
 *  因为实际的开发中。需要使用多线程，线程池，代理等等方式
 *  而，jsoup 对这些东西的支持不是很好，所以我们一般把 jsoup 仅仅作为 解析工具去使用
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class JsouptTest {
    @Test
    public void testUrl() throws Exception {
        // 解析url 地址，第一个参数是访问的url，第二个参数是访问时候的超时时间
        Document doc = Jsoup.parse(new URL("http://www.itcast.cn/"), 1000);
        String lione = doc.getElementsByTag("title").first().text();
        System.out.println(lione);

    }

    @Test
    public void testPath(){
        System.out.println(JsouptTest.class.getClassLoader().getResource("static/index.html").getPath());
    }

    @Test
    public void testString() throws Exception {
        // 使用工具类读取文件，获取字符串
        String content = FileUtils.readFileToString(
                //这里获取 resources 下面的 static/index.html
                new File(JsouptTest.class.getClassLoader().getResource("static/index.html").getPath())
                , "utf-8");
        Document doc = Jsoup.parse(content);
        String title = doc.getElementsByTag("title").first().text();
        System.out.println(title);

    }
    @Test
    public void testFile() throws Exception{
        Document doc = Jsoup.parse(new File(JsouptTest.class.getClassLoader().getResource("static/index.html").getPath()), "utf-8");
        Elements title = doc.getElementsByTag("title");
        System.out.println(title);

    }
}
