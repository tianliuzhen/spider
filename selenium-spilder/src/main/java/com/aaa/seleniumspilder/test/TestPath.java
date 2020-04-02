package com.aaa.seleniumspilder.test;

import com.aaa.seleniumspilder.LagouSearcher;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/2
 */
public class TestPath {
    public static void main(String[] args) {
        System.out.println(LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath());


    }
}
