package com.aaa.httpclientcarwler.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/4
 */
public class DomSelectorMixTest {

    /**
     *             Selector 选择组合器
     *  1. el#id 元素+ID，（选择器和元素 在同标签）比如： H3#id_a
     *  2. el.class 元素＋class 比如: li.class_a
     *  3. el[attr] 元素+属性名，比如: span[abc]
     *  4. 任意组合：比如：span[abc].class_a
     *  5. ancestor child :查找某个元素下子元素，比如：.class_a li 查找 class_a 下的所有li
     *  6. parent > children :查找某个父元素下的直接子元素，
     *      比如：#div2 > ul >li
     *           查找 #div2 下的第一级 ul ，接着查找 ul 下的第一级 li
     *  7. parent > * :查找某个父元素下所有直接子元素
     *
     */

    @Test
    public void selector() throws Exception {
        // 解析文件
        Document doc = Jsoup.parse(new File(DomTest.class.getClassLoader().getResource("static/index.html").getPath()), "utf-8");
        // 1. el#id 元素+ID，比如： H3#id_a  ，只能是 同标签，否者无效
        Elements select = doc.select("span#id_first_span ");
        System.out.println("select = " + select.text());
        // 2. el.class 元素＋class 比如: li.class_a
        Elements select1 = doc.select("span.class_first_span");
        System.out.println("select1 = " + select1.text());
        // 3. el[attr] 元素+属性名，比如: span[abc]
        Elements select2 = doc.select("li[abc]");
        System.out.println("select2 = " + select2.text());
        Elements select3 = doc.select("li[abc=123]");
        System.out.println("select3 = " + select3.text());
        // 4. 任意组合：比如：span[abc].class_a
        Elements select4 = doc.select("li[abc=456].class_abc_456");
        System.out.println("select4 = " + select4.text());
        //5.ancestor child :查找某个元素下子元素，比如：.class_a li 查找 class_a 下的所有li
        Elements select5 = doc.select("#id_first_ul li");
        System.out.println("select5 = " + select5.text());
        //6. parent > children :查找某个父元素下的直接子元素，
        //注：这里只获取 第一层次
        Elements select6 = doc.select("#id_first_ul > li");
        System.out.println("select6 = " + select6.text());
        //7. parent > * :查找某个父元素下所有直接子元素
        // 注：这里和步骤5 类似
        Elements select7 = doc.select("#id_first_ul > *");
        System.out.println("select6 = " + select7.text());
    }
    }
