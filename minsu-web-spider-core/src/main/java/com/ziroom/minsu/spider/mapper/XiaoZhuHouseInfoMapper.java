package com.ziroom.minsu.spider.mapper;

import com.ziroom.minsu.spider.domain.XiaoZhuHouseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XiaoZhuHouseInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(XiaoZhuHouseInfo record);

    int insertSelective(XiaoZhuHouseInfo record);

    XiaoZhuHouseInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(XiaoZhuHouseInfo record);

    int updateByPrimaryKey(XiaoZhuHouseInfo record);

    List<XiaoZhuHouseInfo> selectByPage();

    List<XiaoZhuHouseInfo> selectByPage(@Param("pageNum") int pageNum,@Param("pageSize") int pageSize);

}