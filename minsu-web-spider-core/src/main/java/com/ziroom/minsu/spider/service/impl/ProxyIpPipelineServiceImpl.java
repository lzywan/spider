package com.ziroom.minsu.spider.service.impl;

import com.ziroom.minsu.spider.config.db.ReadOnly;
import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.core.utils.UUIDGenerator;
import com.ziroom.minsu.spider.core.utils.ValueUtil;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.constant.HttpConstant;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import com.ziroom.minsu.spider.service.ProxyIpPipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;

import java.util.ArrayList;
import java.util.List;

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
@EnableAsync
public class ProxyIpPipelineServiceImpl implements ProxyIpPipelineService{

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyIpPipelineServiceImpl.class);

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
	@Transactional
	@Override
	public void asyncCheckProxyAndSave(ResultItems resultItems){
		LOGGER.info("【asyncCheckProxyAndSave】代理ip持久化开始,resultItems:{}",resultItems);
		try {
			List<NetProxyIpPort> netProxyIpPorts = resultItems.get("netIps");
			for (NetProxyIpPort netProxyIpPort : netProxyIpPorts) {
//				Future<Boolean> future = asyncService.checkProxyIp(HttpConstant.airbnbUrl,netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
//				long start = System.currentTimeMillis();
//				while (true){
//					long end = System.currentTimeMillis();
//					if(future.isDone()){
//						try {
//							Boolean isVailide = future.get();
//							if(isVailide){
//								netProxyIpPort.setFid(UUIDGenerator.hexUUID());
//								netProxyIpPortMapper.saveNetProxyIp(netProxyIpPort);
//							}else{
//								LOGGER.info("【asyncCheckProxyAndSave】Pipeline IP无效！ 网站:{}，ip={},port={}", resultItems.get("siteName"), netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
//							}
//						} catch (InterruptedException e) {
//							LOGGER.error("【asyncCheckProxyAndSave】中断异常：e={}", e);
//						} catch (ExecutionException e) {
//							LOGGER.error("【asyncCheckProxyAndSave】线程异常：e={}", e);
//						}
//						break;
//					}else {
//						if(end-start> HttpConstant.httpclient_tread_time_out){
//							future.cancel(true);
//							LOGGER.info("【asyncService.checkProxyIp】异步线程超时被关闭！Pipeline IP无效！ 网站:{}，ip={},port={}", resultItems.get("siteName"), netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
//							break;
//						}
//					}
//				}

                boolean isVailide = HttpClientUtil.checkProxyIp(HttpConstant.airbnbUrl, netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                if(isVailide){
                    netProxyIpPort.setFid(UUIDGenerator.hexUUID());
                    netProxyIpPortMapper.saveNetProxyIp(netProxyIpPort);
                }else{
                    LOGGER.info("【asyncCheckProxyAndSave】Pipeline IP无效！ 网站:{}，ip={},port={}", resultItems.get("siteName"), netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
                }
			}
			LOGGER.info("【asyncCheckProxyAndSave】Pipeline处理完成，网站={}，当前页面={}，ip数量={}", resultItems.get("siteName"), resultItems.get("url"), netProxyIpPorts.size());
		} catch (Exception e) {
			LOGGER.info("【asyncCheckProxyAndSave】Pipeline process异常！ 网站={}，当前页面={}，ip数量={}，e={}", resultItems.get("siteName"), resultItems.get("url"), e);
		}
	}


	/**
	 * @description: 定时任务扫描数据库中的代理ip,校验是否可用
	 * @author: lusp
	 * @date: 2017/8/23 11:45
	 * @params:
	 * @return:
	 */
    @Scheduled(cron = "33 33 0/6 * * ? ")
	@Async
	@Override
	public void checkProxyIpAvailable() {
		LOGGER.info("【checkProxyIpAvailable】,检验代理ip可用性定时任务启动！");
		try {
			int count = netProxyIpPortMapper.countNetProxyIp();
			int page = ValueUtil.getPage(count,HttpConstant.page_num);
			for(int i=1;i<=page;i++){
				LOGGER.info("【checkProxyIpAvailable】,当前扫描到第 "+i+" 页,共 "+count+" 条数据,每页100条数据...");
				List<NetProxyIpPort> netProxyIpPorts = netProxyIpPortMapper.selectNetProxyIpByPage(i,HttpConstant.page_num);
				if(netProxyIpPorts==null||netProxyIpPorts.size()==0){
					break;
				}
				for(NetProxyIpPort netProxyIpPort:netProxyIpPorts){
//					Future<Boolean> future = asyncService.checkProxyIp(HttpConstant.airbnbUrl,netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
//					long start = System.currentTimeMillis();
//					while (true){
//						long end = System.currentTimeMillis();
//						if(future.isDone()){
//							try {
//								Boolean isVailide = future.get();
//								if(!isVailide){
//									LOGGER.info("【checkProxyIpAvailable】该ip不可用！ip:{},port:{}",netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
//									netProxyIpPort.setIsValid(0);
//									netProxyIpPortMapper.updateByPrimaryKeySelective(netProxyIpPort);
//								}else{
//									LOGGER.info("【checkProxyIpAvailable】该ip可用！ip:{},port:{}",netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
//								}
//							} catch (InterruptedException e) {
//								LOGGER.error("【checkProxyIpAvailable】中断异常：e={}", e);
//							} catch (ExecutionException e) {
//								LOGGER.error("【checkProxyIpAvailable】线程异常：e={}", e);
//							}
//							break;
//						}else {
//							if(end-start>HttpConstant.httpclient_tread_time_out){
//								future.cancel(true);
//								LOGGER.info("【asyncService.checkProxyIp】异步线程超时被关闭！Pipeline IP无效！ip={},port={}", netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
//								netProxyIpPort.setIsValid(0);
//								netProxyIpPortMapper.updateByPrimaryKeySelective(netProxyIpPort);
//								break;
//							}
//						}
//					}

                    boolean isVailide = HttpClientUtil.checkProxyIp(HttpConstant.airbnbUrl, netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                    if(!isVailide){
                        LOGGER.info("【checkProxyIpAvailable】该ip不可用！ip:{},port:{}",netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
                        netProxyIpPort.setIsValid(0);
                        netProxyIpPortMapper.updateByPrimaryKeySelective(netProxyIpPort);
                    }else{
                        LOGGER.info("【checkProxyIpAvailable】该ip可用！ip:{},port:{}",netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
                    }
				}
			}
			LOGGER.info("【checkProxyIpAvailable】,检验代理ip可用性定时任务执行完毕！");
		}catch (Exception e){
			LOGGER.info("【checkProxyIpAvailable】,检验代理ip可用性定时任务异常,e:{}",e);
		}

	}

	//只读
	@ReadOnly
	@Override
	public List<String> listProxyIp() {
		List<NetProxyIpPort> netProxyIpPorts = netProxyIpPortMapper.listNetProxyIp();
		if (Check.NuNCollection(netProxyIpPorts)){
			return null;
		}
		List<String> list = new ArrayList<>();
		for (NetProxyIpPort netProxyIpPort : netProxyIpPorts){
			list.add(netProxyIpPort.getProxyIp()+":"+netProxyIpPort.getProxyPort());
		}
		return list;
	}


	@Override
	public int updateProxyIpUseCount(String ip,int port) {



		return 0;
	}

}
