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

public interface AsyncCalendarService {

    /**
     * 
     * 分页同步
     * 
     * @author zhangyl2
     * @created 2017年11月16日 11:58
     * @param 
     * @return 
     */
    void asyncHouseCalendarList(List<HouseRelateDto> houseRelateDto);

    /**
     * 
     * 同步单个日历
     * 
     * @author zhangyl2
     * @created 2017年11月16日 11:58
     * @param 
     * @return 
     */
    boolean syncHouseCalendar(HouseRelateDto houseRelateDto);

    boolean checkCalendarUrlAvailable(HouseRelateDto houseRelateDto);
}
