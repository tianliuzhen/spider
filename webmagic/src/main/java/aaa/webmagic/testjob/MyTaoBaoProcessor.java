package aaa.webmagic.testjob;

import aaa.webmagic.config.HttpClientDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * description: 使用webmagic 爬虫
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/5
 */
public class MyTaoBaoProcessor implements PageProcessor {

    private Site site = Site.me();

    public static void main(String[] args) {
        Spider.create(new MyTaoBaoProcessor()).
                setDownloader(new HttpClientDownloader()).
                addUrl("https://www.taobao.com/?spm=a2e15.8261149.1581860521.1.144d29b4FbFZ7y").
                run();

    }

        @Override
        public void process(Page page) {
        // 解析返回的数据，把解析的结果放在 resultItems
        //1. css 选择器
        page.putField("div",page.getHtml().css("title").all());
        //2. XPath
        page.putField("div2",
                page.getHtml().xpath("//div[@class='site-nav-menu-bd site-nav-menu-list']/div/a[1]/text()").get());
        //3. css+正则表达式  链式组合筛选
       page.putField("div3",page.getHtml().css("div#J_SearchTab ul ").regex(".天猫.").all());
        //不去爬 淘宝的商品，因为淘宝有反扒机制必须要登录才可以
        page.putField("获取到的links 子链接 = ",page.getHtml().$("li#J_SiteNavService  a").links().
                regex(".*com$").all());
       //4. 获取连接
        page.addTargetRequests(page.getHtml().$("li#J_SiteNavService  a")
                .links().regex(".*com$").all());
            page.putField("children_url",page.getHtml().css("title").all());
    }
    @Override
    public Site getSite() {
        return site;
    }


}
