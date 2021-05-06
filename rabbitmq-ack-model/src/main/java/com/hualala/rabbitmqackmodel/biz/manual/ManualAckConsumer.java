package com.hualala.rabbitmqackmodel.biz.manual;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Order;
import com.hualala.rabbitmqackmodel.config.RabbitMqConfig;
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
public class ManualAckConsumer {

    @RabbitListener(queues = RabbitMqConfig.MANUAL_ACKNOWLEDGE_QUEUE, containerFactory = "singleListenerContainerManual")
    public void consumeMsg(@Payload Order order, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long tag) throws IOException {
        try {
            log.info("手工确认消费模式,消费者监听消费消息,消息投递标记:{},内容为:{}",tag, JSON.toJSONString(order));
            //抛异常,归入使得消息重新归入队列
             double result = 1 / 0;
            //执行完业务逻辑后，手动进行确认消费，其中第一个参数为：消息的分发标识(全局唯一);第二个参数：是否允许批量确认消费
            channel.basicAck(tag, false);
        } catch (Exception e) {
            //第二个参数reueue重新归入队列,true的话会重新归入队列,需要人为地处理此次异常消息,重新归入队列也会继续异常,
            //导致后续得消息无法处理 陷入死循环处理
            //正确做法 设置了消息重试次数，达到了重试上限以后，手动确认，队列删除此消息，并将消息持久化入MySQL并推送报警，进行人工处理和定时任务做补偿
            channel.basicReject(tag, false);
            log.error("手工确认消费模式,消费者监听消费消息:{},消息投递标签:{},发生异常:{}",JSON.toJSONString(order), tag, e);
        }
    }
}
