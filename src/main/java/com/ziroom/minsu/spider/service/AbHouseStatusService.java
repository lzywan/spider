package com.ziroom.minsu.spider.service;

import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;

import java.util.List;

/**
 * <p>ab房源状态处理</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 17:38
 * @since 1.0
 */
public interface AbHouseStatusService {

   int saveUpdateAbHouse(List<CalendarDataVo> list,String abSn);

}
