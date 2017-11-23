package com.ziroom.minsu.spider.task;

import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.service.RedisService;
import com.ziroom.minsu.spider.core.utils.ValueUtil;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.constant.HttpConstant;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>ProxyIpCheckTasker</p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author zhangyl2
 * @version 1.0
 * @Date Created in 2017年11月16日
 * @since 1.0
 */
@Component
public class ProxyIpCheckTasker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyIpCheckTasker.class);

    private static final String logPreStr = "ProxyIpCheckTasker-";

    private static final String PROXYIP_CHECK_TASKER_THREAD_NAME = "PROXYIP_CHECK_TASKER_THREAD_NAME-";

    @Autowired
    private RedisService redisService;

    @Autowired
    private NetProxyIpPortMapper netProxyIpPortMapper;

    public void runAsyncCheck() {
        Thread thread = new Thread(() -> {
            if (redisService.getDistributedLock(PROXYIP_CHECK_TASKER_THREAD_NAME)) {
                LOGGER.info(logPreStr + "[代理ip检测线程]启动！");
                checkProxyIpAvailable();
                // 删除锁
                redisService.releaseDistributedLock(PROXYIP_CHECK_TASKER_THREAD_NAME);
            } else {
                LOGGER.info(logPreStr + "[代理ip检测线程]已经启动或尚未结束!请勿重复调用！");
            }
        });
        thread.setName(PROXYIP_CHECK_TASKER_THREAD_NAME);
        thread.start();
    }

    /**
     * 扫描数据库中的代理ip,校验是否可用
     *
     * @param
     * @return
     * @author zhangyl2
     * @created 2017年11月16日 17:35
     */
    private void checkProxyIpAvailable() {
        try {
            int count = netProxyIpPortMapper.countNetProxyIp();
            int page = ValueUtil.getPage(count, HttpConstant.page_num);
            for (int i = 1; i <= page; i++) {
                LOGGER.info(logPreStr + "[代理ip检测],当前扫描到第 " + i + " 页,共 " + count + " 条数据,每页100条数据...");
                List<NetProxyIpPort> netProxyIpPorts = netProxyIpPortMapper.selectNetProxyIpByPage(i, HttpConstant.page_num);
                if (netProxyIpPorts == null || netProxyIpPorts.size() == 0) {
                    break;
                }
                for (NetProxyIpPort netProxyIpPort : netProxyIpPorts) {
                    boolean isVailide = HttpClientUtil.checkProxyIp(HttpConstant.airbnbUrl, netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                    if (!isVailide) {
                        LOGGER.info(logPreStr + "[代理ip检测]该ip不可用！ip:{},port:{}", netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                        netProxyIpPort.setIsValid(0);
                        netProxyIpPortMapper.updateByPrimaryKeySelective(netProxyIpPort);
                    } else {
                        LOGGER.info(logPreStr + "[代理ip检测]该ip可用！ip:{},port:{}", netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                    }
                }
            }
            LOGGER.info(logPreStr + "[代理ip检测]结束");
        } catch (Exception e) {
            LOGGER.error(logPreStr + "[代理ip检测]异常,e:{}", e);
        }

    }


}
