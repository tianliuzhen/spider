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
import java.util.Set;

/**
 * description: 访问淘宝进行测试
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
                .addUrl("https://item.taobao.com/item.htm?id=592936211013&ali_refid=a3_430673_1006:1105538482:N:emtiAWsF8%2Bzhhxaiwzc0Aw%3D%3D:88b6b28e0166aa7a04636a174564fab6&ali_trackid=1_88b6b28e0166aa7a04636a174564fab6&spm=a2e15.8261149.07626516002.2").run();
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
            driver.get("https://login.taobao.com/");
            driver.findElement(By.id("TPL_username_1")).clear();
            //在******中填你的用户名
            driver.findElement(By.id("TPL_username_1")).sendKeys("15836165756");
            Thread.sleep(1000);
            driver.findElement(By.id("TPL_password_1")).clear();
            //在*******填你密码
            driver.findElement(By.id("TPL_password_1")).sendKeys("t13673437687");
            Thread.sleep(1000);
            //模拟点击登录按钮
            driver.findElement(By.id("J_SubmitStatic")).click();
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
        page.putField("爬取淘宝商品详情的标题：",page.getHtml().xpath("title/text()"));
    }

    @Override
    public Site getSite() {
        //将获取到的cookie信息添加到webmagic中
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName(),cookie.getValue());
        }
        return site.addHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.36 Safari/537.36");

    }
}
