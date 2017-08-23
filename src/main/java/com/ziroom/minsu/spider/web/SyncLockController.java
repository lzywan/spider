package com.ziroom.minsu.spider.web;

import com.ziroom.minsu.spider.config.mq.RabbitMqSender;
import com.ziroom.minsu.spider.core.result.Result;
import com.ziroom.minsu.spider.core.result.ResultCode;
import com.ziroom.minsu.spider.core.utils.CalendarDataUtil;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;
import com.ziroom.minsu.spider.domain.vo.CalendarTimeDataVo;
import com.ziroom.minsu.spider.domain.vo.TimeDataVo;
import com.ziroom.minsu.spider.service.AbHouseStatusService;
import com.ziroom.minsu.spider.service.ProxyIpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private AbHouseStatusService abHouseStatusService;

    @Autowired
    private ProxyIpService proxyIpService;

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
            return result.setCode(ResultCode.FAIL).setMessage("参数为空");
        }
        if (StringUtils.isEmpty(houseRelateDto.getHouseFid())){
            return result.setCode(ResultCode.FAIL).setMessage("房源fid为空");
        }
        if (houseRelateDto.getRentWay() == null){
            return result.setCode(ResultCode.FAIL).setMessage("出租方式为空");
        }
        if (StringUtils.isEmpty(houseRelateDto.getCalendarUrl())){
            return result.setCode(ResultCode.FAIL).setMessage("日历url为空");
        }
        //开始读取日历数据

        String ip = "60.255.186.169";
        int port = 8888;



        //获取到日历原数据
        List<CalendarDataVo> calendarDataVos = CalendarDataUtil.transStreamToListVo(CalendarDataUtil.getInputStreamByUrl(houseRelateDto.getCalendarUrl(), ip, port));

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
        //清空数据返回 ，把数据放到mq中
        result.setData(null);

        return result;
    }


}
