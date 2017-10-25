package com.ziroom.minsu.spider.task;

import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.domain.enums.ProxyipSiteEnum;
import com.ziroom.minsu.spider.service.ProxyIpPipelineService;
import com.ziroom.minsu.spider.service.processor.PageProcessorFactory;
import com.ziroom.minsu.spider.service.processor.ProxyipPipeline;
import com.ziroom.minsu.spider.service.processor.SimpleHttpClientDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>定时任务爬代理ip</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author zhangyl2
 * @version 1.0
 * @Date Created in 2017年10月24日
 * @since 1.0
 */
@Component
public class ProxyipTask {

    private static Logger LOGGER = LoggerFactory.getLogger(ProxyipTask.class);

    private static final String logPreStr = "ProxyipTask-";

    private List<Proxy> proxysList = new ArrayList<>();

    private SimpleHttpClientDownloader simpleHttpClientDownloader = new SimpleHttpClientDownloader();

    @Autowired
    private ProxyIpPipelineService proxyIpPipelineService;

    @Autowired
    private ProxyipPipeline proxyipPipeline;

//    @Scheduled(cron = "11 11 0 * * ?")
    public void syncLockByPage() {
        LOGGER.info(logPreStr + "启动");

        // 设置库里可用的ip做代理池
        // 获取有效代理ip地址列表
        List<String> ipList = proxyIpPipelineService.listProxyIp();
        proxysList = new ArrayList<>();
        if (!Check.NuNCollection(ipList)) {
            for (String ipStr : ipList) {
                String[] ipArr = ipStr.split(":");
                Proxy proxy = new Proxy(ipArr[0], Integer.valueOf(ipArr[1]), "", "");
                proxysList.add(proxy);
            }

            // 配置WebMagic代理
            simpleHttpClientDownloader = new SimpleHttpClientDownloader();
            simpleHttpClientDownloader.setProxyProvider(new SimpleProxyProvider(Collections.unmodifiableList(proxysList)));

            // 开始串行爬取任务
            startSpiderTasker(ProxyipSiteEnum.KUAI_DAILI);
            startSpiderTasker(ProxyipSiteEnum.IP_181_DAILI);
            startSpiderTasker(ProxyipSiteEnum.XICI_DAILI);
        } else {
            LOGGER.error(logPreStr + "无可用ip");
        }


    }

    /**
     *
     * 开始爬取任务
     *
     * @author zhangyl
     * @created 2017年7月5日 下午5:13:30
     *
     * @param proxyipSiteEnum
     */
    public void startSpiderTasker(ProxyipSiteEnum proxyipSiteEnum) {

        String siteName = null;
        String url = null;
        PageProcessor pageProcessor = null;

        try {
            siteName = proxyipSiteEnum.getSiteName();
            url = proxyipSiteEnum.getUrl();
            pageProcessor = PageProcessorFactory.createPageProcessor(proxyipSiteEnum);

            LOGGER.info(logPreStr + "开始爬取：网站={} url={} 代理数量={} 代理={}", siteName, url, proxysList.size(), proxysList);

            // Spider开始爬取
            Spider.create(pageProcessor)
                    .addUrl(url)
                    .addPipeline(proxyipPipeline)
                    .setDownloader(simpleHttpClientDownloader)
                    .thread(1)
                    .run();
        } catch (Exception e) {
            LOGGER.error(logPreStr + "发生异常：网站={} url={} pageProcessor={} exception,e={}", siteName, url, pageProcessor, e);
        }
    }

}
