package com.hualala.rabbitmqpriorityqueue.biz;


import com.hualala.core.domain.Employee;
import com.hualala.rabbitmqpriorityqueue.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PriorityPublish {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(Employee employee, Integer priority) {
        rabbitTemplate.convertAndSend(RabbitmqConfig.PRIORITY_EXCHANGE, RabbitmqConfig.PRIORITY_ROUTING_KEY, employee, m -> {
            m.getMessageProperties().setPriority(priority);
            m.getMessageProperties().setHeader("x-attach-id",55555);
            return m;
        });
        log.info("优先级队列发送消息:{},优先级为:{}", employee, priority);
    }

}
