package com.hualala.rabbitmqbasic.biz;


import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Order;
import com.hualala.rabbitmqbasic.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class BasicPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Order order) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.BASIC_EXCHANGE, RabbitMqConfig.BASIC_ROUTING_KEY,order, new CorrelationData(UUID.fastUUID().toString()));
        log.info("生产者发送消息:{}", JSON.toJSONString(order));
    }
}
