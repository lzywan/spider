package com.ziroom.minsu.spider.proxyip.processor;

import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
 *
 * <p>ProxyIpPipelineService</p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * 
 * @author lusp
 * @since 1.0
 * @version 1.0
 */
@Service
public class ProxyIpPipelineService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyIpPipelineService.class);

	private static final String airbnbUrl = "https://www.airbnbchina.cn/";

	@Autowired
	private NetProxyIpPortMapper netProxyIpPortMapper;

	/**
	 * @description: 启用多线程去判断代理ip是否可用，如果可用进行持久化
	 * @author: lusp
	 * @date: 2017/8/22 13:53
	 * @params: resultItems
	 * @return:
	 */
	@Async
	public void asyncCheckProxyAndSave(ResultItems resultItems){
		List<NetProxyIpPort> netIps = resultItems.get("netIps");
		try {
			for (NetProxyIpPort netIp : netIps) {
				if (checkProxyIp(airbnbUrl,netIp.getProxyIp(), netIp.getProxyPort())) {
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

	/**
	 * @description: 判断代理ip 是否可用
	 * @author: lusp
	 * @date: 2017/8/22 13:51
	 * @params: URL,ip,port
	 * @return:
	 */
	public static boolean checkProxyIp(String url, String ip, int port) {
		boolean flag = false;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		RequestConfig.Builder builder = RequestConfig.custom()
				.setSocketTimeout(5000)
				.setConnectTimeout(5000)
				.setMaxRedirects(50)
				.setConnectionRequestTimeout(5000);
		HttpHost httpHost = new HttpHost(ip,port);
		builder.setProxy(httpHost);
		httpGet.setConfig(builder.build());
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			if(status>=200&&status<=300){
				flag = true;
			}
		} catch (IOException e) {
			LOGGER.info("该代理ip地址不可用,ip:{},port:{}",ip,port);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	//测试多线程
	@Async
	public void doTask(int i) {
		LOGGER.info("Task"+i+" started.");
	}

}
