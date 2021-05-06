package com.hualala.rabbitexchangemodel.biz.topic;

import com.hualala.core.domain.Employee;
import com.hualala.rabbitexchangemodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicConsumer {


    @RabbitListener(queues = RabbitMqConfig.TOPIC_ONE_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeTopicMsgOne(@Payload Employee employee) {
        try {
            log.info("消息模型topicExchange-*-消费者,监听消费到消息：{} ", employee);
        } catch (Exception e) {
            log.error("消息模型topicExchange-*-消费者,监听消费发生异常：", e);
        }
    }

    @RabbitListener(queues = RabbitMqConfig.TOPIC_TWO_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeTopicMsgTwo(@Payload Employee employee) {
        try {
            log.info("消息模型topicExchange-#-消费者,监听消费到消息：{} ", employee);
        } catch (Exception e) {
            log.error("消息模型topicExchange-#-消费者,监听消费发生异常：", e);
        }
    }
}
