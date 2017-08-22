package com.ziroom.minsu.spider.mapper;

import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NetProxyIpPortMapper {

    int saveNetProxyIp(NetProxyIpPort netProxyIpPort);

    List<NetProxyIpPort> listNetProxyIp();

    List<NetProxyIpPort> selectNetProxyIpByPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    int updateByPrimaryKeySelective(NetProxyIpPort netProxyIpPort);

}