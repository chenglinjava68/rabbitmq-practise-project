package com.hualala.rabbitmqbasic.biz;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Order;
import com.hualala.rabbitmqbasic.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


/**
 * @author cheng
 */
@Component
@Slf4j
public class BasicConsumer {

    @RabbitListener(queues = RabbitMqConfig.BASIC_QUEUE, containerFactory = "singleListenerContainer")
    public void consume(@Payload Order order) {
        try {
            log.info("消费者监听消费到消息:{}", JSON.toJSONString(order));
        } catch (Exception e) {
            log.error("消费者消费发生异常:{}", e);
        }
    }
}
