package com.aaa.seleniumspilder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/2
 */
public class LagouSearcher {
    public static void main(String[] args) throws InterruptedException {

        //设置webdriver 路径
        System.setProperty("webdriver.chrome.dirver",LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath());
        WebDriver chromeDriver = new ChromeDriver();
        chromeDriver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");
        //xpath 选择元素
        //模拟浏览器点击
        clickOption(chromeDriver, "工作经验", "应届毕业生");
        clickOption(chromeDriver, "学历要求", "大专");
        clickOption(chromeDriver, "融资阶段", "未融资");
        clickOption(chromeDriver, "行业领域", "移动互联网");

        // 解析页面元素
        List<WebElement> jobElements = chromeDriver.findElements(By.className("con_list_item"));
        for (WebElement jobElement : jobElements) {
            WebElement element = jobElement.findElement(By.className("position")).findElement(By.className("money"));
            System.out.println("薪资 = " + element.getText());
            String companyName = jobElement.findElement(By.className("company_name")).getText();
            System.out.println("companyName = " + companyName);
        }

        Thread.sleep(1000*10);
        chromeDriver.quit();
    }


    private static void clickOption(WebDriver chromeDriver, String choseTitle, String optionTitle) {
        WebElement choosenElement = chromeDriver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + choseTitle + "')]"));
        WebElement optionElement = choosenElement.findElement(By.xpath("../a[contains(text(),'" + optionTitle + "')]"));

        // 模拟浏览器点击 工作性质  应届毕业生
        optionElement.click();
    }
}
