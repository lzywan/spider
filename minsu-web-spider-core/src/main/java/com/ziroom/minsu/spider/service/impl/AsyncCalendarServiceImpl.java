package com.ziroom.minsu.spider.service.impl;

import com.ziroom.minsu.spider.config.mq.RabbitMqConfiguration;
import com.ziroom.minsu.spider.config.mq.RabbitMqSender;
import com.ziroom.minsu.spider.core.result.Result;
import com.ziroom.minsu.spider.core.utils.CalendarDataUtil;
import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;
import com.ziroom.minsu.spider.domain.vo.CalendarTimeDataVo;
import com.ziroom.minsu.spider.domain.vo.TimeDataVo;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import com.ziroom.minsu.spider.service.AbHouseStatusService;
import com.ziroom.minsu.spider.service.AsyncCalendarService;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

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
public class AsyncCalendarServiceImpl implements AsyncCalendarService {

    private static Logger LOGGER = LoggerFactory.getLogger(AsyncCalendarServiceImpl.class);

    private static final String logPreStr = "AsyncCalendarServiceImpl-";

    @Autowired
    private NetProxyIpPortMapper netProxyIpPortMapper;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    @Autowired
    private AbHouseStatusService abHouseStatusService;

    /**
     * 异步任务执行 更新日历 和发送Mq数据
     *
     * @param
     * @return
     * @author jixd
     * @created 2017年08月25日 11:27:06
     */
    @Async
    @Override
    public void asyncHouseCalendarList(List<HouseRelateDto> houseRelateDtoList) {

        // 本页数据的失败重试池
        List<HouseRelateDto> asyncRetryPool = new ArrayList<>();

        // 一页房源共用一波代理ip
        List<NetProxyIpPort> ipList = netProxyIpPortMapper.listNetProxyIp();

        for (HouseRelateDto houseRelateDto : houseRelateDtoList) {
            if (!syncHouseCalendar(houseRelateDto, ipList)) {
                // 进入失败重试池
                asyncRetryPool.add(houseRelateDto);
                LOGGER.info(logPreStr + "[进入失败重试池]当前房源={}", houseRelateDto);
            }
        }

        // 失败重试
        if (!Check.NuNCollection(asyncRetryPool)) {
            LOGGER.info(logPreStr + "[失败重试池]数量={}", asyncRetryPool.size());

            ListIterator<HouseRelateDto> iterator = asyncRetryPool.listIterator();
            while(iterator.hasNext()){
                if(syncHouseCalendar(iterator.next(), ipList)){
                    iterator.remove();
                }
            }

            LOGGER.error(logPreStr + "[最终同步失败]数量={}，房源={}", asyncRetryPool.size(), asyncRetryPool);
        }
    }

    /**
     * 
     * 同步单个日历，返回同步结果
     * 
     * @author zhangyl2
     * @created 2017年11月16日 15:02
     * @param 
     * @return 
     */
    @Override
    public boolean syncHouseCalendar(HouseRelateDto houseRelateDto) {
        List<NetProxyIpPort> ipList = netProxyIpPortMapper.listNetProxyIp();
        return syncHouseCalendar(houseRelateDto, ipList);
    }

