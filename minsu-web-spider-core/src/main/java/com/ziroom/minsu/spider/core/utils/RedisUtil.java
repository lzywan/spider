package com.ziroom.minsu.spider.core.utils;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtil {

    /**
     * redis锁
     *
     * @param
     * @return
     * @author zhangyl2
     * @created 2017年11月17日 11:46
     */
    public static boolean checkDistributed(RedisTemplate redisTemplate, String key, long time) {
        if (redisTemplate.hasKey(key)) {
            return true;
        } else {
            redisTemplate.opsForValue().set(key, "1", time, TimeUnit.SECONDS);
            return false;
        }
    }

}

