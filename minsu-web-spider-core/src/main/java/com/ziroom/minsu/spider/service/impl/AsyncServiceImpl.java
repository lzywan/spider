package com.ziroom.minsu.spider.service.impl;

import com.ziroom.minsu.spider.config.mq.RabbitMqConfiguration;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

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
        return new AsyncResult<>(result);
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

        if(Check.NuNCollection(ipList)){
            LOGGER.error("无可用ip");
            return;
        }

        //异步获取数据 重试3次
        List<CalendarDataVo> calendarDataVos = httpGetData(houseRelateDto.getCalendarUrl(), ipList, 3);
        if (Check.NuNCollection(calendarDataVos)){
            LOGGER.error("请求日历没有结果或异常");
            return;
        }
        // 保存ab原始数据
        int count = abHouseStatusService.saveUpdateAbHouse(calendarDataVos, houseRelateDto.getAbSn());
        LOGGER.info("保存ab原始数据数量={},houseRelateDto={}", count, houseRelateDto);

        // 整理数据 mq通知order加同步锁
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
        calendarTimeDataVo.setCalendarDataVos(timeList);

        Result result = new Result();
        result.setData(calendarTimeDataVo);
        rabbitMqSender.send(RabbitMqConfiguration.lockMqName, result);
        LOGGER.info("发送日历mq数据={}", result);
    }
    
    /**
     * 
     * 检测日历url是否有效
     * 
     * @author zhangyl2
     * @created 2017年10月20日 18:59
     * @param 
     * @return 
     */
    @Override
    public boolean checkCalendarUrlAvailable(String calendarUrl, List<String> ipList){
        List<CalendarDataVo> calendarDataVos = httpGetData(calendarUrl, ipList, 3);
        if(!Check.NuNCollection(calendarDataVos)){
            return true;
        }
        return false;
    }

    /**
     * 获取http请求数据
     * @param url
     * @param ipList
     * @param tryNum
     * @return
     */
    private List<CalendarDataVo> httpGetData(String url, List<String> ipList, int tryNum) {
        String result = null;
        for (int i = 0; i < tryNum; i++) {
            String randomIp = CalendarDataUtil.getRandomIp(ipList);
            String[] iparr = randomIp.split(":");
            String ip = iparr[0];
            int port = Integer.parseInt(iparr[1]);
            //更新操作
            netProxyIpPortMapper.updateProxyIpUseCount(ip, port);

            try {
                result = HttpClientUtil.sendProxyGet(url, new HashMap<>(), ip, port);
                if (result.indexOf("html") > 0) {
                    //代理ip不可用，换一个ip
                    ipList.remove(randomIp);
                }
            } catch (IOException e) {
                //连接超时等原因，换一个ip
                ipList.remove(randomIp);
            }
        }
        List<CalendarDataVo> calendarDataVoList = null;
        try {
            if (!Check.NuNStr(result)) {
                calendarDataVoList = CalendarDataUtil.transStreamToListVo(new ByteArrayInputStream(result.getBytes()));
            }
        } catch (Exception e) {
            LOGGER.info("获取日历失败：" + e.getMessage() + "e={}", e);
            return null;
        }

        return calendarDataVoList;
    }
}
