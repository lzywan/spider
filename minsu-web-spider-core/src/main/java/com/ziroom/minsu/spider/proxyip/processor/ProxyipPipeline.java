package com.ziroom.minsu.spider.proxyip.processor;

import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.core.utils.UUIDGenerator;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.constant.HttpConstant;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;


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
@EnableAsync
public class ProxyipPipeline implements Pipeline {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyipPipeline.class);

	private static final String logPreStr = "ProxyipPipeline-";

    @Autowired
    private NetProxyIpPortMapper netProxyIpPortMapper;

	/**
	 * @description: 异步调用方法判断该ip是否有效(能否访问Airbnb的https网站)，如果有效入库
	 * @author: lusp
	 * @date: 2017/8/22 15:24
	 * @params: resultItems,task
	 * @return:
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<NetProxyIpPort> netIps = resultItems.get("netIps");
		LOGGER.info(logPreStr + "开始处理，网站={}，当前页面={}，ip数量={}", resultItems.get("siteName"), resultItems.get("url"), netIps.size());

        LOGGER.info(logPreStr + "代理ip持久化开始,resultItems:{}",resultItems);
        try {
            List<NetProxyIpPort> netProxyIpPorts = resultItems.get("netIps");
            for (NetProxyIpPort netProxyIpPort : netProxyIpPorts) {
                boolean isVailide = HttpClientUtil.checkProxyIp(HttpConstant.airbnbUrl, netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                if(isVailide){
                    netProxyIpPort.setFid(UUIDGenerator.hexUUID());
                    netProxyIpPortMapper.saveNetProxyIp(netProxyIpPort);
                }else{
                    LOGGER.info(logPreStr + "Pipeline IP无效！ 网站:{}，ip={},port={}", resultItems.get("siteName"), netProxyIpPort.getProxyIp(),netProxyIpPort.getProxyPort());
                }
            }
            LOGGER.info(logPreStr + "处理完成，网站={}，当前页面={}，ip数量={}", resultItems.get("siteName"), resultItems.get("url"), netProxyIpPorts.size());
        } catch (Exception e) {
            LOGGER.error(logPreStr + "process异常！ 网站={}，当前页面={}，ip数量={}，e={}", resultItems.get("siteName"), resultItems.get("url"), e);
        }
	}

}
