package com.ziroom.minsu.spider.service;

import com.ziroom.minsu.spider.core.utils.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    public static final String REDIS_PRE = "minsu:redis:spider:";

    private static final int MAX_WAIT_LOCK_TIME_OUT = 3600;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取redis锁
     *
     * @param
     * @return
     * @author zhangyl2
     * @created 2017年11月17日 11:46
     */
    public boolean getDistributedLock(String key, long timeout) {

        String lockKey = generateLockKey(key);
        long now = System.currentTimeMillis();

        // setNX
        // key不存在时设置成功，原子操作
        if (redisTemplate.getConnectionFactory()
                .getConnection()
                .setNX(lockKey.getBytes(), String.valueOf(now).getBytes())) {

            redisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
            LOGGER.info("add RedisLock[" + lockKey + "]");
            return true;

        } else {
            // 一小时删除锁
            Object o = redisTemplate.opsForValue().get(lockKey);
            if (!Check.NuNObj(o) && o instanceof Long) {
                if (now - (int) o > MAX_WAIT_LOCK_TIME_OUT * 1000) {
                    redisTemplate.delete(lockKey);
                }
            } else {
                redisTemplate.delete(lockKey);
            }
        }

        return false;
    }

    public void releaseDistributedLock(String key) {
        String lockKey = generateLockKey(key);
        redisTemplate.delete(lockKey);
    }

    private static String generateLockKey(String key) {
        return String.format(REDIS_PRE + "lock:%s", key);
    }

}

