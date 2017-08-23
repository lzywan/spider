package com.ziroom.minsu.spider.service.impl;

import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;


/*
 * <P></P>
 * <P>
 * <PRE>
 * <BR> 修改记录
 * <BR>------------------------------------------
 * <BR> 修改日期       修改人        修改内容
 * </PRE>
 *
 * @Author lusp
 * @Date Create in 2017年08月 23日 20:00
 * @Version 1.0
 * @Since 1.0
 */
@Service
public class AsyncServiceImpl implements AsyncService{

    /**
     * @description: 异步调用httpclient,防止线程阻塞
     * @author: lusp
     * @date: 2017/8/23 20:05
     * @params: url,ip,port
     * @return:
     */
    @Async
    @Override
    public Future<Boolean> checkProxyIp(String url, String ip, int port){
        Boolean result = HttpClientUtil.checkProxyIp(url,ip,port);
        return new AsyncResult<Boolean>(result);
    }
}