    /**
     * 
     * 同步单个日历，返回同步结果
     * 
     * @author zhangyl2
     * @created 2017年11月16日 15:01
     * @param 
     * @return 
     */
    private boolean syncHouseCalendar(HouseRelateDto houseRelateDto, List<NetProxyIpPort> ipList) {

        // 读取日历
        List<CalendarDataVo> calendarDataVos = httpGetData(houseRelateDto, ipList);

        if (Check.NuNCollection(calendarDataVos)) {
            return false;
        }

        // 保存ab原始数据
        int count = abHouseStatusService.saveUpdateAbHouse(calendarDataVos, houseRelateDto.getAbSn());

        LOGGER.info(logPreStr + "[保存ab日历数据]数量={},当前房源={}", count, houseRelateDto);

        // 整理数据 mq通知order加同步锁
        CalendarTimeDataVo calendarTimeDataVo = new CalendarTimeDataVo();
        calendarTimeDataVo.setHouseFid(houseRelateDto.getHouseFid());
        calendarTimeDataVo.setRoomFid(houseRelateDto.getRoomFid());
        calendarTimeDataVo.setRentWay(houseRelateDto.getRentWay());
        List<TimeDataVo> timeList = new ArrayList<>();
        for (CalendarDataVo dataVo : calendarDataVos) {
            TimeDataVo timeDataVo = new TimeDataVo();
            timeDataVo.setStartDate(dataVo.getStartDate());
            timeDataVo.setEndDate(dataVo.getEndDate());
            timeList.add(timeDataVo);
        }
        calendarTimeDataVo.setCalendarDataVos(timeList);

        Result result = new Result().setData(calendarTimeDataVo);
        rabbitMqSender.send(RabbitMqConfiguration.lockMqName, result);

        LOGGER.info(logPreStr + "[发送日历mq数据]当前房源={}", result);

        return true;
    }

    /**
     * 检测日历url是否有效
     *
     * @param
     * @return
     * @author zhangyl2
     * @created 2017年10月20日 18:59
     */
    @Override
    public boolean checkCalendarUrlAvailable(HouseRelateDto houseRelateDto) {
        List<NetProxyIpPort> ipList = netProxyIpPortMapper.listNetProxyIp();
        List<CalendarDataVo> calendarDataVos = httpGetData(houseRelateDto, ipList);
        if (!Check.NuNCollection(calendarDataVos)) {
            return true;
        }
        return false;
    }

    /**
     * 获取http请求数据
     *
     * @param
     * @return
     * @author zhangyl2
     * @created 2017年11月16日 10:56
     */
    private List<CalendarDataVo> httpGetData(HouseRelateDto houseRelateDto, List<NetProxyIpPort> ipList) {
        int tryNum = 3;
        String result = null;

        if (ipList == null) {
            ipList = new ArrayList<>();
        }

        for (int i = 0; i < tryNum; i++) {

            // 代理ip消耗完及时补充
            if (Check.NuNCollection(ipList)) {
                ipList.addAll(netProxyIpPortMapper.listNetProxyIp());
            }

            if (!Check.NuNCollection(ipList)) {
                NetProxyIpPort ip = ipList.get(RandomUtils.nextInt(0, ipList.size()));
                //更新操作
                netProxyIpPortMapper.updateProxyIpUseCount(ip.getProxyIp(), ip.getProxyPort());

                try {
                    result = HttpClientUtil.sendProxyGet(houseRelateDto.getCalendarUrl(), new HashMap<>(), ip.getProxyIp(), ip.getProxyPort());
                    if (result.indexOf("html") > 0) {
                        //代理ip不可用，换一个ip
                        ipList.remove(ip);
                    } else if (!Check.NuNStr(result)) {
                        break;
                    }
                } catch (Exception e) {
                    //连接超时等原因，换一个ip
                    ipList.remove(ip);
                }
            } else {
                LOGGER.error(logPreStr + "[读取日历数据]系统无可用ip！当前房源={}", houseRelateDto);
                return null;
            }
        }

        List<CalendarDataVo> calendarDataVoList = null;

        if (!Check.NuNStr(result)) {
            try {
                calendarDataVoList = CalendarDataUtil.transStreamToListVo(new ByteArrayInputStream(result.getBytes()));
            } catch (Exception e) {
                LOGGER.info(logPreStr + "[读取日历数据]解析异常！e={}，当前房源={}", houseRelateDto);
            }
        } else {
            LOGGER.info(logPreStr + "[读取日历数据]日历地址数据为空！当前房源={}", houseRelateDto);
        }

        return calendarDataVoList;
    }

}
