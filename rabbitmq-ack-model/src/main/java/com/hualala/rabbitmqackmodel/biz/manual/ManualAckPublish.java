package com.hualala.rabbitmqackmodel.biz.manual;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Order;
import com.hualala.rabbitmqackmodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author cheng
 */
@Slf4j
@Component
public class ManualAckPublish {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void publish(Order order) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.MANUAL_ACKNOWLEDGE_EXCHANGE,RabbitMqConfig.MANUAL_ACKNOWLEDGE_ROUTING_KEY,order);
        log.info("确认消费模式为手动确认机制,发送消息:{}", JSON.toJSONString(order));
    }
}
