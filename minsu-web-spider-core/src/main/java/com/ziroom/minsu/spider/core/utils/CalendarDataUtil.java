/**
 * @FileName: CalendarDataUtil.java
 * @Package ical4j
<<<<<<< HEAD
 * 
 * @author zl
 * @created 2017年4月12日 下午3:11:50
 * 
=======
 * @author zl
 * @created 2017年4月12日 下午3:11:50
 * <p>
>>>>>>> test
 * Copyright 2016-2025 ziroom
 */
package com.ziroom.minsu.spider.core.utils;


import com.alibaba.fastjson.JSONObject;
<<<<<<< HEAD
import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.HostInfo;
import net.fortuna.ical4j.util.Strings;
import net.fortuna.ical4j.util.UidGenerator;
=======
import com.ziroom.minsu.spider.domain.dto.HouseRelateDto;
import com.ziroom.minsu.spider.domain.vo.CalendarDataVo;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.util.Strings;
>>>>>>> test
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

<<<<<<< HEAD
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
=======
import java.io.*;
>>>>>>> test
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
<<<<<<< HEAD
import java.util.Random;
=======
>>>>>>> test
import java.util.Set;

/**
 * <p>日历工具类</p>
<<<<<<< HEAD
 * 
=======
 *
>>>>>>> test
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
<<<<<<< HEAD
 * 
=======
 *
>>>>>>> test
 * @author zl
 * @since 1.0
 * @version 1.0
 */
public class CalendarDataUtil {

<<<<<<< HEAD
	private static final Logger LOGGER = LoggerFactory.getLogger(CalendarDataUtil.class);
	
	private static final Map<String, ThreadLocal<SimpleDateFormat>> dateFormatPool = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	private static final Object dateFormatLock = new Object();
	
	private static final Set<String> userAgentSet= new HashSet<>();

    private static final String propertysperator = ":";

    private static final String linesperator =  Strings.LINE_SEPARATOR;

	static{
		//初始化userAgent
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
		userAgentSet.add("Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1  (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1");
		userAgentSet.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3");
		userAgentSet.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24");
		userAgentSet.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24");
	}
	
	/**
	 * 
	 * 转化日历对象
	 *
	 * @author zl
	 * @created 2017年4月12日 下午3:23:07
	 *
	 * @param in
	 * @return
	 */
	public static Calendar getCalendarData(InputStream in) {
		if (in == null) {
			return null;
		}
		try {
			CalendarBuilder builder = new CalendarBuilder();
			InputStreamReader reader = new InputStreamReader(in);			
			return builder.build(reader);
		} catch (Exception e) {
			LOGGER.info("转化日历对象失败，e={}", e);
		}
		return null;
	}
	
	/**
	 * 
	 * 获取日历数据列表
	 *
	 * @author zl
	 * @created 2017年4月12日 下午3:26:50
	 *
	 * @param calendar
	 * @return
	 */
	public static List<CalendarDataVo> getCalendarDataList(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		List<CalendarDataVo> list = new ArrayList<>();
		List<CalendarComponent> componentList = calendar.getComponents();
		if (componentList != null && componentList.size() > 0) {
			for (CalendarComponent calendarComponent : componentList) {
				CalendarDataVo dataVo = getCalendarDataVo(calendarComponent);
				if (dataVo != null) {
					list.add(dataVo);
				}
			}
		}

		return list;
	}
	
