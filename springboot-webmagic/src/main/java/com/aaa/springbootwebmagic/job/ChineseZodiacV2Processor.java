package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.config.HttpClientDownloader;
import com.aaa.springbootwebmagic.domain.ArtUtil;
import com.aaa.springbootwebmagic.domain.ItemDTO;
import com.aaa.springbootwebmagic.domain.SxDTO;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * description: 采集十二生效
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
public class ChineseZodiacV2Processor implements PageProcessor {

    private Site site= Site.me().setRetryTimes(3).setSleepTime(0).setTimeOut(3000);

    public static void main(String[] args) {
        Spider.create(new ChineseZodiacV2Processor()).setDownloader(new HttpClientDownloader())
                .addUrl("https://www.d1xz.net/sx/").run();
    }

    @Override
    public void process(Page page) {
        List<SxDTO> sxDTOS = Lists.newArrayList();
        List<String> all = page.getHtml().css("div[class='item_ml']").all();
        //抓取  生肖运势 、生肖性格、生肖爱情、生肖解说  第一层url
        for (String s : all) {
            Elements select1 = Jsoup.parse(s).select(".title ");
            for (Element element : select1) {
                String sxTitle = element.select("a strong").text();
                String url = element.select("a").attr("href");
                page.addTargetRequest("https://www.d1xz.net"+url);
            }
        }
       //抓取  生肖运势 、生肖性格、生肖爱情、生肖解说  第二层url
        System.out.println(page.getHtml().xpath("//div[@class='main_left fl dream_box']/div/text()"));

    }

    @Override
    public Site getSite() {
        return site;
    }
}
