package com.hualala.rabbitmqackmodel.biz.auto;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Order;
import com.hualala.rabbitmqackmodel.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author cheng
 */
@Slf4j
@Component
public class AutoAckConsumer {

    @RabbitListener(queues = RabbitMqConfig.AUTO_ACKNOWLEDGE_QUEUE, containerFactory = "singleListenerContainerAuto")
    public void consume(@Payload Order order){
        try {
            log.info("自动确认消费模式,消费者监听消费消息:{} ", JSON.toJSONString(order));
            //模拟程序异常后,消息丢失不会重试
            double result = 1 / 0;
        } catch (Exception e) {
            log.error("自动确认消费模式,消费者监听消费消息:{},异常", order, e);
        }
    }
}
