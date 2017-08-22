package com.ziroom.minsu.spider.config.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    final static String queueName = "minsu.order.syncLock";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }


}
