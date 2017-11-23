package com.ziroom.minsu.spider.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ziroom.minsu.spider.core.utils.Check;
import com.ziroom.minsu.spider.core.utils.HttpClientUtil;
import com.ziroom.minsu.spider.core.utils.ValueUtil;
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.service.AsyncCalendarService;
import com.ziroom.minsu.spider.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
public class SyncCalendarTasker {

    private static Logger LOGGER = LoggerFactory.getLogger(SyncCalendarTasker.class);

    private static final String logPreStr = "SyncLockTasker-";

    private static final String SYNC_CALENDAR_TASKER_THREAD_NAME = "SYNC_CALENDAR_TASKER_THREAD_NAME-";

    @Value("${airbnbRelate.url}")
    private String airbnbRelateUrl;

    @Autowired
    private AsyncCalendarService asyncCalendarService;

    @Autowired
    private RedisService redisService;


    /**
     * 
     * 同步房源日历线程防御性容错
     * 
     * @author zhangyl2
     * @created 2017年11月16日 16:40
     * @param 
     * @return 
     */
    public void runAsyncCalendar() {

        if (redisService.getDistributedLock(SYNC_CALENDAR_TASKER_THREAD_NAME)) {
            LOGGER.info(logPreStr + "[同步房源日历线程]启动！");
            startSyncCalendarTasker();
        } else {
            LOGGER.info(logPreStr + "[同步房源日历线程]已经启动或尚未结束!请勿重复调用！");
        }
    }

    public void startSyncCalendarTasker() {

        LOGGER.info(logPreStr + "start 开始获取房源ab关系接口数据，airbnbRelateUrl={}", airbnbRelateUrl);

        int page = 1;
        int limit = 100;
        JSONObject resultObject = getRelateResult(page, limit);

        if (Check.NuNObj(resultObject) || Check.NuNObj(resultObject.getJSONObject("data"))) {
            LOGGER.info(logPreStr + "房源日历关系分页接口异常，当前页={}，airbnbRelateUrl={}", page, airbnbRelateUrl);
            return;
        }

        // 获取总页数
        JSONObject data = resultObject.getJSONObject("data");
        int totalPage = ValueUtil.getPage(data.getInteger("total"), limit);

        for (int i = 1; i <= totalPage; i++) {

            resultObject = getRelateResult(i, limit);
            if (Check.NuNObj(resultObject) || Check.NuNObj(resultObject.getJSONObject("data"))) {
                LOGGER.info(logPreStr + "房源日历关系分页接口异常，当前页={}，airbnbRelateUrl={}", i, airbnbRelateUrl);
                continue;
            }
            data = resultObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            if(Check.NuNCollection(list)){
                break;
            }

            List<HouseRelateDto> houseRelateDtoList = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                JSONObject obj = (JSONObject) list.get(j);
                HouseRelateDto houseRelateDto = new HouseRelateDto();
                houseRelateDto.setHouseFid(obj.getString("houseFid"));
                houseRelateDto.setRoomFid(obj.getString("roomFid"));
                houseRelateDto.setRentWay(obj.getInteger("rentWay"));
                houseRelateDto.setCalendarUrl(obj.getString("calendarUrl"));
                houseRelateDto.setAbSn(obj.getString("abSn"));
                houseRelateDtoList.add(houseRelateDto);
            }

            asyncCalendarService.asyncHouseCalendarList(houseRelateDtoList);

        }

    }

    /**
     * 
     * 分页获取
     * 
     * @author zhangyl2
     * @created 2017年11月17日 13:42
     * @param 
     * @return 
     */
    private JSONObject getRelateResult(int page, int limit) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("page", String.valueOf(page));
        paramMap.put("limit", String.valueOf(limit));
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Accept", "application/json");

        // 重试3次
        String resultJson = null;
        for (int i = 0; Check.NuNStr(resultJson) && i < 3; i++) {
            resultJson = HttpClientUtil.sendPost(airbnbRelateUrl, paramMap, headerMap);
        }

        JSONObject resultObject = JSONObject.parseObject(resultJson);

        return resultObject;
    }

}
