package com.ziroom.minsu.spider.config.mq;

import com.ziroom.minsu.spider.core.result.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>消息生产者</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月22日 11:14
 * @since 1.0
 */
@Component
public class RabbitMqSender {

    @Autowired
    private AmqpTemplate template;

    public void send(Result msg){
        template.convertAndSend(RabbitMqConfiguration.queueName,msg);
    }

}