	/**
	 * 
	 * 转化事件
	 *
	 * @author zl
	 * @created 2017年4月12日 下午3:28:47
	 *
	 * @param calendarComponent
	 * @return
	 */
	public static CalendarDataVo getCalendarDataVo(CalendarComponent calendarComponent) {
		if (calendarComponent == null) {
			return null;
		}
		CalendarDataVo dataVo = new CalendarDataVo();

		try {
			PropertyList properties = calendarComponent.getProperties();
			if (properties != null) {
				for (Property property : properties) {
					if (CalendarDataConstant.PROPERTY_UID.equalsIgnoreCase(property.getName())) {
						dataVo.setUid(property.getValue());
					} else if (CalendarDataConstant.PROPERTY_DTSTART.equalsIgnoreCase(property.getName())) {
						dataVo.setStartDate(transDayDate(property.getValue()));
					} else if (CalendarDataConstant.PROPERTY_DTEND.equalsIgnoreCase(property.getName())) {
						dataVo.setEndDate(transDayDate(property.getValue()));
					} else if (CalendarDataConstant.PROPERTY_DESCRIPTION.equalsIgnoreCase(property.getName())) {
						dataVo.setDescription(property.getValue());
						if (dataVo.getDescription() != null) {
							dataVo.setDescriptionMap(getDescriptionMap(dataVo.getDescription()));
						}

					} else if (CalendarDataConstant.PROPERTY_SUMMARY.equalsIgnoreCase(property.getName())) {
						dataVo.setSummary(property.getValue());
						if (dataVo.getSummary() != null && CalendarDataConstant.PROPERTY_SUMMARY_STATUS
								.equalsIgnoreCase(dataVo.getSummary().trim())) {
							dataVo.setSummaryStatus(1);
						}else if (dataVo.getSummary() != null){
							dataVo.setSummaryStatus(2);
						}
					} else if (CalendarDataConstant.PROPERTY_LOCATION.equalsIgnoreCase(property.getName())) {
						dataVo.setLocation(property.getValue());
					}
				}
			}

		} catch (Exception e) {
			LOGGER.error("事件解析失败，data={},e={}", JSONObject.toJSONString(calendarComponent), e);
		}
		return dataVo;
	}
	
	/**
	 * 
	 * 获取描述字典
	 *
	 * @author zl
	 * @created 2017年4月12日 下午5:10:25
	 *
	 * @param description
	 * @return
	 */
	private static Map<String, Object> getDescriptionMap(String description) {
		Map<String, Object> map = new HashMap<>();

		try {
			if (description != null) {
				String[] tmppars = description.split("\n");
				if (tmppars != null && tmppars.length > 0) {
					for (String tmppar : tmppars) {
						String[] par = tmppar.split(":");
						if (par != null && par.length == 2) {
							if (par[0] != null) {

								if (CalendarDataConstant.PROPERTY_DESCRIPTION_CHECKIN.equalsIgnoreCase(par[0])) {
									map.put(par[0], transDayDate(par[1]));
								} else if (CalendarDataConstant.PROPERTY_DESCRIPTION_CHECKOUT
										.equalsIgnoreCase(par[0])) {
									map.put(par[0], transDayDate(par[1]));
								} else {
									map.put(par[0], par[1]);
								}

							}

						}

					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("解析描述信息失败，description={},e={}", description, e);
		}

		return map;
	}
 
	/**
	 * 
	 * 获取日期格式化工具
	 *
	 * @author zl
	 * @created 2017年4月12日 下午5:36:45
	 *
	 * @param dateFormatPattern
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(final String dateFormatPattern) {
		ThreadLocal<SimpleDateFormat> tl = dateFormatPool.get(dateFormatPattern);
		if (null == tl) {
			synchronized (dateFormatLock) {
				tl = dateFormatPool.get(dateFormatPattern);
				if (null == tl) {
					tl = new ThreadLocal<SimpleDateFormat>() {
						@Override
						protected synchronized SimpleDateFormat initialValue() {
							return new SimpleDateFormat(dateFormatPattern);
						}
					};
					dateFormatPool.put(dateFormatPattern, tl);
				}
			}
		}
		return tl.get();
	}
	
	/**
	 * 
	 * 获取文件流
	 *
	 * @author zl
	 * @created 2017年4月12日 下午5:23:17
	 *
	 * @param filePath
	 * @return
	 */
	public static InputStream getInputStreamByFilePath(String filePath) {
		if (filePath == null) {
			return null;
		}
		InputStream in = null;

		try {
			in = new FileInputStream(filePath);
		} catch (Exception e) {
			LOGGER.error("获取文件流失败，filePath={},e={}", filePath, e);
		}

		return in;
	}

	
	/**
	 * 
	 * 获取代理
	 *
	 * @author zl
	 * @created 2017年4月12日 下午5:45:03
	 *
	 * @return
	 */
	public static String getUserAgent(){
		if (userAgentSet.size()>0) {
			int n = new Random().nextInt(userAgentSet.size()-1);
			return (String) userAgentSet.toArray()[n];
		}
		return null; 
	}

	/**
	 * 随机获取ip
	 * @author jixd
	 * @created 2017年08月24日 13:36:42
	 * @param
	 * @return
	 */
	public static String getRandomIp(List<String> list){
		if (!Check.NuNCollection(list)){
			return list.get(RandomUtils.nextInt(0,list.size()));
		}
		return null;
	}
	
	/**
	 * 
	 * 将流数据规范化
	 *
	 * @author zl
	 * @created 2017年4月12日 下午6:18:02
	 *
	 * @param originalInputStream
	 * @return
	 */
	private static InputStream normalizeInputStream(InputStream originalInputStream) {
		if (originalInputStream == null) {
			return null;
		}
		InputStream newInputStream;

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(originalInputStream));
			StringBuilder resultStringBuilder = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				int t = line.indexOf(propertysperator);
				if ((t <= 0 && resultStringBuilder.toString().endsWith(linesperator)) ||
                        (t > 0 && !line.startsWith(Calendar.BEGIN)
                                && !line.startsWith(Calendar.END)
                                && !line.startsWith(Calendar.VCALENDAR)
                                && !isProperty(line)
                                && resultStringBuilder.toString().endsWith(linesperator))) {

                    resultStringBuilder.delete(resultStringBuilder.lastIndexOf(linesperator), resultStringBuilder.length());
				}
				resultStringBuilder.append(line);
				resultStringBuilder.append(linesperator);
			}
			newInputStream = new ByteArrayInputStream(resultStringBuilder.toString().getBytes());

		} catch (Exception e) {
			LOGGER.error("流数据规范化失败或者超时，e={}", e);
			return null;
		}

		return newInputStream;
	}
	
