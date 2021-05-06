package com.hualala.rabbitmqdeadqueue.biz;

import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Employee;
import com.hualala.rabbitmqdeadqueue.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class DeadQueuePublish {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void publish(Employee employee) {
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE_DEAD_PRE, RabbitMqConfig.DIRECT_ROUTING_KEY_DEAD_PRE, employee);
            log.info("普通队列生产者,发送消息：{}", JSON.toJSONString(employee));
        } catch (Exception e) {
            log.error("普通队列生产者,发送消息异常,消息：{},异常：", employee, e);
        }
    }
}
