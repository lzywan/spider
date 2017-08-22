package com.ziroom.minsu.spider.service.impl;

import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import com.ziroom.minsu.spider.service.CheckProxyIpJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * <P>定时任务校验代理ip可用</P>
 * <P>
 * <PRE>
 * <BR> 修改记录
 * <BR>------------------------------------------
 * <BR> 修改日期       修改人        修改内容
 * </PRE>
 *
 * @Author lusp
 * @Date Create in 2017年08月 22日 17:15
 * @Version 1.0
 * @Since 1.0
 */
@Component
public class CheckProxyIpJobServiceImpl implements CheckProxyIpJobService{

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckProxyIpJobServiceImpl.class);

    private static final String airbnbUrl = "https://www.airbnbchina.cn/";

    @Autowired
    private NetProxyIpPortMapper netProxyIpPortMapper;

    @Scheduled(cron="0 0/2 8-20 * * ?")
    @Transactional
    @Override
    public void checkProxyIpAvailable() {

        LOGGER.info("CheckProxyIpJobService.checkProxyIpAvailable(),检验代理ip可用性定时任务启动！");

        boolean flag = true;
        int pageNum = 1;
        while (flag){
            LOGGER.info("CheckProxyIpJobService.checkProxyIpAvailable(),当前扫描到第 "+pageNum+" 页,每页100条数据...");
            List<NetProxyIpPort> netProxyIpPorts = netProxyIpPortMapper.selectNetProxyIpByPage(pageNum,100);
            pageNum ++;
            if(netProxyIpPorts==null||netProxyIpPorts.size()==0){
                flag = false;
                continue;
            }
            for(NetProxyIpPort netProxyIpPort:netProxyIpPorts){
                if (!HttpClientUtil.checkProxyIp(airbnbUrl,netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort())) {
                    netProxyIpPort.setIsValid(0);
                    netProxyIpPortMapper.updateByPrimaryKeySelective(netProxyIpPort);
                }
            }
        }
    }

}
