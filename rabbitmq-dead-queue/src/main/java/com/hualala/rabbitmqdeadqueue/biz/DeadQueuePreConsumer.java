package com.hualala.rabbitmqdeadqueue.biz;

import com.hualala.core.domain.Employee;
import com.hualala.rabbitmqdeadqueue.config.RabbitMqConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class DeadQueuePreConsumer {

    @RabbitListener(queues = RabbitMqConfig.DIRECT_QUEUE_DEAD_PRE, containerFactory = "singleListenerContainerManual")
    public void consumeMsg(@Payload Employee employee, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            log.info("普通队列-消费者,监听到消息：{},准备处理业务逻辑。", employee);
            double result = 1 / 0;
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("普通队列-消费者,监听到消息：{},发生异常,消息不再归入队列中,转向死信队列,异常e：", employee, e);
            //channel.basicNack(tag, false, false);
            channel.basicReject(tag, false);
        }
    }
}
