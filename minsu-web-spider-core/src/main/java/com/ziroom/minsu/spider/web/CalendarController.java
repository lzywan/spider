package com.ziroom.minsu.spider.web;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.minsu.spider.core.result.Result;
import com.ziroom.minsu.spider.core.result.ResultCode;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.service.AsyncCalendarService;
import com.ziroom.minsu.spider.task.SyncCalendarTasker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@Controller
@RequestMapping("/calendar")
public class CalendarController {

    private static Logger LOGGER = LoggerFactory.getLogger(CalendarController.class);

    @Autowired
    private AsyncCalendarService asyncCalendarService;

    @Autowired
    private SyncCalendarTasker syncCalendarTasker;

    /**
     * 同步单个日历
     * @author jixd
     * @created 2017年08月22日 15:34:26
     * @param
     * @return
     */
    @RequestMapping(value = "/syncSingleHouse", method = RequestMethod.POST)
    @ResponseBody
    public Result syncSingleHouse(HouseRelateDto houseRelateDto){
        LOGGER.info("【syncSingleHouse】入参={}", JSONObject.toJSONString(houseRelateDto));
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
        if (StringUtils.isEmpty(houseRelateDto.getAbSn())){
            return result.setStatus(ResultCode.FAIL).setMessage("abSn为空");
        }

        //开始读取日历数据
        return result.setData(asyncCalendarService.syncHouseCalendar(houseRelateDto));
    }

    /**
     * 
     * 检查日历url是否有效
     * 
     * @author zhangyl2
     * @created 2017年10月20日 19:05
     * @param 
     * @return 
     */
    @RequestMapping(value = "/checkCalendarUrlAvailable", method = RequestMethod.POST)
    @ResponseBody
    public Result checkCalendarUrlAvailable(HouseRelateDto houseRelateDto){
        return new Result().setData(asyncCalendarService.checkCalendarUrlAvailable(houseRelateDto));
    }

    /**
     * 
     * 同步所有房源日历
     * 
     * @author zhangyl2
     * @created 2017年11月16日 17:02
     * @param 
     * @return 
     */
    @RequestMapping(value = "/syncAllHouse")
    public void syncAllHouse(HttpServletRequest request, HttpServletResponse response){
        LOGGER.info("SyncCalendarTasker[同步房源日历接口]响应成功！");
        syncCalendarTasker.runAsyncCalendar();
    }

}
