package com.ziroom.minsu.spider.proxyip.processor;

import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.utils.ProxyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 
 * <p>
 * 代理ip抽取结束后处理
 * </p>
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
public class ProxyipPipeline implements Pipeline {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyipPipeline.class);

	@Autowired
	private NetProxyIpPortMapper netProxyIpPortMapper;

	/**
	 * 处理页面抽取结果
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {

		List<NetProxyIpPort> netIps = resultItems.get("netIps");

		LOGGER.info("Pipeline开始处理，网站={}，当前页面={}，ip数量={}", resultItems.get("siteName"), resultItems.get("url"), netIps.size());

		try {
			for (NetProxyIpPort netIp : netIps) {
				
				if (ProxyUtils.validateProxy(new Proxy(netIp.getProxyIp(), netIp.getProxyPort()))) {
					
					netIp.setFid(UUID.randomUUID().toString());
					
					netProxyIpPortMapper.saveNetProxyIp(netIp);
					
				} else {
					LOGGER.info("Pipeline IP无效！ 网站={}，ip={}", resultItems.get("siteName"), netIp);
				}
			}
			LOGGER.info("Pipeline处理完成，网站={}，当前页面={}，ip数量={}", resultItems.get("siteName"), resultItems.get("url"), netIps.size());
		} catch (Exception e) {
			LOGGER.info("Pipeline process异常！ 网站={}，当前页面={}，ip数量={}，e={}", resultItems.get("siteName"), resultItems.get("url"), netIps.size(), e);
		}

	}


	public static void main(String[] args) {
		List<Boolean> list = new ArrayList<Boolean>();
		
		Proxy proxy = new Proxy("113.140.25.4", 81);
		
		list.add(ProxyUtils.validateProxy(proxy));
		
		proxy = new Proxy("43.240.138.31", 8080);

		list.add(ProxyUtils.validateProxy(proxy));
		
		proxy = new Proxy("114.239.149.230", 808);

		list.add(ProxyUtils.validateProxy(proxy));
		
		proxy = new Proxy("115.203.64.9", 808);

		list.add(ProxyUtils.validateProxy(proxy));

		System.out.println(list);
	}

}
