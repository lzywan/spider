package com.ziroom.minsu.spider.core.utils;

import com.ziroom.minsu.spider.domain.constant.HttpConstant;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>httpClient使用工具类</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月18日 14:23
 * @since 1.0
 */
public class HttpClientUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * 有代理的请求
     * @author jixd
     * @created 2017年08月18日 16:06:27
     * @param
     * @return
     */
    public static String sendProxyGet(String url,Map<String,String> headerMap,String ip,int port) throws IOException{
            RequestConfig defaultConfig = getRequestConfig(null, 0);
            //设置默认配置
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultConfig).build();
            HttpGet httpGet = new HttpGet(url);
            //直接在这个位置设置userAgent
            headerMap.put("User-Agent",CalendarDataUtil.getUserAgent());
            initHeader(httpGet,headerMap);
            httpGet.setConfig(getRequestConfig(ip,port));
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "UTF-8");
            }finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    /**
     * httpGet请求
     *
     * @param
     * @return
     * @author jixd
     * @created 2017年08月18日 14:42:55
     */
    public static String sendGet(String url, Map<String, String> headerMap) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        initHeader(httpGet,headerMap);
        //设置请求配置
        httpGet.setConfig(getRequestConfig(null,0));
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * httpPost请求
     *
     * @param
     * @return
     * @author jixd
     * @created 2017年08月18日 14:44:16
     */
    public static String sendPost(String url, Map<String, String> param, Map<String, String> headerMap) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        initHeader(httpPost,headerMap);
        CloseableHttpResponse response = null;
        try {
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key:param.keySet()){
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(paramList));
            }
            response = httpclient.execute(httpPost);
            return EntityUtils.toString(response.getEntity(),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 初始化头信息
     * @param httpRequestBase
     * @param headerMap
     */
    private static void initHeader(HttpRequestBase httpRequestBase,Map<String, String> headerMap){
        if (headerMap != null){
            for (String key : headerMap.keySet()){
                httpRequestBase.setHeader(key,headerMap.get(key));
            }
        }
    }

    /**
     * 获取请求连接
     * @author jixd
     * @created 2017年08月18日 15:48:08
     * @param
     * @return
     */
    private static RequestConfig getRequestConfig(String ip,int port){
        RequestConfig.Builder builder = RequestConfig.custom()
                .setSocketTimeout(HttpConstant.socket_time_out)
                .setConnectTimeout(HttpConstant.connect_time_out)
                .setMaxRedirects(HttpConstant.max_redirects)
                .setConnectionRequestTimeout(HttpConstant.connection_request_time_out);
        if (ip != null && !"".equals(ip)){
            HttpHost httpHost = new HttpHost(ip,port);
            builder.setProxy(httpHost);
        }
        return builder.build();
    }

    /**
     * @description: 判断代理ip 是否可用
     * @author: lusp
     * @date: 2017/8/22 13:51
     * @params: URL,ip,port
     * @return:
     */
    public static boolean checkProxyIp(String url, String ip, int port) {
        boolean flag = false;
        CloseableHttpClient httpclient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = getRequestConfig(ip, port);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status >= HttpStatus.SC_OK && status <= HttpStatus.SC_MULTIPLE_CHOICES) {
                flag = true;
            }
        } catch (IOException e) {
            LOGGER.info("【checkProxyIp】该代理ip地址不可用,ip:{},port:{}", ip, port);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public static void main(String[] args) {

//        Map<String,String> paramMap = new HashMap<>();
//        paramMap.put("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3");
//        String s = null;
//        try {
//            s = sendProxyGet("https://zh.airbnb.com/calendar/ical/19534577.ics?s=bc0b6fc6173428bc097d42526fc75988", paramMap, "121.31.150.25", 8123);
//        } catch (IOException e) {
//            e.printStackTrace();
//            LOGGER.info("io");
//        }
//        System.err.println(s);

        List<Boolean> list = new ArrayList<>();
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "111.13.109.27", 80));

        list.add(checkProxyIp(HttpConstant.airbnbUrl, "119.18.234.140", 80));
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "116.85.17.182", 8080));
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "103.233.157.234", 53281));
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "112.5.175.234", 3128));
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "121.201.58.204", 3128));
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "112.95.190.85", 9797));
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "163.125.29.246", 8118));
        list.add(checkProxyIp(HttpConstant.airbnbUrl, "183.62.196.10", 3128));

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        for (Boolean b : list){
            System.out.println(b);
        }


    }
}
