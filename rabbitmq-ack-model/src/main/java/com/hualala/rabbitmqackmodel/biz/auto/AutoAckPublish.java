package com.hualala.rabbitmqackmodel.biz.auto;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Order;
import com.hualala.rabbitmqackmodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AutoAckPublish {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(Order order) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.AUTO_ACKNOWLEDGE_EXCHANGE,RabbitMqConfig.AUTO_ACKNOWLEDGE_ROUTING_KEY,order);
        log.info("发送消息:{}", JSON.toJSONString(order));
    }
}
