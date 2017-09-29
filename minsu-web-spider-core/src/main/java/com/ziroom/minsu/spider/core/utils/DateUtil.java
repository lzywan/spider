package com.ziroom.minsu.spider.core.utils;

import com.ziroom.minsu.spider.core.exception.ServiceException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>时间工具类</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 19:37
 * @since 1.0
 */
public final class DateUtil {


    /**
     * 日期转 localDateTime
     * @author jixd
     * @created 2017年08月23日 09:52:49
     * @param
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 日期转 localDate
     * @author jixd
     * @created 2017年08月23日 09:55:09
     * @param
     * @return
     */
    public static LocalDate dateToLocalDate(Date date){
        return dateToLocalDateTime(date).toLocalDate();
    }

    /**
     * localDate 转 date
     * @author jixd
     * @created 2017年08月23日 10:04:17
     * @param
     * @return
     */
    public static Date localDateTodate(LocalDate localDate){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 拆分时间
     * @author jixd
     * @created 2017年08月22日 20:41:23
     * @param
     * @return
     */
    public static List<Date> dateSplit(Date startDate,Date endDate){
        List<Date> dates = new ArrayList<>();
        if (startDate.after(endDate)){
            throw new ServiceException("开始时间在结束时间之后");
        }
        LocalDate startLocalDate = dateToLocalDate(startDate);
        LocalDate endLocalDate = dateToLocalDate(endDate);
        for (;;){
            if (!startLocalDate.isBefore(endLocalDate)){
                break;
            }
            dates.add(localDateTodate(startLocalDate));
            startLocalDate = startLocalDate.plusDays(1);
        }
        return dates;
    }




}
