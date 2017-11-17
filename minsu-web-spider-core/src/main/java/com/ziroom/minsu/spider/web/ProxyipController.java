package com.ziroom.minsu.spider.web;

import com.ziroom.minsu.spider.task.ProxyIpCheckTasker;
import com.ziroom.minsu.spider.task.ProxyipSpiderTasker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>爬取代理ip接口</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author zhangyl
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/proxyip")
public class ProxyipController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyipController.class);

    @Autowired
    private ProxyipSpiderTasker proxyipSpiderTasker;

    @Autowired
    private ProxyIpCheckTasker proxyIpCheckTasker;

    /**
     * 爬取代理ip
     *
     * @param request
     * @param response
     * @return
     * @author zhangyl
     * @created 2017年7月5日 下午5:23:13
     */
    @RequestMapping("/spider")
    public void spider(HttpServletRequest request, HttpServletResponse response) {
        // 开始爬取任务
        LOGGER.info("ProxyipSpiderTasker爬取代理ip接口响应成功！");
        proxyipSpiderTasker.runAsyncSpider();
    }

    /**
     * 
     * 代理ip刷新可用性
     * 
     * @author zhangyl2
     * @created 2017年11月17日 13:46
     * @param 
     * @return 
     */
    @RequestMapping("/freshAvailable")
    public void freshAvailable(HttpServletRequest request, HttpServletResponse response) {
        // 刷新可用性
        LOGGER.info("ProxyIpCheckTasker刷新可用性接口响应成功！");
        proxyIpCheckTasker.runAsyncCheck();
    }

}
