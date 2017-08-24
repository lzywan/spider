package com.ziroom.minsu.spider.core.utils;

import com.ziroom.minsu.spider.domain.constant.HttpConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
    public static String sendProxyGet(String url,Map<String,String> headerMap,String ip,int port){
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            initHeader(httpGet,headerMap);
            httpGet.setConfig(getRequestConfig(ip,port));
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
        }catch (Exception e){
            LOGGER.error("sendProxyGet error={}",e);
        }
        return null;
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
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = getRequestConfig(ip,port);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if(status>=200&&status<=300){
                flag = true;
            }
        } catch (IOException e) {
            LOGGER.info("【checkProxyIp】该代理ip地址不可用,ip:{},port:{}",ip,port);
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

    public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {


        SSLContext context = SSLContexts.custom()
                .loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE)
                .build();


        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(context, NoopHostnameVerifier.INSTANCE))
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);

        HttpHost proxy = new HttpHost("60.255.186.169", 8888);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);


       /* CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setRoutePlanner(routePlanner)
                //.setProxy(new HttpHost("60.255.186.169", 8888))
                .build();*/
        CloseableHttpClient httpclient = HttpClients.custom().build();
        HttpGet httpget = new HttpGet("http://www.ziroom.com/");
        // Request configuration can be overridden at the request level.
        // They will take precedence over the one set at the client level.
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setProxy(new HttpHost("112.86.90.47",8118))
                .build();
        httpget.setConfig(requestConfig);
        httpget.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity, "UTF-8"));



    }
}
