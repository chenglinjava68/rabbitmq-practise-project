package com.hualala.rabbitmqdeadqueue.biz;

import com.hualala.core.domain.Employee;
import com.hualala.rabbitmqdeadqueue.config.RabbitMqConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DeadQueueConsumer {

    @Autowired
    private DeadQueuePublish ordinaryPublisher;

    private Boolean dynamicRepairSign = false;

    /** 可以注释掉监听,在rabbitmq管理后台取出该消息,等到异常处理完之后把该消息丢回原先的队列进行处理。**/
    @RabbitListener(queues = RabbitMqConfig.DEAD_QUEUE, containerFactory = "singleListenerContainerAuto")
    public void consumeMsg(@Payload Employee employee, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        log.info("死信队列监听到消息：{}", employee);
        if (dynamicRepairSign) {
            //修复完异常之后发送消息到原先队列进行消费
            ordinaryPublisher.publish(employee);
            channel.basicAck(tag, false);
        } else {
            channel.basicReject(tag, true);
        }
    }
}
