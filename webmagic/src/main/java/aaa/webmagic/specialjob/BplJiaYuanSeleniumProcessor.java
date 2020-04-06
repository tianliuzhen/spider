package aaa.webmagic.specialjob;

import aaa.webmagic.config.HttpClientDownloader;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * description: 这里访问婚庆网需要 先同过 Selenium 获取cookie 带cookie去访问
 *
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/6
 */
public class BplJiaYuanSeleniumProcessor implements PageProcessor {

    /**
     * 用来存储cookie信息
     */
    private Set<Cookie> cookies;
    private Site site= Site.me().setRetryTimes(3).setSleepTime(0).setTimeOut(3000);
    private String webDriverPath = BplJiaYuanSeleniumProcessor.class.getClassLoader().getResource("chromedriver.exe").getPath();

    public static void main(String[] args)  {
        BplJiaYuanSeleniumProcessor job = new BplJiaYuanSeleniumProcessor();
        job.login();
        Spider.create(job).setDownloader(new HttpClientDownloader())
                .addUrl("http://www.jiayuan.com/227057021").run();
    }

    /**
     * 使用 selenium 来模拟用户的登录获取cookie信息
     */
    public void login() {
        //        声明一个DriverServer，只启动一次server，避免多次启动server,节省内存
        ChromeDriverService service=new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(webDriverPath))
                .usingAnyFreePort().build();
        WebDriver driver = null;
        try {
            service.start();
            driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
            driver.get("http://login.jiayuan.com/");
            driver.findElement(By.id("login_email")).clear();
            //在******中填你的用户名
            driver.findElement(By.id("login_email")).sendKeys("15836165756");
            Thread.sleep(1000);
            driver.findElement(By.id("login_password")).clear();
            //在*******填你密码
            driver.findElement(By.id("login_password")).sendKeys("t13673437687");
            Thread.sleep(1000);
            //模拟点击登录按钮
            driver.findElement(By.id("login_btn")).click();
            //获取cookie信息
            cookies = driver.manage().getCookies();
            System.out.println(cookies);
            Thread.sleep(1000*4);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            service.stop();
        }

    }
    @Override
    public void process(Page page) {
        //获取用户的id
        page.putField("", page.getHtml().xpath("//div[@class='member_info_r yh']/h4/span/text()"));

        //获取用户的详细信息
        List<String> information = page.getHtml().xpath("//ul[@class='member_info_list fn-clear']//li/div[@class='fl pr']/em/text()").all();
        page.putField("information = ", information);
    }

    @Override
    public Site getSite() {
        //将获取到的cookie信息添加到webmagic中
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName(),cookie.getValue());
        }
        // 设置模拟登录的浏览器的信息
        return site.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.36 Safari/537.36");

    }
}
