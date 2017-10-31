package com.ziroom.minsu.spider.service;/*
 * <P></P>
 * <P>
 * <PRE>
 * <BR> 修改记录
 * <BR>------------------------------------------
 * <BR> 修改日期       修改人        修改内容
 * </PRE>
 * 
 * @Author lusp
 * @Date Create in 2017年08月 23日 19:54
 * @Version 1.0
 * @Since 1.0
 */

import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;

import java.util.List;
import java.util.concurrent.Future;

public interface AsyncService {

    Future<Boolean> checkProxyIp(String url, String ip, int port);


    void saveHouseCalendarDateAndSendMq(List<HouseRelateDto> houseRelateDto, List<String> ipList);

    boolean checkCalendarUrlAvailable(String calendarUrl, List<String> ipList);
}