	/**
	 * 
	 * 
	 * 是否标准属性
	 *
	 * @author zl
	 * @created 2017年6月8日 下午10:30:42
	 *
	 * @param string
	 */
	public static boolean isProperty(String string){
		boolean is = false;
		if (StringUtils.isNotEmpty(string)) {
			for(String prop:getPropertys()){
				if (string.startsWith(prop)) {
					is=true;
					break;
				}
			}
		}
		
		return is;
	}
	
	
	
	/**
	 * 
	 * 可设置的属性名称
	 *
	 * @author zl
	 * @created 2017年6月7日 下午10:11:22
	 *
	 * @return
	 */
	public static List<String> getPropertys(){
		List<String> list = new ArrayList<>(); 
		Class entityClass = Property.class;		
		Field[] entityFields = entityClass.getDeclaredFields();
		for (Field field : entityFields) {
			list.add(field.getName());
		}
		
		return list;
	}
	
	
	/**
	 * 
	 * 转化事件对象
	 *
	 * @author zl
	 * @created 2017年4月13日 下午5:31:24
	 *
	 * @param calendarDataVo
	 * @return
	 */
	public static VEvent getEvent(CalendarDataVo calendarDataVo){
		
		if (calendarDataVo==null) {
			return null;
		}else if(calendarDataVo.getStartDate()==null || calendarDataVo.getEndDate()==null || calendarDataVo.getSummary()==null){
			LOGGER.error("数据错误，calendarDataVo={}",JSONObject.toJSONString(calendarDataVo));
			return null;
		}
		
		try {
			DateTime start = new DateTime(calendarDataVo.getStartDate());
			DateTime end = new DateTime(calendarDataVo.getEndDate());
			VEvent event = new VEvent(start, end, calendarDataVo.getSummary());
			
			UidGenerator ug = new UidGenerator(new HostInfo() {				
				@Override
				public String getHostName() { 
					return CalendarDataConstant.PROPERTY_UID_HOSTNAME;
				}
			}, "uidGen");
			Uid uid = ug.generateUid();
			event.getProperties().add(uid);
			
			
			
			return event;
		} catch (Exception e) {
			LOGGER.error("转化事件对象失败，calendarDataVo={},e={}",JSONObject.toJSONString(calendarDataVo), e);
		}
		
		return null;
	}
	
	
	/**
	 * 
	 * 转化事件列表
	 *
	 * @author zl
	 * @created 2017年4月13日 下午5:16:01
	 *
	 * @param dataList
	 * @return
	 */
	public static List<VEvent> getEventList(List<CalendarDataVo> dataList){
		if(dataList==null){
			return null;
		}
		
		List<VEvent> eventList = new ArrayList<>();
		
		for (CalendarDataVo calendarDataVo : dataList) {
			VEvent event = getEvent(calendarDataVo);
			if (event!=null) {				
				eventList.add(event);
			}
		}
		
		return eventList;
	}
	
	
	/**
	 * 
	 * 获取日历对象
	 *
	 * @author zl
	 * @created 2017年4月13日 下午4:33:00
	 *
	 * @param eventList
	 * @return
	 */
	public static Calendar getIcsCalendar(List<VEvent> eventList){
		
		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(new ProdId(CalendarDataConstant.PROPERTY_PRODID));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(Version.VERSION_2_0);
		try {
			
			if(eventList!=null && eventList.size()>0){
				for (VEvent vEvent : eventList) {
					icsCalendar.getComponents().add(vEvent);
				}
			} 
			
		} catch (Exception e) {
			LOGGER.error("获取IcsCalendar失败，eventList={},e={}",JSONObject.toJSONString(eventList), e);
		}
		
		return icsCalendar;
	}
	
	
	
