package com.ziroom.minsu.spider.domain;

import java.util.Date;

public class NetProxyIpPort {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 逻辑fid
     */
    private String fid;

    /**
     * 代理ip
     */
    private String proxyIp;

    /**
     * 代理端口
     */
    private Integer proxyPort;

    /**
     * 代理类型 0：http 1：https
     */
    private Integer proxyType;

    /**
     * 是否有效 0：未生效 1：已生效
     */
    private Integer isValid;

    /**
     * ip来源 0：手动增加 *：网站地址
     */
    private String ipSource;

    /**
     * 使用次数
     */
    private Integer validUsedCount;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后修改时间
     */
    private Date lastModifyDate;

    /**
     * 是否删除 0：否，1：是
     */
    private Integer isDel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid == null ? null : fid.trim();
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp == null ? null : proxyIp.trim();
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public Integer getProxyType() {
        return proxyType;
    }

    public void setProxyType(Integer proxyType) {
        this.proxyType = proxyType;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public String getIpSource() {
        return ipSource;
    }

    public void setIpSource(String ipSource) {
        this.ipSource = ipSource;
    }

    public Integer getValidUsedCount() {
        return validUsedCount;
    }

    public void setValidUsedCount(Integer validUsedCount) {
        this.validUsedCount = validUsedCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}