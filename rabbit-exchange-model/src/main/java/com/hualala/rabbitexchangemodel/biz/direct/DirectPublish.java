package com.hualala.rabbitexchangemodel.biz.direct;

import com.hualala.core.domain.Employee;
import com.hualala.rabbitexchangemodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectPublish {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void publishDirectOne(Employee employee) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE, RabbitMqConfig.DIRECT_ONE_ROUTING_KEY,employee);
        log.info("发送 publishDirectOne DIRECT_EXCHANGE 成功{}", RabbitMqConfig.DIRECT_ONE_ROUTING_KEY);
    }
    public void publishDirectTwo(Employee employee) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE, RabbitMqConfig.DIRECT_TWO_ROUTING_KEY,employee);
        log.info("发送 publishDirectTwo DIRECT_EXCHANGE 成功{}", RabbitMqConfig.DIRECT_TWO_ROUTING_KEY);
    }
}
