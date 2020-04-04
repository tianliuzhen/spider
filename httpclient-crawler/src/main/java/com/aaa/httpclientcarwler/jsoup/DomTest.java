package com.aaa.httpclientcarwler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * description: 使用dom遍历文档
 *  和 js 、jq 有点像
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class DomTest {
    /**
     * 元素获取
     *      1. 根据Id  查询元素  getElementById
     *      2. 根据标签获取元素  getElementByTag
     *      3. 根据class 获取元素   getElementByClass
     *      4. 根据属性获取元素 getElementByAttribute
     */
    @Test
    public void testDom() throws Exception {
        // 解析文件
        Document doc = Jsoup.parse(new File(DomTest.class.getClassLoader().getResource("static/index.html").getPath()), "utf-8");
        // 1. id
        String div1 = doc.getElementById("div1").text();
        System.out.println("根据Id  查询元素的内容是 = " + div1);

        // 2. tag
        Element span = doc.getElementsByTag("span").first();
        System.out.println("根据标签获取元素 = " + span.text());

        // 3. 根据class 获取元素  这里的 demo：  <li class="class_bbb class_ddd" id="id_xm">
        Elements elementsByClass = doc.getElementsByClass("class_bbb class_ddd");
        String elementsByClass2 = doc.getElementsByClass("class_bbb").first().text();
        String elementsByClass3 = doc.getElementsByClass("class_ddd").first().text();
        System.out.println(elementsByClass.first().text());
        System.out.println(elementsByClass2);
        System.out.println(elementsByClass3);

        //4. 属性
        //通过属性获取
        String abc = doc.getElementsByAttribute("abc").text();
        System.out.println("属性 abc = " + abc);
        //通过属性名和属性值获取
        String abc1 = doc.getElementsByAttributeValue("abc", "456").text();
        System.out.println("通过属性名和属性值获取 = " + abc1);
    }



}
