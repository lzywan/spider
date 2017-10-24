package com.ziroom.minsu.spider.config.mq;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


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
 * @Date Created in 2017年08月22日 10:25
 * @since 1.0
 */
@Configuration
public class RabbitMqConfiguration {

    final static String queueName = "minsu_order_syncLock";

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }

    public static String lockMqName;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${order.lock.mq.name}")
    public void setLockMqName(String lockMqName) {
        this.lockMqName = lockMqName;
    }

    @PostConstruct
    public void init() {
        if (amqpAdmin.getQueueProperties(lockMqName) == null) {
            Queue queue = new Queue(lockMqName);
            amqpAdmin.declareQueue(queue);
        }
    }

}
