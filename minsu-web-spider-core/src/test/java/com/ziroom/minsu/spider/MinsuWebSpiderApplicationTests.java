package com.ziroom.minsu.spider;

import com.ziroom.minsu.spider.core.utils.ValueUtil;
import com.ziroom.minsu.spider.domain.XiaoZhuHouseInfo;
import com.ziroom.minsu.spider.mapper.NetProxyIpPortMapper;
import com.ziroom.minsu.spider.service.ProxyIpPipelineService;
import com.ziroom.minsu.spider.service.XiaoZhuHouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinsuWebSpiderApplication.class)
@ActiveProfiles("dev")
@EnableAsync
public class MinsuWebSpiderApplicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(MinsuWebSpiderApplicationTests.class);

	@Autowired
	private XiaoZhuHouseService xiaoZhuHouseService;

	@Autowired
	private ProxyIpPipelineService proxyipPipeline;

	@Autowired
	private NetProxyIpPortMapper netProxyIpPortMapper;


	@Test
	public void testList(){
		List<XiaoZhuHouseInfo> byPage = xiaoZhuHouseService.findByPage(4, 10);
		byPage.forEach(System.out::println);
	}

//	@Test
//	public void testAsync(){
//		for (int i = 0; i < 100; i++) {
//			proxyipPipeline.doTask(i);
//		}
//		LOGGER.info("finished.");
//	}



	@Test
	public void testSelectNetProxyIpByPage(){

//		int count = netProxyIpPortMapper.countNetProxyIp();
//
//		List<NetProxyIpPort> netProxyIpPorts = netProxyIpPortMapper.selectNetProxyIpByPage(100,100);
//		LOGGER.info("size:{}",netProxyIpPorts.size());

		int page = ValueUtil.getPage(0,100);
		LOGGER.info("page:{}",page);

	}

}
