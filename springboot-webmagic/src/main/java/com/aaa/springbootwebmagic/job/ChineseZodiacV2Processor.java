package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.config.HttpClientDownloader;
import com.aaa.springbootwebmagic.domain.SxDTO;
import com.aaa.springbootwebmagic.domain.SxIndexRoll;
import com.aaa.springbootwebmagic.domain.SxTypeListDTO;
import com.aaa.springbootwebmagic.domain.SxUtil;
import com.aaa.springbootwebmagic.domain.entity.SxMain12Sub;
import com.aaa.springbootwebmagic.domain.entity.SxTypeList;
import com.aaa.springbootwebmagic.util.StringUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * description: 采集十二生效
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/14
 */
@Service
public class ChineseZodiacV2Processor implements PageProcessor {

    public static final String NET = "https://www.d1xz.net";
    /**
     * 限制分页条数
     */
    public static final int PAGE_LIMIT = 2;
    public static final String AN_OBJECT = "专题";

    private Site site= Site.me().setRetryTimes(5).setSleepTime(100).setTimeOut(5000).setCharset("utf-8")
            .setCycleRetryTimes(2);

    private int i = 0;

    public static  int PAGE_INT = 1,xingge = 1,zonghe =1,aiqing =1 ,jieshuo=1;

    @Autowired
    private SxV2Pipeline sxV2Pipeline;

    /**
     *    fixedDelay 每隔几秒执行一次
     *    initialDelay 启动后执行
     * @return void
     */
    public  void main() {
        Spider.create(new ChineseZodiacV2Processor()).setDownloader(new HttpClientDownloader())
                .addUrl("https://www.d1xz.net/sx/")
                .addPipeline(this.sxV2Pipeline)
                .thread(20).run();
    }


    @Override
    public void process(Page page) {

        //1.抓取首页
        crawlIndex_1(page);
        //2. 抓取  生肖运势 、生肖性格、生肖爱情、生肖解说  第二层url （文章列表）
        crawlIndex_2(page);
        //3. 抓取  生肖运势 、生肖性格、生肖爱情、生肖解说  第三层url  (文章详情)
        crawlIndex_3(page);
        //4. 存入文章类型详情
        CommonProcessor.crawlIndex_4(page,2);
        //5. 采集专题
        crawlIndex_5(page);
        System.out.println("总共发起url = " + ++i);

    }

    private void crawlIndex_5(Page page) {
        List<SxTypeListDTO> sxTypeListDTOS = Lists.newArrayList();
            String title = page.getHtml().xpath("//div[@class='cur_postion w960']/span[2]/text()").get();
            if(StringUtils.isNotBlank(title) && title.equals(AN_OBJECT)){
                System.out.println();
                String src = page.getHtml().xpath("//a[@class='pic_adv relative']/img/@src").get();
                String alt = page.getHtml().xpath("//a[@class='pic_adv relative']/img/@alt").get();
                String desc = page.getHtml().xpath("//div[@class='describe_words']/p/text()").get();
                String code = StringUtil.getSxType(alt.substring(1, 2));
                code = StringUtils.isBlank(code) ? "all" : code;
                List<String> all = page.getHtml().xpath("//ul[@class='words_list_ui']/li").all();
                for (String s : all) {
                    String href = Jsoup.parse(s).select("a").attr("href");
                    String title2 = Jsoup.parse(s).select("a img").attr("alt");
                    String src2 = Jsoup.parse(s).select("a img").attr("src");
                    String info = Jsoup.parse(s).select("li p").first().text();
                    page.addTargetRequest(href);
                    SxTypeListDTO sxTypeList = new SxTypeListDTO();
                    sxTypeList.setArtCode(StringUtil.getUrlArtId(href)).setHref(src2).setTitle(title2).setTitleDesc(info).setSxTypeCode(code);
                    sxTypeListDTOS.add(sxTypeList);
                }
                // 放入自定义管道 3
                if (sxTypeListDTOS.size()>0) {
                    page.putField("sxTypeListDTOS",sxTypeListDTOS);
                }
                SxMain12Sub sxMain12Sub = new SxMain12Sub();
                sxMain12Sub.setCode(code).setImgUrl(src).setTitle(alt).setInfo(desc);
                page.putField("SxMain12Sub",sxMain12Sub);
            }


    }


