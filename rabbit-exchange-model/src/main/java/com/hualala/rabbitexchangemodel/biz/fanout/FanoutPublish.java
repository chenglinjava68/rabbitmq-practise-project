package com.hualala.rabbitexchangemodel.biz.fanout;


import com.hualala.core.domain.Employee;
import com.hualala.rabbitexchangemodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FanoutPublish {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void publishFanout(Employee employee) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.FANOUT_EXCHANGE,"",employee);
        log.info("消息模型fanoutExchange-生产者-发送消息：{} ", employee);
    }
}
