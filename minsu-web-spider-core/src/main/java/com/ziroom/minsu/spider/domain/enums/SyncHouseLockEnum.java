/**
 * @FileName: HouseStatusEnum.java
 * @Package com.ziroom.minsu.valenum.house
 * 
 * @author bushujie
 * @created 2016年4月2日 下午8:56:30
 * 
 * Copyright 2011-2015 asura
 */
package com.ziroom.minsu.spider.domain.enums;

/**
 * 
 * <p>同步状态枚举</p>
 * 
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 * 
 * @author jixd
 * @since 1.0
 * @version 1.0
 */
public enum SyncHouseLockEnum {

	SELF_LOCK(1,"设置不可租"),
	ORDER_LOCK(2,"订单锁定");

	/** code */
	private int code;

	/** 名称 */
	private String name;

	SyncHouseLockEnum(int code, String name){
		 this.code = code;
	     this.name = name;
	}

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