    private void crawlIndex_3(Page page) {
        List<SxTypeListDTO> sxTypeListDTOS = Lists.newArrayList();
        String type = page.getHtml().xpath("//div[@class='main_left fl dream_box']/div/text()").get();
        if (StringUtils.isNotBlank(type)) {
            List<String> allType = page.getHtml().xpath("//ul[@class='words_list_ui']/").all();
            for (String s : allType) {
                SxTypeListDTO sxTypeListDTO = new SxTypeListDTO();
                sxTypeListDTO.setTitle(Jsoup.parse(s).select("li a").text());
                sxTypeListDTO.setTitleDesc(Jsoup.parse(s).select("li p").text());
                String url = NET + Jsoup.parse(s).select("li a").attr("href");
                sxTypeListDTO.setHref(url);
                sxTypeListDTO.setArtCode(StringUtil.getUrlArtId(url));
                //todo  ===》   创建url循环进行循环抓取 文章详情
                page.addTargetRequest(url);
                sxTypeListDTO.setSxTypeCode(StringUtil.getCodeSwitch(type));
                sxTypeListDTOS.add(sxTypeListDTO);
            }
            // 截取 url 的 数字作为id eg：/sx/zonghe/index_3.aspx ==》 获取 zonghe
            String nextPageType = page.getHtml().xpath("//span[@class='next']/a/@href").regex("(?<=sx/).*(?=/index)").get();
            if(geTypeSwitch(nextPageType) < PAGE_LIMIT){
                // todo   ===》  分页获取文章列表
                page.addTargetRequest("https://www.d1xz.net/sx/"+nextPageType+"/index_"+ geTypeSwitch(nextPageType) +".aspx");
            }
        }
        // 放入自定义管道 3
        if (sxTypeListDTOS.size()>0) {
            page.putField("sxTypeListDTOS",sxTypeListDTOS);
        }
    }

    private void crawlIndex_2(Page page) {
        List<SxDTO> sxDTOS = Lists.newArrayList();
        List<String> all = page.getHtml().css("div[class='item_ml'] > div").all();

        for (String s : all) {
            SxDTO sxDTO = new SxDTO();
            String sxTypeName =   Jsoup.parse(s).select(".title a").text();
            String sxTypeHref =   Jsoup.parse(s).select(".title a").attr("href");
            //todo  ===》   创建url循环进行循环抓取
            page.addTargetRequest("https://www.d1xz.net"+sxTypeHref);
            sxDTO.setSxTypeName(sxTypeName).setSxTypeHref(sxTypeHref);
            // 封装内层
            Elements select = Jsoup.parse(s).select("div[class='same_list h277 '] li");
            List<SxUtil> sxUtils = Lists.newArrayList();
            for (Element element : select) {
                SxUtil sxUtil = new SxUtil();
                sxUtil.setSxArtTitle(element.text()).setSxArtHref(StringUtil.getUrlArtId(element.select("a").attr("href")));
                sxUtils.add(sxUtil);
                page.addTargetRequest(NET+element.select("a").attr("href"));
            }
            Elements select1 = Jsoup.parse(s).select("ul[class='pic_ui fl'] li ");
            if(select1.size()>=2){
                String aHref1 = select1.get(0).select("a").attr("href");
                String aHref2 = select1.get(0).select("a").attr("href");
                sxDTO.setImgSrc1(select1.get(0).select("img").attr("src")+"?"+StringUtil.getUrlArtId(aHref1));
                sxDTO.setImgSrc2(select1.get(0).select("img").attr("src")+"?"+StringUtil.getUrlArtId(aHref2));
                page.addTargetRequest(aHref1);
                page.addTargetRequest(aHref2);
            }
            if(sxUtils.size()>=2){
                sxDTO.setImgTitle1(sxUtils.get(0).getSxArtTitle()).setImgTitle2(sxUtils.get(1).getSxArtTitle());
                sxUtils.remove(0);
                sxUtils.remove(0);
            }
            sxDTO.setCode(StringUtil.getCodeSwitch(sxTypeName.trim()));
            sxDTO.setList(sxUtils);
            sxDTOS.add(sxDTO);
            // 放入自定义管道 2
        }
        if (sxDTOS.size()>0) {
            page.putField("sxDTOS",sxDTOS);
        }
    }

    private void crawlIndex_1(Page page) {
        List<String> mainTitle = page.getHtml().css("div[class='slider-wrapper'] a p", "text").all();
        List<String> mainUrl = page.getHtml().xpath("div[@class='slider-wrapper']/a/img/@src").all();
        List<String> allSrc = page.getHtml().xpath("div[@class='slider-wrapper']/a/@href").all();
        List<SxIndexRoll> list= Lists.newArrayList();
        if(mainTitle.size()>0 && mainUrl.size()>0 &&allSrc.size()>0){
            for (int i1 = 0; i1 < mainTitle.size(); i1++) {
                SxIndexRoll sr = new SxIndexRoll();
                sr.setSrcTitle(mainTitle.get(i1));
                sr.setSrcUrl(mainUrl.get(i1));
                //todo  ===》   创建url循环进行循环抓取
                page.addTargetRequest(allSrc.get(i1));
                sr.setArtCode(StringUtil.getUrlArtId(allSrc.get(i1)));
                list.add(sr);
            }
            // 放入自定义管道 1
            page.putField("entity-sxindex", JSON.toJSONString(list));
        }
    }


    public Integer geTypeSwitch(String str){
        int code = 1;
        switch(str){
            case "xingge" :
                xingge ++;
                code = xingge;
                break;
            case "zonghe" :
                zonghe ++;
                code = zonghe;
                break;
            case "aiqing" :
                aiqing ++;
                code = aiqing;
                break;
            case "jieshuo":
                jieshuo ++;
                code = jieshuo;
                break;
            default : //可选
                //语句
        }
        return code;
    }



    @Override
    public Site getSite() {
        return site;
    }
}
