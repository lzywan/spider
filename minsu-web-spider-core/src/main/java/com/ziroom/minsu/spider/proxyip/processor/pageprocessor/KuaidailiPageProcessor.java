package com.ziroom.minsu.spider.proxyip.processor.pageprocessor;


import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.enums.ProxyTypeEnum;
import com.ziroom.minsu.spider.domain.enums.ProxyipSiteEnum;
import com.ziroom.minsu.spider.proxyip.processor.SimpleHttpClientDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.ProxyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * <p>快代理爬虫逻辑</p>
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
public class KuaidailiPageProcessor implements PageProcessor {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	// 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	private Site site = Site.me().setCharset("utf-8").setRetryTimes(3).setCycleRetryTimes(3).setSleepTime(1000).setUseGzip(true).setTimeOut(10000).setRetrySleepTime(2000);
	
	private static final ProxyipSiteEnum proxyipSiteEnum = ProxyipSiteEnum.KUAI_DAILI;
	
	private static final String IP = "IP";
	
	private static final String PORT = "PORT";
	
	private static final String PROXYTYPE = "类型";

    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {

		LOGGER.info("开始抽取页面，网站={}，当前页面={}", proxyipSiteEnum.getSiteName(), page.getUrl());
    	
        List<Selectable> trs = page.getHtml().xpath("//div[@id='list']/table/tbody/tr").nodes();
        
        List<NetProxyIpPort> netProxyIpPorts = new ArrayList<>();
        NetProxyIpPort netProxyIpPort;
        
        // 是否继续抓取其他列表页
        boolean continueSpiderOther = true;
        
        // 遍历ip行数据
		outer: for (Selectable tr : trs) {
			try {
				netProxyIpPort = new NetProxyIpPort();
				netProxyIpPort.setIpSource(proxyipSiteEnum.getUrl());
				netProxyIpPort.setIsValid(1);
				netProxyIpPort.setValidUsedCount(0);
				netProxyIpPort.setIsDel(0);

				List<Selectable> tds = tr.xpath("//td").nodes();

				// 遍历ip属性
				for (Selectable td : tds) {
					String title = td.$("td", "data-title").get();
					String value = td.$("td", "text").get();

					if (IP.equals(title)) {
						// ip
						netProxyIpPort.setProxyIp(value);
					} else if (PORT.equals(title)) {
						// 端口
						netProxyIpPort.setProxyPort(Integer.parseInt(value));
					} else if (PROXYTYPE.equals(title)) {
						// 代理类型
						if (ProxyTypeEnum.HTTP.getType().equalsIgnoreCase(value)) {
							netProxyIpPort.setProxyType(ProxyTypeEnum.HTTP.getCode());
						} else if (ProxyTypeEnum.HTTPS.getType().equalsIgnoreCase(value)) {
							netProxyIpPort.setProxyType(ProxyTypeEnum.HTTPS.getCode());
						} else {
							// 其他类型则忽略当前行
							continue outer;
						}
					}
				}

				if (!Check.NuNStr(netProxyIpPort.getProxyIp())
						&& !Check.NuNObj(netProxyIpPort.getProxyPort())
						&& !Check.NuNObj(netProxyIpPort.getProxyType())) {
					netProxyIpPorts.add(netProxyIpPort);
				} else {
					LOGGER.error("页面结构变化无法抓取，请及时修改抓取规则，网站={}，当前页面={}", proxyipSiteEnum.getSiteName(), page.getUrl());
					continueSpiderOther = false;
					break outer;
				}
			} catch (Exception e) {
				LOGGER.info( "抽取页面失败，网站={}，当前页面={}，e={}", proxyipSiteEnum.getSiteName(), page.getUrl(), e);
				continue;
			}
		}
        
        if(Check.NuNCollection(netProxyIpPorts)){

			LOGGER.info("无数据跳过页面，网站={}，当前页面={}", proxyipSiteEnum.getSiteName(), page.getUrl());
        	
        	page.setSkip(true);
        }else{

			LOGGER.info("抽取页面完成，网站={}，当前页面={}，ip数量={}", proxyipSiteEnum.getSiteName(), page.getUrl(), netProxyIpPorts.size());
        	
        	page.putField("siteName", proxyipSiteEnum.getSiteName());
        	page.putField("url", page.getUrl());
        	page.putField("netIps", netProxyIpPorts);
        }
		
		if (continueSpiderOther) {
			page.addTargetRequests(page.getHtml().xpath("//div[@id=\"listnav\"]").links().regex("http://www\\.kuaidaili\\.com/free/inha/\\d+/").all());
		}
		
    }

    @Override
    public Site getSite() {
        return site;
    }
    
    public static void main(String[] args) {
    	
    	List<Boolean> list = new ArrayList<>();
    	
    	List<Proxy> proxysList = new ArrayList<>();
		
		Proxy proxy = new Proxy("113.140.25.4", 81, "", "");
		list.add(ProxyUtils.validateProxy(proxy));
		proxysList.add(proxy);
		
		proxy = new Proxy("43.240.138.31", 8080, "", "");
		list.add(ProxyUtils.validateProxy(proxy));
		proxysList.add(proxy);
		
		proxy = new Proxy("114.239.149.230", 808, "", "");
		list.add(ProxyUtils.validateProxy(proxy));
		proxysList.add(proxy);
		
		proxy = new Proxy("115.203.64.9", 808, "", "");
		list.add(ProxyUtils.validateProxy(proxy));
		proxysList.add(proxy);

		System.out.println(list);
		
		// 配置WebMagic代理
		SimpleHttpClientDownloader simpleHttpClientDownloader = new SimpleHttpClientDownloader();

		simpleHttpClientDownloader.setProxyProvider(new SimpleProxyProvider(Collections.unmodifiableList(proxysList)));
		
    	// Spider开始爬取
		Spider.create(new KuaidailiPageProcessor())
			.addUrl(proxyipSiteEnum.getUrl())
			.setDownloader(simpleHttpClientDownloader)
			.thread(1)
			.run();
	}

}