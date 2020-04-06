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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * description: 淘宝这里无效
 *
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/6
 */
public class BplTaoBaoSeleniumProcessor implements PageProcessor {

    /**
     * 用来存储cookie信息
     */
    private Set<Cookie> cookies=new LinkedHashSet<>();
    private Site site= Site.me().setRetryTimes(3).setSleepTime(0).setTimeOut(3000);
    private String webDriverPath = BplTaoBaoSeleniumProcessor.class.getClassLoader().getResource("chromedriver.exe").getPath();

    public static void main(String[] args)  {
        BplTaoBaoSeleniumProcessor job = new BplTaoBaoSeleniumProcessor();
        // job.login();
        Spider.create(job).setDownloader(new HttpClientDownloader())
                .addUrl("https://uland.taobao.com/sem/tbsearch?refpid=mm_26632258_3504122_32538762&keyword=%E5%A5%B3%E8%A3%85&clk1=e183cd8b198836c884cffbb65c0f1928&upsid=e183cd8b198836c884cffbb65c0f1928").run();
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
            driver.get("https://login.taobao.com/member/login.jhtml");
            driver.findElement(By.id("TPL_username_1")).clear();
            //在******中填你的用户名
            driver.findElement(By.id("TPL_username_1")).sendKeys("15836165756");
            Thread.sleep(1000);
            driver.findElement(By.id("TPL_password_1")).clear();
            //在*******填你密码
            driver.findElement(By.id("TPL_password_1")).sendKeys("t13673437687");

            //模拟点击登录按钮
            Thread.sleep(1000*4);
            driver.findElement(By.id("J_SubmitStatic")).click();
            //获取cookie信息
            cookies = driver.manage().getCookies();
            System.out.println(cookies);
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            service.stop();
        }

    }
    @Override
    public void process(Page page) {
        System.out.println("爬取的标题"+page.getHtml().xpath("title/text()"));
        System.out.println(page.getHtml().css("div[class='itemLink'] span","text"));
        System.out.println(page.getHtml().xpath("//div[@class=item]/[@class=imgLink]/text()"));
    }

    @Override
    public Site getSite() {
        //将获取到的cookie信息添加到webmagic中
        if (!cookies.isEmpty()) {
            for (Cookie cookie : cookies) {
                site.addCookie(cookie.getName(),cookie.getValue());
            }
            // 设置模拟登录的浏览器的信息
            return site.addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.36 Safari/537.36");

        }
        return site;
    }
}