	/**
	 * 
	 * 获取输出流
	 *
	 * @author zl
	 * @created 2017年4月13日 下午4:22:25
	 *
	 * @param calendarDataVoList
	 * @param outputStream
	 * @return
	 */
	public static void calendarFileOutput(List<CalendarDataVo> calendarDataVoList, OutputStream outputStream){
		if (calendarDataVoList == null || outputStream == null) {
			return;
		}
		
		Calendar icsCalendar = getIcsCalendar(getEventList(calendarDataVoList));
		
		try {
			CalendarOutputter outputter = new CalendarOutputter();
			outputter.output(icsCalendar, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			LOGGER.error("输出文件失败，calendarDataVoList={},icsCalendar={},e={}", calendarDataVoList, JSONObject.toJSONString(icsCalendar), e);
		} 
		
	}
	
	
	public class CalendarDataConstant{
		
		public static final String  PROPERTY_UID="UID";
		
		public static final String  PROPERTY_DTSTART="DTSTART";
		
		public static final String  PROPERTY_DTEND="DTEND";
		
		public static final String  PROPERTY_DESCRIPTION="DESCRIPTION";
		
		public static final String  PROPERTY_SUMMARY="SUMMARY";
		
		public static final String  PROPERTY_LOCATION="LOCATION";
		
		public static final String  PROPERTY_SUMMARY_STATUS="Not available";
		
		public static final String  PROPERTY_DESCRIPTION_PHONE="PHONE";
		
		public static final String  PROPERTY_DESCRIPTION_CHECKIN="CHECKIN";
		
		public static final String  PROPERTY_DESCRIPTION_CHECKOUT="CHECKOUT";
		
		public static final String  PROPERTY_DESCRIPTION_PROPERTY="PROPERTY";
		
		public static final String  PROPERTY_DESCRIPTION_EMAIL="EMAIL";
		
		public static final String  PROPERTY_DESCRIPTION_NIGHTS="NIGHTS";
		
		public static final String  PROPERTY_UID_HOSTNAME="ziroom.com";
		
		public static final String  PROPERTY_PRODID="--//ziroom Inc//Hosting Calendar 1.0.0//EN";

	}

	/**
	 * 流转换成对象
	 * @author jixd
	 * @created 2017年04月19日 16:15:58
	 * @param
	 * @return
	 */
	public static List<CalendarDataVo> transStreamToListVo(InputStream in){
		return getCalendarDataList(getCalendarData(normalizeInputStream(in)));
	}
	
	/**
	 * 
	 * 转化日期
	 *
	 * @author zl
	 * @created 2017年6月29日 上午10:54:29
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date transDayDate(String dateStr){
		Date date = null;
		for (String dayFmt : getDateFormatDayList()) {
			try {
				date = getDateFormat(dayFmt).parse(dateStr);
				break;
			} catch (Exception e) {
				continue;
			}
		}
		return date;
	}

	/**
	 * 
	 * 日期格式
	 *
	 * @author zl
	 * @created 2017年6月29日 上午10:53:12
	 *
	 * @return
	 */
	public static Set<String> getDateFormatDayList(){
		Set<String> set = new HashSet<>();
		set.add("yyyy-MM-dd");
		set.add("yyyyMMdd");
		set.add("yyyy/MM/dd");
		return set;
	}
	

	/**
	 * TODO
	 *
	 * @author zl
	 * @created 2017年4月12日 下午3:11:50
	 *
	 * @param args
	 * @throws ParserException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParserException { 		
		
//		InputStream in =  getInputStreamByFilePath("D:\\Personal\\Downloads\\listing-17373819.ics");
		//InputStream in = getInputStreamByUrl("https://www.airbnbchina.cn/calendar/ical/16582865.ics?s=f048109d9e534ca065555628ada9b5c6");
//	  
	    //List<CalendarDataVo> list=  getCalendarDataList(getCalendarData(normalizeInputStream(in)));
//	    
	    //System.out.println(JsonEntityTransform.Object2Json(list));
		
//		createCalendarData();
		
		/*Calendar icsCalendar = getIcsCalendar(getEventList(list));
		calendarFileOutput(icsCalendar,"88jggfkf9");*/


		/*List<CalendarDataVo> calendarDataVos = convertToList("https://www.airbnbchina.cn/calendar/ical/16582865.ics?s=f048109d9e534ca065555628ada9b5c6");
		System.out.println(JsonEntityTransform.Object2Json(calendarDataVos));
		LogUtil.info(LOGGER,"结果={}",JsonEntityTransform.Object2Json(calendarDataVos));*/

        /*Set<String> ipSet = new HashSet<>();
        ipSet.add("121.199.28.155:80");

        CalendarDataUtil.initIpSet(ipSet);
        String randomIp = CalendarDataUtil.getRandomIp(ipSet);*/
     /* String randomIp = "202.202.90.20:8080";
//        InputStream in = getInputStreamByUrl("https://www.airbnbchina.cn/calendar/ical/17539939.ics?s=0ef6c764be261e8e22776eaddb63e226",randomIp);
        InputStream in = getInputStreamByUrl("https://www.airbnbchina.cn/calendar/ical/16582865.ics?s=f048109d9e534ca065555628ada9b5c6",randomIp);

       List<CalendarDataVo> list=  getCalendarDataList(getCalendarData(normalizeInputStream(in)));
        System.out.println(JSONObject.toJSONString(list));*/


        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
		String s = HttpClientUtil.sendProxyGet("https://zh.airbnb.com/calendar/ical/17908318.ics?s=0e3f15c4f6e974281f4375b219ddc5d8", headerMap, "60.255.186.169", 8888);
		System.err.println(s);
		List<CalendarDataVo> list=  getCalendarDataList(getCalendarData(normalizeInputStream(new ByteArrayInputStream(s.getBytes()))));
		System.out.println(JSONObject.toJSONString(list));

	}
	
