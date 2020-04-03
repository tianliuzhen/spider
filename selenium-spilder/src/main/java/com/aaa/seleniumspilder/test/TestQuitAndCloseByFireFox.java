package com.aaa.seleniumspilder.test;

import com.aaa.seleniumspilder.LagouSearcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/2
 */
public class TestQuitAndCloseByFireFox {
    public static void main(String[] args) {

        //火狐的安装位置
        //System.setProperty("webdriver.firefox.bin","D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");

        System.setProperty("webdriver.firefox.marionette", LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath());
        WebDriver chromeDriver = new FirefoxDriver();
        chromeDriver.get("http://sahitest.com/demo/index.htm");
        // 打开新window1
        ((ChromeDriver) chromeDriver).findElementByPartialLinkText("Window Open Test").click();
        // 打开新window2
        ((ChromeDriver) chromeDriver).findElementByPartialLinkText("Window Open Test With Title").click();
        //查看所有window handles
        chromeDriver.getWindowHandles();

        // 关闭当前窗口，如果是当前打开的最后一个窗口，则退出浏览器
//        chromeDriver.close();

        chromeDriver.getWindowHandles();

        //退出驱动，关闭所有相关的窗口
        chromeDriver.quit();
    }
}
