package com.ziroom.minsu.spider.web;

import com.ziroom.minsu.spider.service.XiaoZhuHouseService;
import com.ziroom.minsu.spider.domain.XiaoZhuHouseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
 * @Date Created in 2017年08月15日 11:40
 * @since 1.0
 */
@Controller
public class HelloController {

    private Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private XiaoZhuHouseService xiaoZhuHouseService;

    @RequestMapping("/hello")
    public String index(ModelMap map) {
        // 加入一个属性，用来在模板中读取
        map.addAttribute("host", "hello word");
        // return模板文件的名称，对应src/main/resources/templates/index.html
        return "hello";
    }


    @RequestMapping("/test")
    @ResponseBody
    public XiaoZhuHouseInfo test(){
        LOGGER.info("this is info");
        LOGGER.debug("this is debug");
        LOGGER.error("this is error");
        return xiaoZhuHouseService.findInfoById(41);
    }


    @RequestMapping("/insert")
    @ResponseBody
    public String inser(){
        XiaoZhuHouseInfo xiaoZhuHouseInfo = new XiaoZhuHouseInfo();
        xiaoZhuHouseInfo.setHouseSn("dfdfdfd");
        int save = xiaoZhuHouseService.save(xiaoZhuHouseInfo);
        return "googd";
    }


    @RequestMapping("/list")
    @ResponseBody
    public List<XiaoZhuHouseInfo> list(){
        return xiaoZhuHouseService.findByPage(2,10);
    }

    @RequestMapping("/list2")
    @ResponseBody
    public List<XiaoZhuHouseInfo> list2(){
        return xiaoZhuHouseService.findByPageV2(3,10);
    }

    @RequestMapping("/list3")
    @ResponseBody
    public List<XiaoZhuHouseInfo> list3(){
        return xiaoZhuHouseService.findByPageV2(3,10);
    }
}
