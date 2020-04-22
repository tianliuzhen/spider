package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.config.HttpClientDownloader;
import com.aaa.springbootwebmagic.domain.ArtTypeUtil;
import com.aaa.springbootwebmagic.domain.SxDTO;
import com.aaa.springbootwebmagic.domain.SxIndexRoll;
import com.aaa.springbootwebmagic.domain.SxUtil;
import com.aaa.springbootwebmagic.domain.entity.SxMain12;
import com.aaa.springbootwebmagic.domain.entity.SxUtilType;
import com.aaa.springbootwebmagic.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
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
    private    SxV1Pipeline sxV1Pipeline;

    private int i = 1;
    public static final String NET = "https://www.d1xz.net";



    private Site site= Site.me().setRetryTimes(5).setSleepTime(100).setTimeOut(5000).setCharset("utf-8")
            .setCycleRetryTimes(2);

    public   void main() {
        Spider.create(new ChineseZodiacV1Processor()).setDownloader(new HttpClientDownloader())
                .addUrl("https://www.d1xz.net/sx/")
                .addPipeline(sxV1Pipeline)
                .thread(20).run();
    }

    @Override
    public void process(Page page) {
        getAll12(page);

        CommonProcessor.crawlIndex_4(page,1);
        i++;
    }
    private void getAll12(Page page) {
        //1. 采集详情
        crawlIndex_1(page);
        //2. 采集列表
        crawlIndex_2(page);

    }

    private void crawlIndex_2(Page page) {
        List<SxDTO> sxUtilTypes = Lists.newArrayList();

        // 1.  class=same_list short_list
        getClassByDiff(page,"same_list short_list",sxUtilTypes);

        // 2.  class=same_list h277
        getClassByDiff(page,"same_list h277",sxUtilTypes);

        System.out.println("总共发起url = " + i);
    }

    private void getClassByDiff(Page page,String classType,List<SxDTO> sxUtilTypes) {

        if(!page.getUrl().toString().equals("https://www.d1xz.net/sx/")){
            List<String> all = page.getHtml().xpath("//div[@class='box_xz_title']//span/text()").all();
            List<String> div = page.getHtml().xpath("//div[@class='"+classType+"']").all();
            for (int i1 = 0; i1 < div.size(); i1++) {
              String  s = div.get(i1);
              Elements select1 = Jsoup.parse(s).select("div ul[class='pic_ui fl']");
              Elements select2 = Jsoup.parse(s).select("div ul[class='list_u fl']");
              if( select1.size()>0 &&  select1.size()==select2.size() ){
                  for (int i2 = 0; i2 < select1.size(); i2++) {
                      SxDTO sxDTO2 = new SxDTO();
                      Elements element1 = select1.get(i2).select("li");
                      if(element1.size()>=1){
                          sxDTO2.setImgSrc1(element1.get(0).select("img").attr("src"));
                          sxDTO2.setImgTitle1(element1.get(0).select("img").attr("alt"));
                      }
                      if(element1.size()>=2 ){
                          sxDTO2.setImgSrc2(element1.get(1).select("img").attr("src"));
                          sxDTO2.setImgTitle2(element1.get(1).select("img").attr("alt"));
                      }
                      List<SxUtil> list=Lists.newArrayList();
                      System.out.println();
                      Elements li_a = select2.get(i2).select("li a");
                      for (Element element : li_a) {
                          SxUtil sxUtil = new SxUtil();
                          sxUtil.setSxArtTitle(element.text());
                          sxUtil.setSxArtHref(StringUtil.getUrlArtId(element.attr("href")));
                          page.addTargetRequest(element.attr("href"));
                          list.add(sxUtil);
                      }
                      sxDTO2.setList(list);
                      String s1 = page.getHtml().xpath("//span[@class='fb st_sx']/text()").get().substring(1, 2);
                      sxDTO2.setCode(StringUtil.getSxType(s1));
                      sxDTO2.setSxTypeHref(String.valueOf(i2+1));
                      // sxDTO2.setSxTypeHref();
                      if(div.size()==1){
                          sxDTO2.setSxTypeName(all.get(0));
                      }
                      if (div.size()>=3){
                          sxDTO2.setSxTypeName(all.get(i1+1));
                      }
                      sxUtilTypes.add(sxDTO2);
                      page.putField("sxDTOS2", sxUtilTypes);
                  }

              }



        }
        }
    }




    private void crawlIndex_1(Page page) {
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
            String s1 = page.getHtml().xpath("//span[@class='fb st_sx']/text()").get().substring(1, 2);
            SxMain12 sxMain12 = new SxMain12();
            sxMain12.setTitle(title).setTitleDesc(desc).setImgUrl(NET+imgUrl).setInfo(info)
            .setCode(StringUtil.getSxType(s1));
            page.putField("sx_12_main",sxMain12);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }


}
