package com.hualala.rabbitexchangemodel.biz.fanout;


import com.hualala.core.domain.Employee;
import com.hualala.rabbitexchangemodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FanoutConsumer {


    @RabbitListener(queues = RabbitMqConfig.FANOUT_ONE_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeFanoutMsgOne(@Payload Employee employee) {
        try {
            log.info("消息模型fanoutExchange-one-消费者-监听消费到消息：{} ", employee);
        } catch (Exception e) {
            log.error("消息模型-消费者-发生异常：", e);
        }
    }


    @RabbitListener(queues = RabbitMqConfig.FANOUT_TWO_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeFanoutMsgTwo(@Payload Employee employee) {
        try {
            log.info("消息模型fanoutExchange-two-消费者-监听消费到消息：{} ", employee);
        } catch (Exception e) {
            log.error("消息模型-消费者-发生异常：", e);
        }
    }
}
