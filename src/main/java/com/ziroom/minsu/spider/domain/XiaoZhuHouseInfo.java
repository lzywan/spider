package com.ziroom.minsu.spider.domain;

public class XiaoZhuHouseInfo {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 房源编号
     */
    private String houseSn;

    /**
     * 房东编号
     */
    private String landlordSn;

    /**
     * 评价数量
     */
    private Integer evaluateNum;

    /**
     * 出租类型
     */
    private Integer rentType;

    /**
     * 居室
     */
    private Integer houseType;

    /**
     * 房源详情url
     */
    private String detailUrl;

    /**
     * 非详细地址
     */
    private String noDetailAddress;

    /**
     * 详细户型
     */
    private String houseTypeDetail;

    /**
     * 房源面积
     */
    private Integer houseArea;

    /**
     * 房源面积单位
     */
    private String houseAreaUnit;

    /**
     * 宜住人数
     */
    private Integer liveNum;

    /**
     * 床位信息
     */
    private String bedInfos;

    /**
     * 内部情况
     */
    private String insideCase;

    /**
     * 交通情况
     */
    private String transportationCase;

    /**
     * 周边情况
     */
    private String aroundCase;

    /**
     * 配置设施
     */
    private String facilityCase;

    /**
     * 入住须知
     */
    private String roomNotes;

    /**
     * 城市编码
     */
    private String cityCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHouseSn() {
        return houseSn;
    }

    public void setHouseSn(String houseSn) {
        this.houseSn = houseSn == null ? null : houseSn.trim();
    }

    public String getLandlordSn() {
        return landlordSn;
    }

    public void setLandlordSn(String landlordSn) {
        this.landlordSn = landlordSn == null ? null : landlordSn.trim();
    }

    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public Integer getHouseType() {
        return houseType;
    }

    public void setHouseType(Integer houseType) {
        this.houseType = houseType;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl == null ? null : detailUrl.trim();
    }

    public String getNoDetailAddress() {
        return noDetailAddress;
    }

    public void setNoDetailAddress(String noDetailAddress) {
        this.noDetailAddress = noDetailAddress == null ? null : noDetailAddress.trim();
    }

    public String getHouseTypeDetail() {
        return houseTypeDetail;
    }

    public void setHouseTypeDetail(String houseTypeDetail) {
        this.houseTypeDetail = houseTypeDetail == null ? null : houseTypeDetail.trim();
    }

    public Integer getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(Integer houseArea) {
        this.houseArea = houseArea;
    }

    public String getHouseAreaUnit() {
        return houseAreaUnit;
    }

    public void setHouseAreaUnit(String houseAreaUnit) {
        this.houseAreaUnit = houseAreaUnit == null ? null : houseAreaUnit.trim();
    }

    public Integer getLiveNum() {
        return liveNum;
    }

    public void setLiveNum(Integer liveNum) {
        this.liveNum = liveNum;
    }

    public String getBedInfos() {
        return bedInfos;
    }

    public void setBedInfos(String bedInfos) {
        this.bedInfos = bedInfos == null ? null : bedInfos.trim();
    }

    public String getInsideCase() {
        return insideCase;
    }

    public void setInsideCase(String insideCase) {
        this.insideCase = insideCase == null ? null : insideCase.trim();
    }

    public String getTransportationCase() {
        return transportationCase;
    }

    public void setTransportationCase(String transportationCase) {
        this.transportationCase = transportationCase == null ? null : transportationCase.trim();
    }

    public String getAroundCase() {
        return aroundCase;
    }

    public void setAroundCase(String aroundCase) {
        this.aroundCase = aroundCase == null ? null : aroundCase.trim();
    }

    public String getFacilityCase() {
        return facilityCase;
    }

    public void setFacilityCase(String facilityCase) {
        this.facilityCase = facilityCase == null ? null : facilityCase.trim();
    }

    public String getRoomNotes() {
        return roomNotes;
    }

    public void setRoomNotes(String roomNotes) {
        this.roomNotes = roomNotes == null ? null : roomNotes.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    @Override
    public String toString() {
        return "XiaoZhuHouseInfo{" +
                "id=" + id +
                ", houseSn='" + houseSn + '\'' +
                ", landlordSn='" + landlordSn + '\'' +
                ", evaluateNum=" + evaluateNum +
                ", rentType=" + rentType +
                ", houseType=" + houseType +
                ", detailUrl='" + detailUrl + '\'' +
                ", noDetailAddress='" + noDetailAddress + '\'' +
                ", houseTypeDetail='" + houseTypeDetail + '\'' +
                ", houseArea=" + houseArea +
                ", houseAreaUnit='" + houseAreaUnit + '\'' +
                ", liveNum=" + liveNum +
                ", bedInfos='" + bedInfos + '\'' +
                ", insideCase='" + insideCase + '\'' +
                ", transportationCase='" + transportationCase + '\'' +
                ", aroundCase='" + aroundCase + '\'' +
                ", facilityCase='" + facilityCase + '\'' +
                ", roomNotes='" + roomNotes + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}