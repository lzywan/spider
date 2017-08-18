/**
 * @FileName: CalendarDataVo.java
 * @Package ical4j
 * 
 * @author zl
 * @created 2017年4月12日 下午2:54:47
 * 
 * Copyright 2016-2025 ziroom
 */
package com.ziroom.minsu.spider.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>TODO</p>
 * 
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * 
 * @author zl
 * @since 1.0
 * @version 1.0
 */
public class CalendarDataVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5960800886360770808L;
	
	/**
	 * 用户编号
	 */
	private String uid;
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 结束时间
	 */
	private Date endDate;	
	/**
	 * 摘要
	 */
	private String summary;	
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 
	 */
	private String location;
	
	
	private Map<String, Object> descriptionMap = new HashMap<>();
	
	/**
	 * 是否可租  0：不可租 ，1：可租
	 */
	private Integer summaryStatus;
	
	/**
	 * 日历类型 1：订单，2：房东锁定， 3：系统锁定
	 */
	private Integer calendarType;


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Map<String, Object> getDescriptionMap() {
		return descriptionMap;
	}

	public void setDescriptionMap(Map<String, Object> descriptionMap) {
		this.descriptionMap = descriptionMap;
	}

	public Integer getSummaryStatus() {
		return summaryStatus;
	}

	public void setSummaryStatus(Integer summaryStatus) {
		this.summaryStatus = summaryStatus;
	}

	public Integer getCalendarType() {
		return calendarType;
	}

	public void setCalendarType(Integer calendarType) {
		this.calendarType = calendarType;
	}

	@Override
	public String toString() {
		return "CalendarDataVo{" +
				"uid='" + uid + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", summary='" + summary + '\'' +
				", description='" + description + '\'' +
				", location='" + location + '\'' +
				", descriptionMap=" + descriptionMap +
				", summaryStatus=" + summaryStatus +
				", calendarType=" + calendarType +
				'}';
	}
}
