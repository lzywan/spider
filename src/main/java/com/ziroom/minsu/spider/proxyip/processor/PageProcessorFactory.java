package com.ziroom.minsu.spider.proxyip.processor;

import com.ziroom.minsu.spider.proxyip.entity.enums.ProxyipSiteEnum;
import com.ziroom.minsu.spider.proxyip.processor.pageprocessor.Ip181dailiPageProcessor;
import com.ziroom.minsu.spider.proxyip.processor.pageprocessor.KuaidailiPageProcessor;
import com.ziroom.minsu.spider.proxyip.processor.pageprocessor.XicidailiPageProcessor;
import us.codecraft.webmagic.processor.PageProcessor;



/**
 * 
 * <p>PageProcessor定制工厂</p>
 * 
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * 
 * @author zhangyl
 * @since 1.0
 * @version 1.0
 */
public class PageProcessorFactory {
	
	/**
	 * 
	 * 获取PageProcessor
	 *
	 * @author zhangyl
	 * @created 2017年7月5日 下午5:28:13
	 *
	 * @param proxyipSiteEnum
	 * @return
	 * @throws Exception 
	 */
	public static PageProcessor createPageProcessor(ProxyipSiteEnum proxyipSiteEnum) throws Exception{
		
		if (ProxyipSiteEnum.KUAI_DAILI.equals(proxyipSiteEnum)) {
			return new KuaidailiPageProcessor();
		} else if (ProxyipSiteEnum.XICI_DAILI.equals(proxyipSiteEnum)) {
			return new XicidailiPageProcessor();
		} else if (ProxyipSiteEnum.IP_181_DAILI.equals(proxyipSiteEnum)) {
			return new Ip181dailiPageProcessor();
		}
		
		throw new Exception("暂无相应的PageProcessor！");
	}
    
}
