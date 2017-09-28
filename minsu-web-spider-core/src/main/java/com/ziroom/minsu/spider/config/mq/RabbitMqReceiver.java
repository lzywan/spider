package com.ziroom.minsu.spider.config.mq;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.minsu.spider.core.result.Result;
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
public class RabbitMqReceiver {


    /*@RabbitListener(queues = RabbitMqConfiguration.queueName)
    @RabbitHandler*/
    public void receiveMessage(String msg) {
        System.out.println("Received <" + msg + ">");

    }
}
