package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.config.HttpClientDownloader;
import com.aaa.springbootwebmagic.domain.*;
import com.aaa.springbootwebmagic.domain.entity.SxIndex;
import com.aaa.springbootwebmagic.mapper.SxIndexMapper;
import com.alibaba.fastjson.JSON;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Site site= Site.me().setRetryTimes(5).setSleepTime(100).setTimeOut(5000).setCharset("utf-8")
            .setCycleRetryTimes(2);

    private int i = 0;

    public static  int PAGE_INT = 1,xingge = 1,zonghe =1,aiqing =1 ,jieshuo=1;

    @Autowired
    private SxPipeline sxPipeline;

    /**
     *    fixedDelay 每隔几秒执行一次
     *    initialDelay 启动后执行
     * @return void
     */
    public  void main() {
        Spider.create(new ChineseZodiacV2Processor()).setDownloader(new HttpClientDownloader())
                .addUrl("https://www.d1xz.net/sx/")
                .addPipeline(this.sxPipeline)
                .thread(10).run();
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
        crawlIndex_4(page);
        System.out.println("总共发起url = " + ++i);

    }

    private void crawlIndex_4(Page page) {
        //区别标题和文章详情
        List<String> p = page.getHtml().css("div[class='art_con_left']").all();
        if(p.size()>0){
            // 截取 url 的 数字作为id eg：https://www.d1xz.net/sx/zonghe/art361019.aspx ==》 获取 361019
            String artId = page.getUrl().regex("(?<=art).*(?=\\.)").get();
            List<ArtTypeUtil> artTypeUtils = Lists.newArrayList();
            String code = Jsoup.parse(page.getHtml().css("div[class='cur_postion w960']").get()).select("span").last().text();
            ArtTypeUtil artTypeUtil = new ArtTypeUtil();
            artTypeUtil.setSxTypeCode(getCodeSwitch(code)).setArtCode(artId);
            artTypeUtil.setHref(page.getUrl().toString());
            artTypeUtil.setTitle(Jsoup.parse(p.get(0)).select(".art_detail_title").text());
            artTypeUtil.setDetailHtml(p.get(0));
            artTypeUtils.add(artTypeUtil);
            if (artTypeUtils.size()>0) {
                page.putField("artTypeUtils",artTypeUtils);
            }
            // TODO:  待入库2
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
                sxTypeListDTO.setArtCode(getUrlArtId(url));
                //todo  ===》   创建url循环进行循环抓取 文章详情
                page.addTargetRequest(url);
                sxTypeListDTO.setSxTypeCode(getCodeSwitch(type));
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
                sxUtil.setSxArtTitle(element.text()).setSxArtHref(element.attr("href"));
                sxUtils.add(sxUtil);
            }
            Elements select1 = Jsoup.parse(s).select("ul[class='pic_ui fl'] li img");
            String href = Jsoup.parse(s).select("ul[class='pic_ui fl'] li a").attr("href");
            if (StringUtils.isNotBlank(href)){
                page.addTargetRequest(href);
            }
            if(select1.size()>=2){
                sxDTO.setImgSrc1(select1.get(0).attr("src")).setImgSrc2(select1.get(1).attr("src"));
            }
            if(sxUtils.size()>=2){
                sxDTO.setImgTitle1(sxUtils.get(0).getSxArtTitle()).setImgTitle2(sxUtils.get(1).getSxArtTitle());
                sxUtils.remove(0);
                sxUtils.remove(0);
            }
            sxDTO.setCode(getCodeSwitch(sxTypeName.trim()));
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
                sr.setArtCode(getUrlArtId(allSrc.get(i1)));
                list.add(sr);
            }
            // 放入自定义管道 1
            page.putField("entity-sxindex", JSON.toJSONString(list));
        }
    }

    public  int getUrlArtId(String args) {
        String str = args;
        String reg = "(?<=art).*(?=\\.)";//定义正则表达式

        Pattern patten = Pattern.compile(reg);//编译正则表达式
        Matcher matcher = patten.matcher(str);// 指定要匹配的字符串

        List<String> matchStrs = new ArrayList<>();

        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            matchStrs.add(matcher.group());//获取当前匹配的值
        }
        int result = 0;
        for (int i = 0; i < matchStrs.size(); i++) {
            result = Integer.parseInt(matchStrs.get(i));
        }
        return result;
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

    public Integer getCodeSwitch(String str){
        Integer code = 0;
        switch(str){
            case "生肖运势" :
                code = 1;
                break;
            case "生肖性格" :
                code = 2;
                break;
            case "生肖爱情" :
                code = 3;
                break;
            case "生肖解说":
                code = 4;
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
