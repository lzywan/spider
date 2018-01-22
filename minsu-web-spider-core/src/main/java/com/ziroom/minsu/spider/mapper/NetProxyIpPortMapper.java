package com.ziroom.minsu.spider.mapper;

import com.ziroom.minsu.spider.domain.NetProxyIpPort;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NetProxyIpPortMapper {

    int saveNetProxyIp(NetProxyIpPort netProxyIpPort);

    List<NetProxyIpPort> listNetProxyIp();

<<<<<<< HEAD
    int countNetProxyIp();

    List<NetProxyIpPort> selectNetProxyIpByPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
=======
    int countCheckNetProxyIp();

    List<NetProxyIpPort> selectCheckNetProxyIpByPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
>>>>>>> test

    int updateByPrimaryKeySelective(NetProxyIpPort netProxyIpPort);

    int updateProxyIpUseCount(@Param("ip") String ip,@Param("port") int port);

}