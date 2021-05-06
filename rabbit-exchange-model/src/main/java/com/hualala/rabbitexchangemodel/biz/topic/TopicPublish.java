package com.hualala.rabbitexchangemodel.biz.topic;
import com.hualala.core.domain.Employee;
import com.hualala.rabbitexchangemodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicPublish {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publicTopicMsg(Employee employee, String routingKey) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE,routingKey,employee);
        log.info("TopicExchange 发布消息成功 TOPIC_EXCHANGE{} routingKey{} ",RabbitMqConfig.TOPIC_EXCHANGE,routingKey);
    }
}
