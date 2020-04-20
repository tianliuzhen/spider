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
    public static int getSxType(String str){
        int code = 1;
        switch (str){
            case "鼠":
                code=1;
                break;
            case "牛":
                code=2;
                break;
            case "虎":
                code=3;
                break;
            case "兔":
                code=4;
                break;
            case "龙":
                code=5;
                break;
            case "蛇":
                code=6;
                break;
            case "马":
                code=7;
                break;
            case "羊":
                code=8;
                break;
            case "猴":
                code=9;
                break;
            case "鸡":
                code=10;
                break;
            case "狗":
                code=11;
                break;
            case "猪":
                code=12;
                break;
        }
        return code;
    }
}
