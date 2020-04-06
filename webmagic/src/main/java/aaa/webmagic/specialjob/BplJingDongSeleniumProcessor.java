package aaa.webmagic.specialjob;

import aaa.webmagic.config.HttpClientDownloader;
import aaa.webmagic.model.JDGoodsInfo;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
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

import javax.xml.xpath.XPath;
import java.io.File;
import java.util.ArrayList;
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
public class BplJingDongSeleniumProcessor implements PageProcessor {

    private String webDriverPath = BplJiaYuanSeleniumProcessor.class.getClassLoader().getResource("chromedriver.exe").getPath();

    /**
     * 用来存储cookie信息
     */
    private Set<Cookie> cookies=new LinkedHashSet<>();
    private Site site= Site.me().setRetryTimes(3).setSleepTime(0).setTimeOut(3000);

    /**
     * 用来存储 爬取手机的Dto
     */
    private   List<JDGoodsInfo> jdGoodsInfos = Lists.newArrayList();
    /**
     * 用来存储 爬取分页的次数
     */
    private int pageCurrent = 1,pageAll = 10;


    public static void main(String[] args)  {
        BplJingDongSeleniumProcessor job = new BplJingDongSeleniumProcessor();
        job.login();
        Spider.create(job).setDownloader(new HttpClientDownloader())
                .addUrl("https://search.jd.com/Search?keyword=%E7%94%B5%E8%84%91&enc=utf-8&pvid=45a4b3d5486e470982a84c392bb68bef").run();
    }

    @Override
    public void process(Page page) {

        System.out.println("爬取的标题::"+page.getHtml().xpath("title/text()"));
        //获取的一个大的Div,包含商品的 价格、图片、连接 ....
        List<String> lists = page.getHtml().xpath("//div[@id='J_goodsList']/ul/li/").all();
        // 处理方式1 、xpath (不推荐 )
        xpathHanderHtml(page, jdGoodsInfos);
        // 处理方式2、采用Jsoup (推荐 个人觉得比较好用)
        for (String sHtml : lists) {
            String goodUtilUrl = Jsoup.parse(sHtml).select("div.p-img a").attr("href");
            String goodsTitle = Jsoup.parse(sHtml).select("div[class='p-name p-name-type-2'] a").text();
            String goodsImg = Jsoup.parse(sHtml).select("div[class='p-name p-name-type-2'] a").text();
            String price =  Jsoup.parse(sHtml).select("div.p-price i").text();
            JDGoodsInfo jdGoodsInfo = new JDGoodsInfo(Double.parseDouble(StringUtil.isBlank(price) ? "0.00" : price)
                    ,goodsTitle,goodUtilUrl,goodsImg);
            jdGoodsInfos.add(jdGoodsInfo);
        }

        if (pageCurrent<=pageAll){
            String pageUrl = "https://search.jd.com/Search?keyword=%E7%94%B5%E8%84%91&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&page="+pageCurrent+"&s=52&click=0";
            page.addTargetRequest(pageUrl);
        }
        pageCurrent++;
        System.out.println(jdGoodsInfos.size());
    }

    public void xpathHanderHtml(Page page, List<JDGoodsInfo> jdGoodsInfos) {
        String price =page.getHtml().xpath("//div[@id='J_goodsList']/ul/li/div/[2]/strong/i/text()").get();
        String goodsTitle =page.getHtml().xpath("//div[@id='J_goodsList']/ul/li/div/[3]/a/em/text()").get();
        String goodUtilUrl =page.getHtml().xpath("//div[@id='J_goodsList']/ul/li/div/[3]//a/@href").get();
        String goodsImg = page.getHtml().xpath("//div[@id='J_goodsList']/ul/li/div/[1]/a/img/@source-data-lazy-img").get();
        JDGoodsInfo jdGoodsInfo = new JDGoodsInfo(Double.parseDouble(StringUtil.isBlank(price) ? "0.00" : price),goodsTitle,goodUtilUrl,goodsImg);
        System.out.println(jdGoodsInfo);
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
            driver.get("https://passport.jd.com/uc/login");
            Thread.sleep(1000*2);
            // 默认是扫码登陆，切换账户登陆，这里采用两种 xpath和jsoup均可
            // driver.findElement(By.xpath("//div[@class='login-form']/div[3]/a")).click();
            driver.findElement(By.cssSelector("div[class='login-tab login-tab-r'] a")).click();
            driver.findElement(By.id("loginname")).clear();
            //在******中填你的用户名
            driver.findElement(By.id("loginname")).sendKeys("15836165756");
            Thread.sleep(1000);
            driver.findElement(By.id("nloginpwd")).clear();
            //在*******填你密码
            driver.findElement(By.id("nloginpwd")).sendKeys("t13673437687");

            //模拟点击登录按钮
            Thread.sleep(1000*4);
            driver.findElement(By.id("loginsubmit")).click();
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
}
