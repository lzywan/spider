package com.ziroom.minsu.spider.web;



import com.ziroom.minsu.spider.service.ProxyIpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <p>爬取代理ip接口</p>
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
@RestController
@RequestMapping("proxyip")
public class ProxyipController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyipController.class);
	
	@Autowired
    private ProxyIpService proxyipService;
	
	/**
	 * 
	 * 爬取代理ip
	 *
	 * @author zhangyl
	 * @created 2017年7月5日 下午5:23:13
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("spider")
	public void kuaidaili(HttpServletRequest request, HttpServletResponse response) {
		// 开始爬取任务
		LOGGER.info("ProxyipService爬取代理ip接口响应成功！");
		proxyipService.runAsyncSpider();
	}

}
