package com.ziroom.minsu.spider.web;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.minsu.spider.core.result.Result;
import com.ziroom.minsu.spider.core.result.ResultCode;
import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.service.AsyncService;
import com.ziroom.minsu.spider.service.ProxyIpPipelineService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    private static Logger LOGGER = LoggerFactory.getLogger(SyncLockController.class);

    @Autowired
    private ProxyIpPipelineService proxyIpPipelineService;

    @Autowired
    private AsyncService asyncService;

    /**
     * 同步单个日历
     * @author jixd
     * @created 2017年08月22日 15:34:26
     * @param
     * @return
     */
    @RequestMapping(name = "/syncSingleHouse",method = RequestMethod.POST)
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
        //开始读取日历数据
        List<String> ipList = proxyIpPipelineService.listProxyIp();
        if (Check.NuNCollection(ipList)){
            return result.setStatus(ResultCode.FAIL).setMessage("无可用ip");
        }
        //异步方法调用
        asyncService.saveHouseCalendarDateAndSendMq(houseRelateDto, ipList);
        LOGGER.info("返回结果");
        return result;
    }







}
