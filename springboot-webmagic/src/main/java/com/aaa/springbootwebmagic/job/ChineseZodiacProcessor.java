package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.config.HttpClientDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectors;

/**
 * description: 采集十二生效
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
public class ChineseZodiacProcessor implements PageProcessor {

    private Site site = Site.me();

    public static void main(String[] args) {
        Spider.create(new ChineseZodiacProcessor()).setDownloader(new HttpClientDownloader())
                .addUrl("https://www.d1xz.net/").run();
    }

    @Override
    public void process(Page page) {
        // 这三种方式一样
        System.out.println(page.getHtml().css(("title")).get());
        System.out.println(page.getHtml().$(("title")).get());
        System.out.println(page.getHtml().select(Selectors.$("title")).get());
    }

    @Override
    public Site getSite() {
        return site;
    }
}
