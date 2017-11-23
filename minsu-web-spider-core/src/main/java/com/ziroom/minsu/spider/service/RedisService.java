package com.ziroom.minsu.spider.service;

import com.ziroom.minsu.spider.core.utils.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private static Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    private static final String REDIS_PRE = "minsu:redis:spider:";

    private static final int MAX_WAIT_LOCK_TIME_OUT = 3600;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取redis锁
     *
     * @param
     * @return
     * @author zhangyl2
     * @created 2017年11月17日 11:46
     */
    public boolean getDistributedLock(String key) {

        String lockKey = generateLockKey(key);
        long now = System.currentTimeMillis();

        // setNX
        // 仅当key不存在时设置成功
        if (redisTemplate.getConnectionFactory()
                .getConnection()
                .setNX(lockKey.getBytes(), String.valueOf(now + MAX_WAIT_LOCK_TIME_OUT * 1000).getBytes())) {

            LOGGER.info("add RedisLock[" + lockKey + "]");
            return true;

        } else {
            // key存在
            Object o = redisTemplate.opsForValue().get(lockKey);
            // 判断是否过期
            if (!Check.NuNObj(o) && (long) o < now) {
                String originValue = new String(redisTemplate.getConnectionFactory()
                        .getConnection()
                        .getSet(lockKey.getBytes(), String.valueOf(now + MAX_WAIT_LOCK_TIME_OUT * 1000).getBytes()));

                if (originValue.equals(o)) {
                    return true;
                }

            }
        }

        return false;
    }

    public void releaseDistributedLock(String key) {
        String lockKey = generateLockKey(key);
        redisTemplate.delete(lockKey);
    }

    private String generateLockKey(String key) {
        return String.format(REDIS_PRE + "lock:%s", key);
    }

}

