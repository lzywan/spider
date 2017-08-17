package com.ziroom.minsu.spider;

import com.ziroom.minsu.spider.domain.XiaoZhuHouseInfo;
import com.ziroom.minsu.spider.service.XiaoZhuHouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MinsuWebSpiderApplication.class)
public class MinsuWebSpiderApplicationTests {

	@Autowired
	private XiaoZhuHouseService xiaoZhuHouseService;


	@Test
	public void testList(){
		List<XiaoZhuHouseInfo> byPage = xiaoZhuHouseService.findByPage(4, 10);
		byPage.forEach(System.out::println);
	}

}
