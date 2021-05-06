package com.hualala.rabbitmqdelayqueue.biz;


import com.hualala.core.domain.Employee;
import com.hualala.rabbitmqdelayqueue.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DelayQueuePublish {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void publish(Employee employee) {
            //设置延迟队列交换机、延迟队列路由键,消息实体并且发送消息
            //消息延迟时间设置为10秒,队列的x-message-ttl是设置30秒,以最小时间为准,即发送消息时的10秒
            rabbitTemplate.convertAndSend(RabbitMqConfig.DELAY_EXCHANGE_PRE, RabbitMqConfig.DELAY_ROUTING_KEY_PRE, employee, m -> {
                m.getMessageProperties().setExpiration("10000");
                return m;
            });
          log.info("延迟队列消息发送成功,消息：{},发送时间：{}", employee, LocalDateTime.now());
    }
}
