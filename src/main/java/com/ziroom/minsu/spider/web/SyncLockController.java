package com.ziroom.minsu.spider.web;

import com.ziroom.minsu.spider.config.mq.RabbitMqSender;
import com.ziroom.minsu.spider.core.exception.ServiceException;
import com.ziroom.minsu.spider.core.result.Result;
import com.ziroom.minsu.spider.core.result.ResultCode;
import com.ziroom.minsu.spider.core.utils.CalendarDataUtil;
import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;
import com.ziroom.minsu.spider.domain.vo.CalendarTimeDataVo;
import com.ziroom.minsu.spider.domain.vo.TimeDataVo;
import com.ziroom.minsu.spider.service.AbHouseStatusService;
import com.ziroom.minsu.spider.service.ProxyIpPipelineService;
import com.ziroom.minsu.spider.service.ProxyIpService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>同步锁</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 15:27
 * @since 1.0
 */
@RestController("/syncLock")
public class SyncLockController {

    private static Logger LOGGER = LoggerFactory.getLogger(SyncLockController.class);

    @Autowired
    private AbHouseStatusService abHouseStatusService;

    @Autowired
    private ProxyIpPipelineService proxyIpPipelineService;

    @Autowired
    private RabbitMqSender rabbitMqSender;

    /**
     * 同步单个日历
     * @author jixd
     * @created 2017年08月22日 15:34:26
     * @param
     * @return
     */
    @RequestMapping(name = "/syncSingleHouse",method = RequestMethod.POST)
    public Result syncSingleHouse(HouseRelateDto houseRelateDto){
        Result result = new Result();
        if (houseRelateDto == null){
            return result.setStatus(ResultCode.FAIL).setMessage("参数为空");
        }
        if (StringUtils.isEmpty(houseRelateDto.getHouseFid())){
            return result.setStatus(ResultCode.FAIL).setMessage("房源fid为空");
        }
        if (houseRelateDto.getRentWay() == null){
            return result.setStatus(ResultCode.FAIL).setMessage("出租方式为空");
        }
        if (StringUtils.isEmpty(houseRelateDto.getCalendarUrl())){
            return result.setStatus(ResultCode.FAIL).setMessage("日历url为空");
        }
        //开始读取日历数据
        List<String> ipList = proxyIpPipelineService.listProxyIp();
        if (Check.NuNCollection(ipList)){
            return result.setStatus(ResultCode.FAIL).setMessage("无可用ip");
        }


        saveHouseCalendarDateAndSendMq(houseRelateDto, ipList);
        LOGGER.info("返回结果");
        return result;
    }



    public void saveHouseCalendarDateAndSendMq(HouseRelateDto houseRelateDto,List<String> ipList) {
        Result result = new Result();
        List<CalendarDataVo> calendarDataVos = httpGetData(houseRelateDto.getCalendarUrl(), ipList, 3);
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
        LOGGER.info("异步任务执行完成");
        //mq发送
        rabbitMqSender.send(result);
    }


    private List<CalendarDataVo> httpGetData(String url,List<String> ipList,int tryNum){
        if (tryNum == 0){
            return null;
        }
        String randomIp = CalendarDataUtil.getRandomIp(ipList);
        String[] iparr = randomIp.split(":");
        String ip = iparr[0];
        int port = Integer.parseInt(iparr[1]);
        String result = "";
        try {
            result = HttpClientUtil.sendProxyGet(url,new HashMap<>(),ip,port);
        } catch (ConnectTimeoutException e) {
            //连接超时，换一个ip
            ipList.remove(randomIp);
            LOGGER.info("重试ip次数num={}",tryNum);
            return httpGetData(url,ipList,--tryNum);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!Check.NuNStr(result)){
            return CalendarDataUtil.transStreamToListVo(new ByteArrayInputStream(result.getBytes()));
        }else{
            return null;
        }
    }



}
