package com.ziroom.minsu.spider.domain.constant;

/*
 * <P></P>
 * <P>
 * <PRE>
 * <BR> 修改记录
 * <BR>------------------------------------------
 * <BR> 修改日期       修改人        修改内容
 * </PRE>
 * 
 * @Author lusp
 * @Date Create in 2017年08月 23日 20:24
 * @Version 1.0
 * @Since 1.0
 */

public class HttpConstant {


    /**
     * socket 超时时间
     */
    public static int socket_time_out = 5000;

    /**
     * connect 超时时间
     */
    public static int connect_time_out = 5000;

    /**
     * 最大重定向
     */
    public static int max_redirects = 50;

    /**
     * request连接超时时间
     */
    public static int connection_request_time_out = 5000;

    /**
     * 验证代理ip是否可用的访问地址
     */
    public static String airbnbUrl = "https://www.airbnbchina.cn/";


    /**
     * 扫描代理ip是否可用,分页条数
     */
    public static int page_num = 100;




}
