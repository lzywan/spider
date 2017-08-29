package com.ziroom.minsu.spider.domain.vo;

import java.util.List;

/**
 * <p>返回的数据</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 15:52
 * @since 1.0
 */
public class CalendarTimeDataVo {

    /**
     * 房源fid
     */
    private String houseFid;

    /**
     * 房间fid
     */
    private String roomFid;
    /**
     * 出租方式
     */
    private Integer rentWay;

    private List<TimeDataVo> calendarDataVos;

    public String getHouseFid() {
        return houseFid;
    }

    public void setHouseFid(String houseFid) {
        this.houseFid = houseFid;
    }

    public String getRoomFid() {
        return roomFid;
    }

    public void setRoomFid(String roomFid) {
        this.roomFid = roomFid;
    }

    public Integer getRentWay() {
        return rentWay;
    }

    public void setRentWay(Integer rentWay) {
        this.rentWay = rentWay;
    }

    public List<TimeDataVo> getCalendarDataVos() {
        return calendarDataVos;
    }

    public void setCalendarDataVos(List<TimeDataVo> calendarDataVos) {
        this.calendarDataVos = calendarDataVos;
    }
}
