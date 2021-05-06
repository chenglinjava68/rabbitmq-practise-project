package com.hualala.rabbitexchangemodel.biz.direct;


import com.hualala.core.domain.Employee;
import com.hualala.rabbitexchangemodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DirectConsumer {


    @RabbitListener(queues = RabbitMqConfig.DIRECT_ONE_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeDirectMsgOne(@Payload Employee employee) {
        try {
            log.info("消息模型directExchange  one消费者,监听消费到消息：{} ", employee);
        } catch (Exception e) {
            log.error("消息模型directExchange one消费者,监听消费发生异常:", e);
        }
    }


    @RabbitListener(queues = RabbitMqConfig.DIRECT_TWO_QUEUE, containerFactory = "singleListenerContainer")
    public void consumeDirectMsgTwo(@Payload Employee employee) {
        try {
            log.info("消息模型directExchange two 消费者,监听消费到消息:{} ", employee);
        } catch (Exception e) {
            log.error("消息模型directExchange two消费者,监听消费发生异常：", e);
        }
    }
}
