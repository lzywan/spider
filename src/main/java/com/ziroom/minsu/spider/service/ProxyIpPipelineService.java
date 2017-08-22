package com.ziroom.minsu.spider.service;

import us.codecraft.webmagic.ResultItems;


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

}
