package com.ziroom.minsu.spider.service.impl;

import com.ziroom.minsu.spider.core.utils.DateUtil;
import com.ziroom.minsu.spider.core.utils.UUIDGenerator;
import com.ziroom.minsu.spider.domain.AbHouseStatus;
import com.ziroom.minsu.spider.domain.enums.SyncHouseLockEnum;
import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;
import com.ziroom.minsu.spider.mapper.AbHouseStatusMapper;
import com.ziroom.minsu.spider.service.AbHouseStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>ab日历状态</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 17:42
 * @since 1.0
 */
@Service
public class AbHouseStatusServiceImpl implements AbHouseStatusService{

    @Autowired
    private AbHouseStatusMapper abHouseStatusMapper;

    @Override
    public int saveUpdateAbHouse(List<CalendarDataVo> list,String abSn) {
        abHouseStatusMapper.deleteByLockTime(abSn);
        Date now = DateUtil.localDateTodate(LocalDate.now());
        int count = 0;
        //处理房源锁状态
        for (CalendarDataVo calendarDataVo : list){
            Date startDate = calendarDataVo.getStartDate();
            Date endDate = calendarDataVo.getEndDate();
            List<Date> dates = DateUtil.dateSplit(startDate, endDate);
            for (Date date : dates){
                if (date.before(now)){
                    continue;
                }
                AbHouseStatus abStatusEntity = new AbHouseStatus();
                abStatusEntity.setFid(UUIDGenerator.hexUUID());
                abStatusEntity.setUid(calendarDataVo.getUid());
                abStatusEntity.setSummary(calendarDataVo.getSummary());
                abStatusEntity.setSummaryStatus(calendarDataVo.getSummaryStatus());
                abStatusEntity.setAbSn(abSn);
                if (calendarDataVo.getSummaryStatus() == SyncHouseLockEnum.ORDER_LOCK.getCode()){
                    Map<String, Object> descriptionMap = calendarDataVo.getDescriptionMap();
                    abStatusEntity.setEmail((String) descriptionMap.get("EMAIL"));
                    abStatusEntity.setPhone((String) descriptionMap.get("PHONE"));
                }
                abStatusEntity.setLockTime(date);
                abHouseStatusMapper.insert(abStatusEntity);
            }
        }
        return count;
    }
}
