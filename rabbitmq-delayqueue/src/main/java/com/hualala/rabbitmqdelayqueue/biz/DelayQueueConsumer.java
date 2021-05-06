package com.hualala.rabbitmqdelayqueue.biz;


import com.hualala.core.domain.Employee;
import com.hualala.rabbitmqdelayqueue.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
public class DelayQueueConsumer {

    @RabbitListener(queues = RabbitMqConfig.DELAY_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeMsg(@Payload Employee employee) {
        try {
            log.info("延迟队列-30s时间到达后,真正消费消息的队列,监听消息：{},当前时间:{}", employee, LocalDateTime.now());
        } catch (Exception e) {
            log.error("延迟队列-30s时间到达后,真正消费消息的队列,监听消息：{},处理发生异常e：", employee, e);
        }
    }
}
