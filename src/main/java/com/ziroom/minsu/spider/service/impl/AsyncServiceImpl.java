package com.ziroom.minsu.spider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.minsu.spider.config.mq.RabbitMqSender;
import com.ziroom.minsu.spider.core.result.Result;
import com.ziroom.minsu.spider.core.utils.CalendarDataUtil;
import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;
import com.ziroom.minsu.spider.domain.vo.CalendarTimeDataVo;
import com.ziroom.minsu.spider.domain.vo.TimeDataVo;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import com.ziroom.minsu.spider.service.AbHouseStatusService;
import com.ziroom.minsu.spider.service.AsyncService;
import com.ziroom.minsu.spider.service.ProxyIpPipelineService;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@Component
public class AsyncServiceImpl implements AsyncService{

    private static Logger LOGGER = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Autowired
    private NetProxyIpPortMapper netProxyIpPortMapper;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private AbHouseStatusService abHouseStatusService;

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

    /**
     * 异步任务执行 更新日历 和发送Mq数据
     * @author jixd
     * @created 2017年08月25日 11:27:06
     * @param
     * @return
     */
    @Async
    public void saveHouseCalendarDateAndSendMq(HouseRelateDto houseRelateDto, List<String> ipList){
        Result result = new Result();
        //异步获取数据 重试3次
        List<CalendarDataVo> calendarDataVos = httpGetData(houseRelateDto.getCalendarUrl(), ipList, 3);
        if (Check.NuNCollection(calendarDataVos)){
            LOGGER.info("请求日历没有结果或异常");
            return;
        }
        LOGGER.info("返回结果={}", JSONObject.toJSONString(calendarDataVos));
        abHouseStatusService.saveUpdateAbHouse(calendarDataVos,houseRelateDto.getAbSn());
        //重新解析数据 放入mq中消费
        CalendarTimeDataVo calendarTimeDataVo = new CalendarTimeDataVo();
        calendarTimeDataVo.setHouseFid(houseRelateDto.getHouseFid());
        calendarTimeDataVo.setRoomFid(houseRelateDto.getRoomFid());
        calendarTimeDataVo.setRentWay(houseRelateDto.getRentWay());
        List<TimeDataVo> timeList = new ArrayList<>();
        for (CalendarDataVo dataVo : calendarDataVos){
            TimeDataVo timeDataVo = new TimeDataVo();
            timeDataVo.setStartDate(dataVo.getStartDate());
            timeDataVo.setEndDate(dataVo.getEndDate());
            timeList.add(timeDataVo);
        }
        calendarTimeDataVo.setDateList(timeList);
        result.setData(calendarTimeDataVo);
        //mq发送
        rabbitMqSender.send(result);
    }

    /**
     * 获取http请求数据
     * @param url
     * @param ipList
     * @param tryNum
     * @return
     */
    private List<CalendarDataVo> httpGetData(String url,List<String> ipList,int tryNum){
        if (tryNum == 0){
            return null;
        }
        String randomIp = CalendarDataUtil.getRandomIp(ipList);
        String[] iparr = randomIp.split(":");
        String ip = iparr[0];
        int port = Integer.parseInt(iparr[1]);
        //更新操作
        netProxyIpPortMapper.updateProxyIpUseCount(ip,port);
        String result = "";
        try {
            result = HttpClientUtil.sendProxyGet(url,new HashMap<>(),ip,port);
            if (result.indexOf("html") >0){
                //代理ip不可用，换一个ip
                ipList.remove(randomIp);
                return httpGetData(url,ipList,--tryNum);
            }
        } catch (ConnectTimeoutException e) {
            //连接超时，换一个ip
            ipList.remove(randomIp);
            return httpGetData(url,ipList,--tryNum);
        } catch (IOException e) {
            ipList.remove(randomIp);
            return httpGetData(url,ipList,--tryNum);
        }
        if (!Check.NuNStr(result)){
            return CalendarDataUtil.transStreamToListVo(new ByteArrayInputStream(result.getBytes()));
        }else{
            return null;
        }
    }
}
