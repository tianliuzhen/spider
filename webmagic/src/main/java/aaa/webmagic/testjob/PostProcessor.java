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
public class PostProcessor implements PageProcessor {
    private Site site = Site.me();

    public static void main(String[] args) {
        Request request = new Request("http://resource.boxuegu.com/booklist/find.html");
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.json("{'search':java}","utf-8"));
        //使用 post 请求
        Spider.create(new PostProcessor()).
                setDownloader(new HttpClientDownloader())
                .addRequest(request)
                .setScheduler(
                new QueueScheduler().
                        //10000000是估计的页面数量
                        setDuplicateRemover(new BloomFilterDuplicateRemover(100000))).
                run();
    }

    @Override
    public void process(Page page) {
        JobDTO jobDTO = new JobDTO();
        //使用jsOup 进行解析
        //注：这里webMagic 没做封装
        jobDTO.setTitle(page.getHtml().css("title","text").get());

        String str = page.getHtml().css("ul[class=nav navbar-nav navbar-right] li a").all().toString();
        String a = Jsoup.parse(str).select("a").text();
        System.out.println(a);
        jobDTO.setName(a);
        if (jobDTO.getName() == null) {
            //skip this page
            page.setSkip(true);
        } else {
            page.putField("repo", jobDTO);
        }
        JobDTO jobDTO1=   page.getResultItems().get("repo");
        jobDTO1.toString();
    }

    @Override
    public Site getSite() {
        return site;
    }
}
