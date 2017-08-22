package com.ziroom.minsu.spider.config.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p>消费者</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 10:51
 * @since 1.0
 */
@Component
public class Receiver {


    @RabbitListener(queues = RabbitMqConfiguration.queueName)
    @RabbitHandler
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");

    }
}
