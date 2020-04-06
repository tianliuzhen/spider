package com.aaa.seleniumspilder.test;

import com.aaa.seleniumspilder.LagouSearcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/2
 */
public class TestQuitAndClose {

    private static String webDriverPath=LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath();


    public static void main(String[] args) throws InterruptedException {


        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(
                new File(webDriverPath)).usingAnyFreePort().build();
        try {
            service.start();
            URL url = service.getUrl();
            openUrl(url,"http://sahitest.com/demo/index.htm");

            //退出驱动，关闭所有相关的窗口
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Thread.sleep(2*1000);
            service.stop();
        }
    }
    public static void openUrl(URL url, String urlPath) throws InterruptedException {
        WebDriver chromeDriver = null;
        try {
            Thread.sleep(3000);
            System.setProperty("webdriver.chrome.dirver", webDriverPath);
            chromeDriver = new RemoteWebDriver(url,DesiredCapabilities.chrome());
            chromeDriver.get(urlPath);
            // 打开新window1
            chromeDriver.findElement(By.linkText("Window Open Test")).click();
            Thread.sleep(3000);
            // 打开新window2
            chromeDriver.findElement(By.linkText("Window Open Test With Title")).click();
            Thread.sleep(3000);
            //查看所有window handles
            chromeDriver.getWindowHandles();
            // 关闭当前窗口，如果是当前打开的最后一个窗口，则退出浏览器
            // chromeDriver.close();
            chromeDriver.getWindowHandles();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            Thread.sleep(3000);
            chromeDriver.close();
        }


    }
}
