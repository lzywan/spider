package com.ziroom.minsu.spider.proxyip.entity.enums;

/**
 * 
 * <p>代理ip网站</p>
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
public enum ProxyipSiteEnum {
	
	KUAI_DAILI("快代理", "http://www.kuaidaili.com/free"),
	XICI_DAILI("西刺代理", "http://www.xicidaili.com/nn/"),
	IP_181_DAILI("ip181代理", "http://www.ip181.com/");

	/**
	 * @param name
	 * @param url
	 */
	private ProxyipSiteEnum(String siteName, String url) {
		this.siteName = siteName;
		this.url = url;
	}

	/** 名称 */
	private String siteName;

	/** 地址 */
	private String url;

	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * @param siteName the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
