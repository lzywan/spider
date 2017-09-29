package com.ziroom.minsu.spider.service;

import com.ziroom.minsu.spider.domain.XiaoZhuHouseInfo;

import java.util.List;

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
 * @Date Created in 2017年08月16日 09:57
 * @since 1.0
 */
public interface XiaoZhuHouseService {

    XiaoZhuHouseInfo findInfoById(int id);

    int save(XiaoZhuHouseInfo xiaoZhuHouseInfo);

    List<XiaoZhuHouseInfo> findByPage(int page,int limit);


    List<XiaoZhuHouseInfo> findByPageV2(int page,int limit);

    int update();
}
