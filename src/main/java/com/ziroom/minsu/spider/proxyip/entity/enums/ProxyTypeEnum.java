package com.ziroom.minsu.spider.proxyip.entity.enums;

/**
 * 
 * <p>代理类型</p>
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
public enum ProxyTypeEnum {
	
	HTTP(0, "http"),
	HTTPS(1, "https");

	/**
	 * 
	 * @param code
	 * @param type
	 */
	private ProxyTypeEnum(Integer code, String type) {
		this.code = code;
		this.type = type;
	}

	/** code*/
	private Integer code;

	/** 类型 */
	private String type;

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


}
