package com.ziroom.minsu.spider.task;

import com.asura.framework.base.util.Check;
import com.asura.framework.base.util.JsonEntityTransform;
import com.asura.framework.utils.LogUtil;
import com.ziroom.minsu.services.common.sms.base.BaseMessage;
import com.ziroom.minsu.services.common.sms.base.SmsMessage;
import com.ziroom.minsu.services.common.utils.CloseableHttpUtil;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.core.utils.ValueUtil;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.constant.HttpConstant;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

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

    @Value("${sms.send.url}")
    private String smsSendUrl;

    @Value("${sms.token}")
    private String smsToken;

    @Autowired
    private NetProxyIpPortMapper netProxyIpPortMapper;

    /**
     * 电话
     * 张少斌，杨东，张扬乐
     */
    private final String mobile = "15010386533,18701482472,18500866372";

    public void runAsyncCheck() {
        LOGGER.info(logPreStr + "[代理ip检测线程]启动！");
        Thread thread = new Thread(() -> {
            checkProxyIpAvailable();
            LOGGER.info(logPreStr + "[代理ip检测线程]执行结束!");

            Calendar c = Calendar.getInstance();
            if (c.get(Calendar.HOUR_OF_DAY) >= 9 && c.get(Calendar.HOUR_OF_DAY) < 18) {
                List<NetProxyIpPort> list = netProxyIpPortMapper.listNetProxyIp();
                if (list.size() < 50) {
                    SmsMessage smsMessage = new SmsMessage(mobile, "爬虫库里可用代理ip数量：" + list.size());
                    smsMessage.setToken(smsToken);
                    sendMessage(smsMessage);
                }
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
            int count = netProxyIpPortMapper.countCheckNetProxyIp();
            int page = ValueUtil.getPage(count, HttpConstant.page_num);
            for (int i = 1; i <= page; i++) {
                LOGGER.info(logPreStr + "[代理ip检测],当前扫描到第 " + i + " 页,共 " + count + " 条数据,每页100条数据...");
                List<NetProxyIpPort> netProxyIpPorts = netProxyIpPortMapper.selectCheckNetProxyIpByPage(i, HttpConstant.page_num);
                if (netProxyIpPorts == null || netProxyIpPorts.size() == 0) {
                    break;
                }
                for (NetProxyIpPort netProxyIpPort : netProxyIpPorts) {
                    boolean isVailide = HttpClientUtil.checkProxyIp(HttpConstant.airbnbUrl, netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                    int isValid;
                    if (!isVailide) {
                        LOGGER.info(logPreStr + "[代理ip检测]该ip不可用！ip:{},port:{}", netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                        isValid = 0;
                    } else {
                        LOGGER.info(logPreStr + "[代理ip检测]该ip可用！ip:{},port:{}", netProxyIpPort.getProxyIp(), netProxyIpPort.getProxyPort());
                        isValid = 1;
                    }
                    if (isValid != netProxyIpPort.getIsValid()) {
                        netProxyIpPort.setIsValid(isValid);
                        netProxyIpPortMapper.updateByPrimaryKeySelective(netProxyIpPort);
                    }
                }
            }
            LOGGER.info(logPreStr + "[代理ip检测]结束");
        } catch (Exception e) {
            LOGGER.error(logPreStr + "[代理ip检测]异常,e:{}", e);
        }
    }


    /**
     * 发送短信
     *
     * @param
     * @return
     * @author zhangyl2
     * @created 2017年12月13日 16:21
     */
    private void sendMessage(BaseMessage baseMessage) {

        if (Check.NuNObj(baseMessage) || Check.NuNStr(baseMessage.getContent())) {
            return;
        }

        Map<String, String> param = (Map<String, String>) JsonEntityTransform.json2Map(JsonEntityTransform.Object2Json(baseMessage));
        LogUtil.info(LOGGER, "信息发送地址url=[" + smsSendUrl + "]");
        String resData = CloseableHttpUtil.sendPost(smsSendUrl, JsonEntityTransform.Object2Json(param));
        LogUtil.info(LOGGER, "短信接口返回数据resData=[" + resData + "]", resData);

    }
}
