package com.aaa.seleniumspilder.test;

import com.aaa.seleniumspilder.LagouSearcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.File;
import java.io.IOException;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/2
 */

import org.openqa.selenium.chrome.ChromeDriver;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/2
 */
public class TestQuitAndCloseByChrome {

    private static String webDriverPath=LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath();


    public static void main(String[] args) throws InterruptedException {


        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(
                new File(webDriverPath)).usingAnyFreePort().build();
        ChromeDriver chromeDriver = null;
        try {
            service.start();
            System.setProperty("webdriver.chrome.dirver", webDriverPath);
            chromeDriver = new ChromeDriver();
            chromeDriver.get("http://sahitest.com/demo/index.htm");
            // 打开新window1
            chromeDriver.findElementByPartialLinkText("Window Open Test").click();
            // 打开新window2
            chromeDriver.findElementByPartialLinkText("Window Open Test With Title").click();
            //查看所有window handles
            chromeDriver.getWindowHandles();

            // 关闭当前窗口，如果是当前打开的最后一个窗口，则退出浏览器
//        chromeDriver.close();

            chromeDriver.getWindowHandles();

            //退出驱动，关闭所有相关的窗口
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Thread.sleep(2*1000);
            chromeDriver.quit();
            service.stop();
        }

    }
}
