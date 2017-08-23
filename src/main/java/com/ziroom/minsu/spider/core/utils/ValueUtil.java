package com.ziroom.minsu.spider.core.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>获取值信息</p>
 * <p/>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author afi on 2016/3/28.
 * @version 1.0
 * @since 1.0
 */
public class ValueUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValueUtil.class);

    /**
     * 获取总共的页码
     * @author afi
     * @param count
     * @param limit
     * @return
     */
    public static int getPage(Integer count,Integer limit){
        double countD = new Double(count);
        double limitD = new Double(limit);
        double c = countD/limitD;
        return (int)Math.ceil(c);
    }

}

