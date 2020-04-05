package aaa.webmagic.test;

import aaa.webmagic.config.HttpClientDownloader;
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

    public static void main(String[] args) {
        Request request = new Request("http://resource.boxuegu.com/booklist/find.html");
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.json("{'search':java}","utf-8"));
        //使用 post 请求
        Spider.create(new MyJiDongProcessor()).
                setDownloader(new HttpClientDownloader())
                .addRequest(request)
                .setScheduler(
                new QueueScheduler().  //10000000是估计的页面数量
                        setDuplicateRemover(new BloomFilterDuplicateRemover(100000))).
                run();
    }

    @Override
    public void process(Page page) {

        System.out.println(page.getHtml().css("title").all());
        //测试是否过滤重复请求
        page.addTargetRequest("http://resource.boxuegu.com/booklist/find.html");
        page.addTargetRequest("http://resource.boxuegu.com/booklist/find.html");
    }

    @Override
    public Site getSite() {
        return null;
    }
}
