package com.ziroom.minsu.spider.service;

import us.codecraft.webmagic.ResultItems;

import java.util.List;
import java.util.Set;


/**
 *
 * <p>ProxyIpPipelineService</p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * 
 * @author lusp
 * @since 1.0
 * @version 1.0
 */
public interface ProxyIpPipelineService {

	void asyncCheckProxyAndSave(ResultItems resultItems);

	void checkProxyIpAvailable();

	/**
	 * 获取可用ip列表
	 * @author jixd
	 * @created 2017年08月24日 11:29:32
	 * @param
	 * @return
	 */
	List<String> listProxyIp();

	/**
	 * 更新ip使用次数
	 * @author jixd
	 * @created 2017年08月25日 10:30:20
	 * @param
	 * @return
	 */
	int updateProxyIpUseCount(String ip,int port);
}
