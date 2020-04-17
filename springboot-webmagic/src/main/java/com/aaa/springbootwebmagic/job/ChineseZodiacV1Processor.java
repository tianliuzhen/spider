package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.config.HttpClientDownloader;
import com.aaa.springbootwebmagic.domain.SxIndexRoll;
import com.aaa.springbootwebmagic.domain.entity.SxMain12;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/17
 */
@Component
public class ChineseZodiacV1Processor implements PageProcessor {

    @Autowired
    private    SxPipeline sxPipeline;

    private int i = 1;
    public static final String NET = "https://www.d1xz.net";



    private Site site= Site.me().setRetryTimes(5).setSleepTime(100).setTimeOut(5000).setCharset("utf-8")
            .setCycleRetryTimes(2);

    public   void main() {
        Spider.create(new ChineseZodiacV1Processor()).setDownloader(new HttpClientDownloader())
                .addUrl("https://www.d1xz.net/sx/")
                .addPipeline(sxPipeline)
                .thread(1).run();
    }

    @Override
    public void process(Page page) {
        getAll12(page);
        i++;
    }
    private void getAll12(Page page) {
        //因为每个页面都有相同的地方，所以只抓取一次
        if(i==1){
            List<String> all1 = page.getHtml().xpath("//ul[@class='constellation nav_zodiac']/li/a/@href").all();
            for (String s : all1) {
                page.addTargetRequest(NET+s);
            }
        }
        String imgUrl = page.getHtml().xpath("//div[@class='xz_pic fl']//img/@src").get();
        String title = page.getHtml().xpath("//p[@class='words']/span[1]/text()").get();
        String desc = page.getHtml().xpath("//p[@class='words']/span[2]/text()").get();
        String info = page.getHtml().xpath("//div[@class='xz_det z_det fr']//p[@class='txt']/text()").get();
        if(StringUtils.isNotBlank(imgUrl)){
            SxMain12 sxMain12 = new SxMain12();
            sxMain12.setTitle(title).setTitleDesc(desc).setImgUrl(NET+imgUrl).setInfo(info);
            page.putField("sx_12_main",sxMain12);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}
