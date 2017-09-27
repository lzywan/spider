package com.ziroom.minsu.spider.mapper;

import com.ziroom.minsu.spider.domain.AbHouseStatus;
/**
 * ab日历同步原始数据
 * @author jixd
 * @created 2017年08月22日 17:45:41
 * @param
 * @return
 */
public interface AbHouseStatusMapper {
    /**
     * 保存记录
     * @author jixd
     * @created 2017年08月22日 17:45:56
     * @param
     * @return
     */
    int insert(AbHouseStatus record);

    /**
     * 根据时间删除锁定时间
     * @param abSn
     * @return
     */
    int deleteByLockTime(String abSn);
}