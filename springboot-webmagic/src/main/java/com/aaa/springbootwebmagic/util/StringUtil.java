package com.aaa.springbootwebmagic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/20
 */
public class StringUtil {
    public static   int getUrlArtId(String args) {
        String str = args;
        String reg = "(?<=art).*(?=\\.)";//定义正则表达式

        Pattern patten = Pattern.compile(reg);//编译正则表达式
        Matcher matcher = patten.matcher(str);// 指定要匹配的字符串

        List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }
        int result = 0;
        for (int i = 0; i < matchStrs.size(); i++) {
            result = Integer.parseInt(matchStrs.get(i));
        }
        return result;
    }
    public static String getSxType(String str){
        String code = "";
        switch (str){
            case "鼠":
                code="shu";
                break;
            case "牛":
                code="niu";
                break;
            case "虎":
                code="hu";
                break;
            case "兔":
                code="tu";
                break;
            case "龙":
                code="long";
                break;
            case "蛇":
                code="she";
                break;
            case "马":
                code="ma";
                break;
            case "羊":
                code="yang";
                break;
            case "猴":
                code="hou";
                break;
            case "鸡":
                code="ji";
                break;
            case "狗":
                code="gou";
                break;
            case "猪":
                code="zhu";
                break;
        }
        return code;
    }
}
