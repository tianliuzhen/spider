package com.aaa.seleniumspilder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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
    public static void main(String[] args){
        WebDriver chromeDriver = null;
        try {
            //设置webdriver 路径
            System.setProperty("webdriver.chrome.dirver",LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath());
            chromeDriver = new ChromeDriver();
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

        }finally {
            chromeDriver.close();
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
