package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.config.HttpClientDownloader;
import com.aaa.springbootwebmagic.domain.ArtUtil;
import com.aaa.springbootwebmagic.domain.ItemDTO;
import net.minidev.json.JSONObject;
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
public class ChineseZodiacProcessor implements PageProcessor {

    private Site site= Site.me().setRetryTimes(3).setSleepTime(0).setTimeOut(3000);

    public static void main(String[] args) {
        Spider.create(new ChineseZodiacProcessor()).setDownloader(new HttpClientDownloader())
                .addUrl("https://www.d1xz.net/").run();
    }

    @Override
    public void process(Page page) {
        List<ItemDTO> list = Lists.newArrayList();
        List<String> atrAll = page.getHtml().xpath("//div[@class='right fr']/dl").all();
        for (String s : atrAll) {
            //星座  constellation
            //生肖 chineseZodiac
            //爱情测试 loveTest
            //推荐 commend
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setTitle(Jsoup.parse(s).select("em").text());
            itemDTO.setTitleHref(Jsoup.parse(s).select("dt a ").attr("href"));
            List<ArtUtil> artUtils = Lists.newArrayList();
            Elements dd = Jsoup.parse(s).select("dd");
            for (Element element : dd) {
                Elements a = Jsoup.parse(element.html()).select("a");
                for (Element element1 : a) {
                    ArtUtil artUtil = new ArtUtil();
                    artUtil.setTitle(element1.html());
                    artUtil.setHref(element1.attr("href"));
                    artUtils.add(artUtil);
                }

            }
            itemDTO.setList(artUtils);
            list.add(itemDTO);
        }
        System.out.println(atrAll);


    }

    @Override
    public Site getSite() {
        return site;
    }
}
