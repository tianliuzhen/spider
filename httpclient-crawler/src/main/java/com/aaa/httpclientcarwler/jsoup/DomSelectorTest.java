package com.aaa.httpclientcarwler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class DomSelectorTest {
    /**
     * 1. tagName： 通过标签查找元素，      比如：span
     * 2. #id：通过ID 查找元素，           比如：#id_xm
     * 3. .class: 通过 class 名称查找元素  比如： .class_aaa
     * 4. [attribute]:利用属性查找元素。   比如：[abc]
     * 5. [att=value]:利用属性值查找元素。 比如：[abc=123]
     */
    @Test
    public void selector() throws Exception {
        // 解析文件
        Document doc = Jsoup.parse(new File(DomTest.class.getClassLoader().getResource("static/index.html").getPath()), "utf-8");

        // 1. tagName： 通过标签查找元素，      比如：Span
        Elements elements = doc.select("span");
        for (Element element : elements) {
            System.out.println("element = " + element.text());
        }
        // 2. #id：通过ID 查找元素，           比如：#sh_id
        Elements selectById= doc.select("#id_xm");
        System.out.println("select = " + selectById.text());
        // 3. .class: 通过 class 名称查找元素  比如： .class_aaa
        Elements selectByClass= doc.select(".class_aaa");
        System.out.println("selectByClass = " + selectByClass.text());
        // 4. [attribute]:利用属性查找元素。   比如：[abc]
        Elements selectByAttr = doc.select("[abc]");
        System.out.println("selectByAttr = " + selectByAttr.text());
        // 5. [att=value]:利用属性值查找元素。 比如：[abc=123]
        Elements selectByAttrAndValue = doc.select("[abc=456]");
        System.out.println("selectByAttrAndValue = " + selectByAttrAndValue.text());


    }

}