	/**
	 * 
	 * TODO
	 *
	 * @author baiwei
	 * @created 2017年6月27日 下午3:27:07
	 *
	 */
	public static void aaa(){
//		List<CalendarDataVo> list=  getCalendarDataList(getCalendarData(normalizeInputStream(in)));
//		Calendar icsCalendar = getIcsCalendar(getEventList(list));
//		calendarFileOutput(icsCalendar,"88jggfkf9");
	}
=======
    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarDataUtil.class);

    private static final Map<String, ThreadLocal<SimpleDateFormat>> dateFormatPool = new HashMap<>();

    private static final Object dateFormatLock = new Object();

    private static final List<String> userAgentList = new ArrayList<>();

    private static final String propertysperator = ":";

    private static final String linesperator = Strings.LINE_SEPARATOR;

    static {
        initUserAgentList();
    }

    private static void initUserAgentList() {
        //初始化userAgent
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
        userAgentList.add("Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1  (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1");
        userAgentList.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3");
        userAgentList.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24");
    }

    /**
     * 流转换成对象
     * @author jixd
     * @created 2017年04月19日 16:15:58
     * @param
     * @return
     */
    public static List<CalendarDataVo> transStreamToListVo(InputStream originalInputStream) throws Exception {

        // 日历流数据规范化
        InputStream in;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(originalInputStream));
            StringBuilder resultStringBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                int t = line.indexOf(propertysperator);
                if (t <= 0 || (t > 0
                        && !line.startsWith(Calendar.BEGIN)
                        && !line.startsWith(Calendar.END)
                        && !line.startsWith(Calendar.VCALENDAR)
                        && !isProperty(line))) {
                    if (resultStringBuilder.toString().endsWith(linesperator)) {
                        resultStringBuilder.delete(resultStringBuilder.lastIndexOf(linesperator), resultStringBuilder.length());
                    }
                }
                resultStringBuilder.append(line);
                resultStringBuilder.append(linesperator);
            }
            in = new ByteArrayInputStream(resultStringBuilder.toString().getBytes());
        } catch (Exception e) {
            throw new Exception("日历流数据规范化失败!", e);
        }

        // 转化日历对象
        Calendar calendar;
        try {
            CalendarBuilder builder = new CalendarBuilder();
            InputStreamReader reader = new InputStreamReader(in);
            calendar = builder.build(reader);
            if (calendar == null) {
                throw new Exception("日历对象构建为空!");
            }
            List<CalendarDataVo> list = new ArrayList<>();
            List<CalendarComponent> componentList = calendar.getComponents();
            if (componentList != null && componentList.size() > 0) {
                for (CalendarComponent calendarComponent : componentList) {
                    CalendarDataVo dataVo = getCalendarDataVo(calendarComponent);
                    if (dataVo != null) {
                        list.add(dataVo);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            throw new Exception("转化日历对象失败!", e);
        }
    }

    /**
     *
     *
     * 是否标准属性
     *
     * @author zl
     * @created 2017年6月8日 下午10:30:42
     *
     * @param string
     */
    public static boolean isProperty(String string) {
        boolean is = false;
        if (StringUtils.isNotEmpty(string)) {
            for (String prop : getPropertys()) {
                if (string.startsWith(prop)) {
                    is = true;
                    break;
                }
            }
        }
        return is;
    }

    /**
     *
     * 可设置的属性名称
     *
     * @author zl
     * @created 2017年6月7日 下午10:11:22
     *
     * @return
     */
    public static List<String> getPropertys() {
        List<String> list = new ArrayList<>();
        Class entityClass = Property.class;
        Field[] entityFields = entityClass.getDeclaredFields();
        for (Field field : entityFields) {
            list.add(field.getName());
        }

        return list;
    }

    /**
     *
     * 转化事件
     *
     * @author zl
     * @created 2017年4月12日 下午3:28:47
     *
     * @param calendarComponent
     * @return
     */
    public static CalendarDataVo getCalendarDataVo(CalendarComponent calendarComponent) {
        if (calendarComponent == null) {
            return null;
        }
        CalendarDataVo dataVo = new CalendarDataVo();

        try {
            PropertyList properties = calendarComponent.getProperties();
            if (properties != null) {
                for (Property property : properties) {
                    if (CalendarDataConstant.PROPERTY_UID.equalsIgnoreCase(property.getName())) {
                        dataVo.setUid(property.getValue());
                    } else if (CalendarDataConstant.PROPERTY_DTSTART.equalsIgnoreCase(property.getName())) {
                        dataVo.setStartDate(transDayDate(property.getValue()));
                    } else if (CalendarDataConstant.PROPERTY_DTEND.equalsIgnoreCase(property.getName())) {
                        dataVo.setEndDate(transDayDate(property.getValue()));
                    } else if (CalendarDataConstant.PROPERTY_DESCRIPTION.equalsIgnoreCase(property.getName())) {
                        dataVo.setDescription(property.getValue());
                        if (dataVo.getDescription() != null) {
                            dataVo.setDescriptionMap(getDescriptionMap(dataVo.getDescription()));
                        }

                    } else if (CalendarDataConstant.PROPERTY_SUMMARY.equalsIgnoreCase(property.getName())) {
                        dataVo.setSummary(property.getValue());
                        if (dataVo.getSummary() != null && CalendarDataConstant.PROPERTY_SUMMARY_STATUS
                                .equalsIgnoreCase(dataVo.getSummary().trim())) {
                            dataVo.setSummaryStatus(1);
                        } else if (dataVo.getSummary() != null) {
                            dataVo.setSummaryStatus(2);
                        }
                    } else if (CalendarDataConstant.PROPERTY_LOCATION.equalsIgnoreCase(property.getName())) {
                        dataVo.setLocation(property.getValue());
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("事件解析失败，data={},e={}", JSONObject.toJSONString(calendarComponent), e);
        }
        return dataVo;
    }

    /**
     *
     * 获取描述字典
     *
     * @author zl
     * @created 2017年4月12日 下午5:10:25
     *
     * @param description
     * @return
     */
    private static Map<String, Object> getDescriptionMap(String description) {
        Map<String, Object> map = new HashMap<>();

        try {
            if (description != null) {
                String[] tmppars = description.split("\n");
                if (tmppars != null && tmppars.length > 0) {
                    for (String tmppar : tmppars) {
                        String[] par = tmppar.split(":");
                        if (par != null && par.length == 2) {
                            if (par[0] != null) {

                                if (CalendarDataConstant.PROPERTY_DESCRIPTION_CHECKIN.equalsIgnoreCase(par[0])) {
                                    map.put(par[0], transDayDate(par[1]));
                                } else if (CalendarDataConstant.PROPERTY_DESCRIPTION_CHECKOUT
                                        .equalsIgnoreCase(par[0])) {
                                    map.put(par[0], transDayDate(par[1]));
                                } else {
                                    map.put(par[0], par[1]);
                                }

                            }

                        }

                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("解析描述信息失败，description={},e={}", description, e);
        }

        return map;
    }

    /**
     *
     * 转化日期
     *
     * @author zl
     * @created 2017年6月29日 上午10:54:29
     *
     * @param dateStr
     * @return
     */
    public static Date transDayDate(String dateStr) {
        Date date = null;
        for (String dayFmt : getDateFormatDayList()) {
            try {
                date = getDateFormat(dayFmt).parse(dateStr);
                break;
            } catch (Exception e) {
                continue;
            }
        }
        return date;
    }

    /**
     *
     * 日期格式
     *
     * @author zl
     * @created 2017年6月29日 上午10:53:12
     *
     * @return
     */
    public static Set<String> getDateFormatDayList() {
        Set<String> set = new HashSet<>();
        set.add("yyyy-MM-dd");
        set.add("yyyyMMdd");
        set.add("yyyy/MM/dd");
        return set;
    }

    /**
     *
     * 获取日期格式化工具
     *
     * @author zl
     * @created 2017年4月12日 下午5:36:45
     *
     * @param dateFormatPattern
     * @return
     */
    public static SimpleDateFormat getDateFormat(final String dateFormatPattern) {
        ThreadLocal<SimpleDateFormat> tl = dateFormatPool.get(dateFormatPattern);
        if (null == tl) {
            synchronized (dateFormatLock) {
                tl = dateFormatPool.get(dateFormatPattern);
                if (null == tl) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected synchronized SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(dateFormatPattern);
                        }
                    };
                    dateFormatPool.put(dateFormatPattern, tl);
                }
            }
        }
        return tl.get();
    }


    /**
     *
     * 获取代理
     *
     * @author zl
     * @created 2017年4月12日 下午5:45:03
     *
     * @return
     */
    public static String getUserAgent() {
        if (Check.NuNCollection(userAgentList)) {
            initUserAgentList();
        }
        if (!Check.NuNCollection(userAgentList)) {
            return userAgentList.get(RandomUtils.nextInt(0, userAgentList.size()));
        }
        return null;
    }

    public class CalendarDataConstant {

        public static final String PROPERTY_UID = "UID";

        public static final String PROPERTY_DTSTART = "DTSTART";

        public static final String PROPERTY_DTEND = "DTEND";

        public static final String PROPERTY_DESCRIPTION = "DESCRIPTION";

        public static final String PROPERTY_SUMMARY = "SUMMARY";

        public static final String PROPERTY_LOCATION = "LOCATION";

        public static final String PROPERTY_SUMMARY_STATUS = "Not available";

        public static final String PROPERTY_DESCRIPTION_PHONE = "PHONE";

        public static final String PROPERTY_DESCRIPTION_CHECKIN = "CHECKIN";

        public static final String PROPERTY_DESCRIPTION_CHECKOUT = "CHECKOUT";

        public static final String PROPERTY_DESCRIPTION_PROPERTY = "PROPERTY";

        public static final String PROPERTY_DESCRIPTION_EMAIL = "EMAIL";

        public static final String PROPERTY_DESCRIPTION_NIGHTS = "NIGHTS";

        public static final String PROPERTY_UID_HOSTNAME = "ziroom.com";

        public static final String PROPERTY_PRODID = "--//ziroom Inc//Hosting Calendar 1.0.0//EN";

    }

    /**
     * TODO
     *
     * @author zl
     * @created 2017年4月12日 下午3:11:50
     *
     * @param args
     * @throws ParserException
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParserException {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1");
        String s = HttpClientUtil.sendProxyGet("https://zh.airbnb.com/calendar/ical/17908318.ics?s=0e3f15c4f6e974281f4375b219ddc5d8", headerMap, "60.255.186.169", 8888);
        System.err.println(s);
        try {
            List<CalendarDataVo> list = transStreamToListVo(new ByteArrayInputStream(s.getBytes()));
            System.out.println(JSONObject.toJSONString(list));
        } catch (Exception e) {

        }

    }
>>>>>>> test

}
