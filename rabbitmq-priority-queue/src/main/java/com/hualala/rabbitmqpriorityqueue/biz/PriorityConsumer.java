package com.hualala.rabbitmqpriorityqueue.biz;

import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Employee;
import com.hualala.rabbitmqpriorityqueue.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


/**
 * @author cheng
 */
@Slf4j
@Component
public class PriorityConsumer {

    @RabbitListener(queues = RabbitmqConfig.PRIORITY_QUEUE,containerFactory = "listenerContainer")
    public void consume(@Payload Employee employee,@Header(AmqpHeaders.DELIVERY_TAG) Long tag) {
        log.info("优先级队列监听消息:{},deliveryTag {}", JSON.toJSONString(employee),tag);
    }
}
