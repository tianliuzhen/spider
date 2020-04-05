package aaa.webmagic.test;

import aaa.webmagic.config.HttpClientDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.pipeline.ResultItemsCollectorPipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * description: 使用webmagic 爬 京东
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/5
 */
public class MyJiDongProcessor implements PageProcessor {

    private Site site = Site.me().
            //设置编码
            setCharset("utf-8").
            //设置超时时间
            setTimeOut(1000*10).
            //设置重试时间
            setRetryTimes(3).
            //设置重试时间
            setSleepTime(1000*3)
            ;

    public static void main(String[] args) {
        Spider.create(new MyJiDongProcessor()).
                setDownloader(new HttpClientDownloader()).
                addUrl("https://www.jd.com/").
                //默认 控制台打印
                // addPipeline(new ConsolePipeline()).
                //设置文件储存
                // addPipeline(new FilePipeline("C:\\Users\\TLZ\\Desktop\\logs")).
                // 设置 json
                //addPipeline(new JsonFilePipeline("C:\\Users\\TLZ\\Desktop\\logs")).

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
