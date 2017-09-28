package com.ziroom.minsu.spider.service.impl;

import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.enums.ProxyipSiteEnum;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import com.ziroom.minsu.spider.service.ProxyIpService;
import com.ziroom.minsu.spider.service.processor.PageProcessorFactory;
import com.ziroom.minsu.spider.service.processor.ProxyipPipeline;
import com.ziroom.minsu.spider.service.processor.SimpleHttpClientDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.utils.ProxyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 
 * <p>代理ipService</p>
 * 
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * 
 * @author zhangyl
 * @since 1.0
 * @version 1.0
 */
@Service
public class ProxyipServiceImpl implements ProxyIpService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyipServiceImpl.class);
	
	@Autowired
	private NetProxyIpPortMapper netProxyIpPortMapper;
	
	@Autowired
    private ProxyipPipeline proxyipPipeline;
	
	private static final String PROXYIP_SPIDER_THREAD_NAME = "PROXYIP_SPIDER_THREAD_NAME-";
	
	private List<Proxy> proxysList = new ArrayList<Proxy>();
	
	private SimpleHttpClientDownloader simpleHttpClientDownloader = new SimpleHttpClientDownloader();
	
	/**
	 * 
	 * 爬虫线程防御性容错
	 *
	 * @author zhangyl
	 * @created 2017年7月10日 下午7:21:29
	 *
	 */
	@Override
	public void runAsyncSpider() {

		boolean started = false;
		Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		if (!Check.NuNObject(threads)) {
			for (Thread th : threads) {
				if (th.getName().startsWith(PROXYIP_SPIDER_THREAD_NAME) && th.isAlive()) {
					started = true;
					break;
				}
			}
		}

		if (started) {
			LOGGER.info("ProxyipService.runAsyncSpider代理ip爬虫已经启动或尚未结束!请勿重复调用！");
		} else {
			try {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						LOGGER.info("ProxyipService.runAsyncSpider代理ip爬虫线程启动中！");
						
						// 设置库里可用的ip做代理池
						// 获取有效代理ip地址列表
						List<NetProxyIpPort> ipList = netProxyIpPortMapper.listNetProxyIp();
						proxysList = new ArrayList<Proxy>();
						
						if (!Check.NuNCollection(ipList)) {
							for (NetProxyIpPort ip : ipList) {
								Proxy proxy = new Proxy(ip.getProxyIp(), ip.getProxyPort(), "", "");
								if (ProxyUtils.validateProxy(proxy)) {
									proxysList.add(proxy);
								}
							}
						}
						
						// 配置WebMagic代理
						simpleHttpClientDownloader = new SimpleHttpClientDownloader();
						if (!Check.NuNCollection(proxysList)) {
							simpleHttpClientDownloader.setProxyProvider(new SimpleProxyProvider(Collections.unmodifiableList(proxysList)));
						}
						
						// 开始串行爬取任务
						startSpiderTasker(ProxyipSiteEnum.KUAI_DAILI);
						startSpiderTasker(ProxyipSiteEnum.IP_181_DAILI);
						startSpiderTasker(ProxyipSiteEnum.XICI_DAILI);
					}
				});

				LOGGER.info("ProxyipService.runAsyncSpider代理ip爬虫线程启动前！");
				thread.setName(PROXYIP_SPIDER_THREAD_NAME);
				thread.start();
			} catch (Exception e) {
				LOGGER.error("ProxyipService.runAsyncSpider代理ip爬虫线程启动异常！ e={}", e);
			}
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

			LOGGER.info("ProxyipService.startSpiderTasker开始爬取：网站={} url={} 代理数量={} 代理={}", siteName, url, proxysList.size(), proxysList);
			
		    // Spider开始爬取
			Spider.create(pageProcessor)
				.addUrl(url)
				.addPipeline(proxyipPipeline)
				.setDownloader(simpleHttpClientDownloader)
				.thread(1)
				.run();
		} catch (Exception e) {
			LOGGER.error("ProxyipService.startSpiderTasker发生异常：网站={} url={} pageProcessor={} exception,e={}", siteName, url, pageProcessor, e);
			return;
		}
	}
	
}
