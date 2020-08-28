package com.aaa.springbootwebmagic.job;

import com.aaa.springbootwebmagic.domain.ArtTypeUtil;
import com.aaa.springbootwebmagic.util.MySimHash;
import com.aaa.springbootwebmagic.util.StringUtil;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.List;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/21
 */
public class CommonProcessor {

    private static String str = "（第一星座网原创文章，转载请联系网站管理人员，否则视为侵权。）";

    public static void crawlIndex_4(Page page,int type) {
        //区别标题和文章详情
        List<String> p = page.getHtml().css("div[class='art_con_left']").all();
        if(p.size()>0){
            // 截取 url 的 数字作为id eg：https://www.d1xz.net/sx/zonghe/art361019.aspx ==》 获取 361019
            String artId = page.getUrl().regex("(?<=art).*(?=\\.)").get();
            List<ArtTypeUtil> artTypeUtils = Lists.newArrayList();
            String code = Jsoup.parse(page.getHtml().css("div[class='cur_postion w960']").get()).select("span").last().text();
            ArtTypeUtil artTypeUtil = new ArtTypeUtil();
            if(type==1){
                artTypeUtil.setSxTypeCode(StringUtil.getSxType(code.substring(1, 2)).split(",")[0]);
            }else if(type==2){
                artTypeUtil.setSxTypeCode(StringUtil.getCodeSwitch(code)).setArtCode(artId);
            }
            artTypeUtil.setArtCode(artId);
            artTypeUtil.setHref(page.getUrl().toString());
            artTypeUtil.setTitle(Jsoup.parse(p.get(0)).select(".art_detail_title").text());
            artTypeUtil.setDetailHtml(p.get(0).replace(str, ""));
            artTypeUtils.add(artTypeUtil);
            if (artTypeUtils.size()>0) {
                page.putField("artTypeUtils",artTypeUtils);
            }
            // TODO:  待入库2
        }
    }
    public static double simHash(String str1, String str2) {
        MySimHash hash1 = new MySimHash(str1, 64);
        MySimHash hash2= new MySimHash(str2, 64);
        return hash1.getSemblance(hash2);
    }
}
