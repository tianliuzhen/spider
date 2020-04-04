package com.aaa.httpclientcarwler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.util.Set;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class DomReversTest {
    /**
     * 元素获取
     *      1. 从元素中获取 id
     *      2. 从元素中获取 className
     *      3. 从元素中获取属性值 attr
     *      4. 从元素中获取所有属性 attributes
     *      5. 从元素中获取文本内容
     */
    @Test
    public void testDomReverse() throws Exception {
        // 解析文件
        Document doc = Jsoup.parse(new File(DomTest.class.getClassLoader().getResource("static/index.html").getPath()), "utf-8");
        // 1. 从元素中获取 id
            Element element = doc.getElementById("id_sh");
            String id = element.id();
            System.out.println("id = " + id);
        // 2. 从元素中获取 className
            String s = element.className();
            System.out.println("s = " + s);
            //  eg: <li class="class_aaa class_aaa1" id="id_sh">
            // 会根据  class_aaa class_aaa1 中间的 空格进行拆分
            Set<String> strings = element.classNames();
            for (String string : strings) {
                System.out.println("string = " + string);
            }
        // 3. 从元素中获取属性值 attr
            String name = element.attr("name");
            System.out.println("name = " + name);

        // 4. 从元素中获取所有属性 attributes
        Attributes attributes = element.attributes();
        System.out.println("attributes.toString() = " + attributes.toString());

        // 5. 从元素中获取文本内容
        System.out.println("element.text() = "+element.text());
    }
}

