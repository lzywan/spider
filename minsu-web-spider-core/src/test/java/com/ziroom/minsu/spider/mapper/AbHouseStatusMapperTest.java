package com.ziroom.minsu.spider.mapper;

import com.ziroom.minsu.spider.MinsuWebSpiderApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AbHouseStatusMapperTest extends MinsuWebSpiderApplicationTests {

    @Autowired
    private AbHouseStatusMapper abHouseStatusMapper;

    @Test
    public void testSelectCountByAbSn(){
        System.out.println(abHouseStatusMapper.selectCountByAbSn("20484281"));
    }
}
