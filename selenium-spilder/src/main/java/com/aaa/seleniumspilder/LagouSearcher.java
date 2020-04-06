package com.aaa.seleniumspilder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * description:
 *                                                           测试环境
 *            1\  谷歌浏览器   版本 79.0.3941.4（正式版本） （64 位）
 *            2\  谷歌插件     http://chromedriver.storage.googleapis.com/79.0.3945.36/chromedriver_win32.zip
 *            3\  64位windows 使用 win 32 插件是可以的,但是会有小小的bug.
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/2
 */
public class LagouSearcher {

    private static String webDriverPath=LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath();


    public static void main(String[] args){
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(
                new File(webDriverPath)).usingAnyFreePort().build();
        WebDriver chromeDriver = null;
        try {
            service.start();
            // ChromeOptions options = new ChromeOptions();
            // chromeDriver = new ChromeDriver(options);
            //同一个台机器上安装了多个不同版本的Chrome 时，可通过setBinary 指定待测试Chrome
            //options.setBinary ("E:/PCSoftware/Google/79/Google/Chrome/Application/");
            /**
             * 这里有坑啊，同志们
             * 谷歌浏览器的安装位置 必须在 当前目录下
             * eg：C:\Users\TLZ\AppData\Local\Google\Chrome\Application\chrome.exe
             * 浏览器不要使用安装版本，否则每次还是启动安装版本。
             * 我这本来  使用安装版本  随意指定的位置
             * 但是一直报错
             *    Starting ChromeDriver 79.0.3945.36 (3582db32b33893869b8c1339e8f4d9ed1816f143-refs/branch-heads/3945@{#614}) on port 29458
             *    Only local connections are allowed.
             *    Please protect ports used by ChromeDriver and related test frameworks to prevent access by malicious code.
             * 经过测试就是 浏览器位置的原因，后来下了一个 免安装的 谷歌 79.0.3945.36 版本，就可以了
             */
            chromeDriver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());

            //设置webdriver 路径, 上面已经设置了
            // System.setProperty("webdriver.chrome.dirver",webDriverPath);

            chromeDriver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");
            //xpath 选择元素

            clickOption(chromeDriver, "工作经验", "应届毕业生");
            clickOption(chromeDriver, "学历要求", "不限");
            clickOption(chromeDriver, "融资阶段", "不限");
            clickOption(chromeDriver, "行业领域", "不限");
            Thread.sleep(1000);
            // 解析页面元素
            extractJobsByPagination(chromeDriver);

            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭窗口
            chromeDriver.quit();
            //杀死进程
            service.stop();
        }

    }

    private static void extractJobsByPagination(WebDriver chromeDriver) throws InterruptedException {
        List<WebElement> jobElements = chromeDriver.findElements(By.className("con_list_item"));
        for (WebElement jobElement : jobElements) {
            WebElement element = jobElement.findElement(By.className("position")).findElement(By.className("money"));
            System.out.println("薪资 = " + element.getText());
            String companyName = jobElement.findElement(By.className("company_name")).getText();
            System.out.println("公司 = " + companyName);
        }
        //
        WebElement nextPageBtn = chromeDriver.findElement(By.className("pager_next"));
        if (! nextPageBtn.getAttribute("class").contains("pager_next_disabled")) {
            nextPageBtn.click();

            WebElement pagerIsCurrent = chromeDriver.findElement(By.className("pager_is_current"));
            System.out.println("当前页是: "+pagerIsCurrent.getText());
            Thread.sleep(1000);
            extractJobsByPagination(chromeDriver);
        }

    }


    private static void clickOption(WebDriver chromeDriver, String choseTitle, String optionTitle) {
        WebElement choosenElement = chromeDriver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + choseTitle + "')]"));
        WebElement optionElement = choosenElement.findElement(By.xpath("../a[contains(text(),'" + optionTitle + "')]"));

        // 模拟浏览器点击
        optionElement.click();
    }
}
