package com.hualala.rabbitmqdelayqueue.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Configuration
public class RabbitMqConfig {


    public static final String DELAY_QUEUE_PRE = "mq.direct.queue.delay.pre";

    public static final String DELAY_EXCHANGE_PRE = "mq.direct.exchange.delay.pre";

    public static final String DELAY_ROUTING_KEY_PRE = "mq.routing.key.delay.pre";

    public static final String DELAY_QUEUE = "mq.delay.queue";

    public static final String DELAY_EXCHANGE = "mq.delay.exchange";

    public static final String DELAY_ROUTING_KEY = "mq.delay.routing.key";


    @Autowired
    private CachingConnectionFactory connectionFactory;


    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;


    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        //定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置容器工厂所用的实例
        factory.setConnectionFactory(connectionFactory);
        //设置消息在传输中的格式，在这里采用JSON的格式进行传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置并发消费者实例的初始数量。在这里为1个
        factory.setConcurrentConsumers(1);
        //设置并发消费者实例的最大数量。在这里为1个
        factory.setMaxConcurrentConsumers(1);
        //设置并发消费者实例中每个实例拉取的消息数量-在这里为1个
        factory.setPrefetchCount(1);
        return factory;
    }


    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        //定义消息监听器所在的容器工厂
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置容器工厂所用的实例
        factoryConfigurer.configure(factory, connectionFactory);
        //设置消息在传输中的格式。在这里采用JSON的格式进行传输
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置消息的确认消费模式。在这里为NONE，表示不需要确认消费
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        //设置并发消费者实例的初始数量。在这里为10个
        factory.setConcurrentConsumers(10);
        //设置并发消费者实例的最大数量。在这里为15个
        factory.setMaxConcurrentConsumers(15);
        //设置并发消费者实例中每个实例拉取的消息数量。在这里为10个
        factory.setPrefetchCount(10);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        //设置发送消息后进行确认
        connectionFactory.setPublisherConfirms(true);
        //设置发送消息后返回确认信息
        connectionFactory.setPublisherReturns(true);
        //构造发送消息组件实例对象
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //发送消息后，如果发送成功，则输出“消息发送成功”的反馈信息
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("消息发送成功:correlationData {},ack {},cause {}", correlationData, ack, cause);
            }
        });
        //发送消息后，如果发送失败，则输出消息发送失败-消息丢失的反馈信息
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.warn("消息异常:exchange {},routeKey {},replyCode {},replyText {},message:{}", exchange, routingKey, replyCode, replyText, message);
            }
        });
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue delayQueuePre() {
        Map<String, Object> args = new HashMap<String, Object>(16);
        //设置消息过期之后的死信交换机(真正消费的交换机)
        args.put("x-dead-letter-exchange", DELAY_EXCHANGE);
        //设置消息过期之后死信队列的路由(真正消费的路由)
        args.put("x-dead-letter-routing-key", DELAY_ROUTING_KEY);
        //设定消息的TTL，单位为ms，在这里指的是30s
        args.put("x-message-ttl", 30000);
        return new Queue(DELAY_QUEUE_PRE, true, false, false, args);
    }


    @Bean
    public DirectExchange delayExchangePre() {
        return new DirectExchange(DELAY_EXCHANGE_PRE, true, false);
    }


    @Bean
    public Binding delayBindingPre() {
        return BindingBuilder.bind(delayQueuePre()).to(delayExchangePre()).with(DELAY_ROUTING_KEY_PRE);
    }


    @Bean
    public Queue delayQueue() {
        return new Queue(DELAY_QUEUE, true);
    }


    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE, true, false);
    }


    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_ROUTING_KEY);
    }
}
