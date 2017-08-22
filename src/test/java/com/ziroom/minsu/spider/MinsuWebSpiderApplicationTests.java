package com.ziroom.minsu.spider;

import com.ziroom.minsu.spider.domain.XiaoZhuHouseInfo;
import com.ziroom.minsu.spider.proxyip.processor.ProxyipPipeline;
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
	private ProxyipPipeline proxyipPipeline;


	@Test
	public void testList(){
		List<XiaoZhuHouseInfo> byPage = xiaoZhuHouseService.findByPage(4, 10);
		byPage.forEach(System.out::println);
	}

	@Test
	public void testAsync(){
		for (int i = 0; i < 100; i++) {
			proxyipPipeline.doTask(i);
		}
		LOGGER.info("finished.");
	}

}
