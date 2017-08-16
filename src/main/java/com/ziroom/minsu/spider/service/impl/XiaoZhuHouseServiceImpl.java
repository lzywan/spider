package com.ziroom.minsu.spider.service.impl;

import com.github.pagehelper.PageHelper;
import com.ziroom.minsu.spider.service.XiaoZhuHouseService;
import com.ziroom.minsu.spider.domain.XiaoZhuHouseInfo;
import com.ziroom.minsu.spider.mapper.XiaoZhuHouseInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>TODO</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月16日 09:58
 * @since 1.0
 */
@Service
public class XiaoZhuHouseServiceImpl implements XiaoZhuHouseService {

    private final Logger LOGGER = LoggerFactory.getLogger(XiaoZhuHouseServiceImpl.class);

    @Autowired
    private XiaoZhuHouseInfoMapper xiaoZhuHouseInfoMapper;

    @Override
    public XiaoZhuHouseInfo findInfoById(int id) {
        return xiaoZhuHouseInfoMapper.selectByPrimaryKey(id);
    }

    //开启事物
    @Transactional
    @Override
    public int save(XiaoZhuHouseInfo xiaoZhuHouseInfo) {
        int insert = xiaoZhuHouseInfoMapper.insert(xiaoZhuHouseInfo);
        if (insert == 1){
            throw new RuntimeException("测试异常");
        }
        return insert;

    }

    @Override
    public List<XiaoZhuHouseInfo> findByPage(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<XiaoZhuHouseInfo> xiaoZhuHouseInfos = xiaoZhuHouseInfoMapper.selectByPage();
        LOGGER.info("参数size={}",xiaoZhuHouseInfos.size());
        return xiaoZhuHouseInfos;
    }

    @Override
    public List<XiaoZhuHouseInfo> findByPageV2(int pageNum, int pageSize) {

        List<XiaoZhuHouseInfo> xiaoZhuHouseInfos = xiaoZhuHouseInfoMapper.selectByPage(pageNum,pageSize);
        LOGGER.info("参数findByPageV2 size={}",xiaoZhuHouseInfos.size());
        return xiaoZhuHouseInfos;
    }


}
