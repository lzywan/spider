package com.ziroom.minsu.spider.mapper;

import com.ziroom.minsu.spider.domain.NetProxyIpPort;

import java.util.List;

public interface NetProxyIpPortMapper {

    int saveNetProxyIp(NetProxyIpPort netProxyIpPort);

    List<NetProxyIpPort> listNetProxyIp();

}