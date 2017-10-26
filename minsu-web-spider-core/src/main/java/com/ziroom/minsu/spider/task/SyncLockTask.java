package com.ziroom.minsu.spider.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.service.AsyncService;
import com.ziroom.minsu.spider.service.ProxyIpPipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>TODO</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月25日 14:06
 * @since 1.0
 */
@Component
public class SyncLockTask {

    private static Logger LOGGER = LoggerFactory.getLogger(SyncLockTask.class);

    @Value("${airbnbRelate.url}")
    private String airbnbRelateUrl;

    @Autowired
    private ProxyIpPipelineService proxyIpPipelineService;

    @Autowired
    private AsyncService asyncService;

    @Scheduled(cron = "00 59 0/1 * * ?")
    public void syncLockByPage(){
        LOGGER.info("定时任务开始执行 syncLockByPage");
        int page = 1;
        int limit = 50;
        for (;;){
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("page",String.valueOf(page));
            paramMap.put("limit",String.valueOf(limit));
            String resultJson = HttpClientUtil.sendPost(airbnbRelateUrl, paramMap, null);
            LOGGER.info("分页返回结果={}",resultJson);

            JSONObject resultObject = JSONObject.parseObject(resultJson);
            Integer code = resultObject.getInteger("code");
            if (code != 0){
                LOGGER.error("房源日历关系分页接口异常");
                break;
            }

            JSONObject data = resultObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            if (list.size() == 0){
                break;
            }
            List<String> ipList = proxyIpPipelineService.listProxyIp();

            for (int i = 0;i<list.size();i++){
                JSONObject obj = (JSONObject) list.get(i);
                HouseRelateDto houseRelateDto = new HouseRelateDto();
                houseRelateDto.setHouseFid(obj.getString("houseFid"));
                houseRelateDto.setRoomFid(obj.getString("roomFid"));
                houseRelateDto.setRentWay(obj.getInteger("rentWay"));
                houseRelateDto.setCalendarUrl(obj.getString("calendarUrl"));
                houseRelateDto.setAbSn(obj.getString("abSn"));
                if (Check.NuNCollection(ipList)){
                    ipList = proxyIpPipelineService.listProxyIp();
                }
                asyncService.saveHouseCalendarDateAndSendMq(houseRelateDto,ipList);
            }

            page++;

        }

    }

}
