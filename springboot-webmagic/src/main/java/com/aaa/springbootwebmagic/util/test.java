package com.aaa.springbootwebmagic.util;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/21
 */
public class test {
    public static void main(String[] args) {
      String str=  "<p>相关文章推荐：<br><!--start ads:1479--><!--ads:1479 end--><a href=\"/sx/zonghe/art303342.aspx\" target=\"_blank\">2020年不宜怀孕的生肖 不宜生子添丁的属相</a><br><a href=\"/sx/zonghe/art303341.aspx\" target=\"_blank\">2021年不宜怀孕的生肖 不适合产子添丁的属相</a><br><a href=\"/sx/zonghe/art303340.aspx\" target=\"_blank\">2021有添丁之喜的生肖 牛宝宝出世旺父母</a><br><a href=\"/sx/zonghe/art302987.aspx\" target=\"_blank\">牛年本命年 属牛2021本命年要注意什么</a><br><a href=\"/sx/zonghe/art302986.aspx\" target=\"_blank\">2022年犯太岁的生肖以及化解方法</a><br></p>\n" +
              " </div>";
        System.out.println(StringUtil.getStringFilter(str));
    }
}
