package com.ziroom.minsu.spider.task;

import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.enums.ProxyipSiteEnum;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import com.ziroom.minsu.spider.proxyip.processor.PageProcessorFactory;
import com.ziroom.minsu.spider.proxyip.processor.ProxyipPipeline;
import com.ziroom.minsu.spider.proxyip.processor.SimpleHttpClientDownloader;
import com.ziroom.minsu.spider.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>代理ipService</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author zhangyl
 * @version 1.0
 * @since 1.0
 */
@Component
public class ProxyipSpiderTasker {

    private static Logger LOGGER = LoggerFactory.getLogger(ProxyipSpiderTasker.class);

    private static final String logPreStr = "ProxyipSpiderTasker-";

    private static final String PROXYIP_SPIDER_TASKER_THREAD_NAME = "PROXYIP_SPIDER_TASKER_THREAD_NAME-";

    private List<Proxy> proxysList = new ArrayList<>();

    private SimpleHttpClientDownloader simpleHttpClientDownloader = new SimpleHttpClientDownloader();

    @Autowired
    private NetProxyIpPortMapper netProxyIpPortMapper;

    @Autowired
    private ProxyipPipeline proxyipPipeline;

    @Autowired
    private RedisService redisService;

    /**
     * 爬虫线程防御性容错
     *
     * @author zhangyl
     * @created 2017年7月10日 下午7:21:29
     */
    public void runAsyncSpider() {
//        if (redisService.getDistributedLock(PROXYIP_SPIDER_TASKER_THREAD_NAME)) {
            LOGGER.info(logPreStr + "[代理ip爬虫线程]启动！");
            Thread thread = new Thread(() -> {
                // 设置库里可用的ip做代理池
                // 获取有效代理ip地址列表
                List<NetProxyIpPort> ipList = netProxyIpPortMapper.listNetProxyIp();
                proxysList = new ArrayList<>();
                if (!Check.NuNCollection(ipList)) {
                    for (NetProxyIpPort netProxyIpPort : ipList) {
                        Proxy proxy = new Proxy(netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort(), "", "");
                        proxysList.add(proxy);
                    }

                    // 配置WebMagic代理
                    simpleHttpClientDownloader = new SimpleHttpClientDownloader();
                    simpleHttpClientDownloader.setProxyProvider(new SimpleProxyProvider(Collections.unmodifiableList(proxysList)));

                    // 开始串行爬取任务
                    startSpiderTasker(ProxyipSiteEnum.KUAI_DAILI);
                    startSpiderTasker(ProxyipSiteEnum.IP_181_DAILI);
                    startSpiderTasker(ProxyipSiteEnum.XICI_DAILI);

                    // 删除锁
                    redisService.releaseDistributedLock(PROXYIP_SPIDER_TASKER_THREAD_NAME);
                } else {
                    LOGGER.error(logPreStr + "无可用ip");
                }
            });
            thread.setName(PROXYIP_SPIDER_TASKER_THREAD_NAME);
            thread.start();
//        } else {
//            LOGGER.info(logPreStr + "[代理ip爬虫线程]已经启动或尚未结束!请勿重复调用！");
//        }
    }

    /**
     * 开始爬取任务
     *
     * @param proxyipSiteEnum
     * @author zhangyl
     * @created 2017年7月5日 下午5:13:30
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
