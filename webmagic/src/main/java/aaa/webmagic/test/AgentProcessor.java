package aaa.webmagic.test;

import aaa.webmagic.config.HttpClientDownloader;
import aaa.webmagic.model.JobDTO;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * description:
 * 可查询IP  http://ip.chinaz.com/getip.aspx
 *
 * 免费的代理服务器 ：
 *   米扑代理
 *   https://proxy.mimvp.com/freeopen
 *   西刺免费代理
 *   https://www.xicidaili.com/wn/
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/6
 */
public class AgentProcessor implements PageProcessor {
    private Site site = Site.me();

    public static void main(String[] args) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
                new Proxy("113.109.213.63",8118	)
                ));
        Spider.create(new AgentProcessor()).
                setDownloader(httpClientDownloader)
                .addUrl("http://resource.boxuegu.com/booklist/find.html").
                run();
    }

    @Override
    public void process(Page page) {
        JobDTO jobDTO = new JobDTO();
    }

    @Override
    public Site getSite() {
        return site;
    }
}
