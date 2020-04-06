package aaa.webmagic.testjob;

import aaa.webmagic.config.HttpClientDownloader;
import aaa.webmagic.model.JobDTO;
import org.jsoup.Jsoup;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.utils.HttpConstant;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/6
 */
public class GetProcessor implements PageProcessor {
    private Site site = Site.me();

    public static void main(String[] args) {
        //使用 post 请求
        Spider.create(new GetProcessor()).
                addUrl("https://item.taobao.com/item.htm?ft=t&&id=590611020947&ali_trackid=2:mm_26632614_0_0:1586170900_131_1991915643&spm=a21bo.7925890.192091.1&pvid=3500ed92-6a44-46ab-82bb-8162ccfb5923&scm=1007.12846.156652.999999999999999").
                setDownloader(new HttpClientDownloader()).
                run();
    }

    @Override
    public void process(Page page) {
        System.out.println(page.getHtml().xpath("title/text()"));
    }

    @Override
    public Site getSite() {
        return site;
    }
}
