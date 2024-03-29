package aaa.webmagic.testjob;

import aaa.webmagic.config.HttpClientDownloader;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * description: 使用webmagic 爬 京东
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/5
 */
public class MyUtilProcessor implements PageProcessor {

    private Site site = Site.me().
            //设置编码
            setCharset("utf-8").
            //设置超时时间
            setTimeOut(1000*10).
            //设置重试时间
            setRetryTimes(3).
            //设置重试时间
            setSleepTime(1000*3)
            // 设置 cookie
            // addCookie().
            // 设置域名，需设置域名后.addCookie 才可生效
            // setDomain()
            // 添加一条 addHeader
            //         addHeader()
            // 设置 http 代理
            //     setUserAgent()

            ;

    public static void main(String[] args) {
        // 设置 redis 连接池
     /**
      *  public JedisPool(
                 GenericObjectPoolConfig poolConfig, String host, int port, int timeout, String password, int database, String clientName)
    */
     JedisPool jedisPool = new JedisPool(
             new GenericObjectPoolConfig(), "127.0.0.1", 6379, 2000, (String)null, 4, (String)null);

        Spider.create(new MyUtilProcessor()).
                setDownloader(new HttpClientDownloader()).
                addUrl("https://www.jd.com/").
                //默认 控制台打印
                // addPipeline(new ConsolePipeline()).
                //设置文件储存
                // addPipeline(new FilePipeline("C:\\Users\\TLZ\\Desktop\\logs")).
                // 设置 json
                //addPipeline(new JsonFilePipeline("C:\\Users\\TLZ\\Desktop\\logs"))
                //设置布隆过滤器  10000000是估计的页面数量
                 setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000))).

                setScheduler(new RedisScheduler(jedisPool)).
                        thread(5).
                run();

    }

        @Override
        public void process(Page page) {
        //获取标题
        page.putField("div",page.getHtml().css("title").all());

        //不去爬 淘宝的商品，因为淘宝有反扒机制必须要登录才可以
        page.putField("获取到的links 子链接:::::: = ",page.getHtml().$("#navitems > ul > li > a")
                .links().regex(".*jd.com/$").all());
            /**
             * 可以选择使用正则表达式
             * 注意这里的子连接 默认获取的是
             * [https://miaosha.jd.com/, https://a.jd.com/ ......]
             */

       // 获取子连接
        page.addTargetRequests(page.getHtml().$("div#navitems  a")
                .links().all());
            page.putField("子连接标题::::::",page.getHtml().css("title").all());
    }
    @Override
    public Site getSite() {
        return site;
    }


}
