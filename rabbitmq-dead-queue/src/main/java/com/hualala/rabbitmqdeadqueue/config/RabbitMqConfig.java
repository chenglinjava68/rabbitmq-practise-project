package com.hualala.rabbitmqdeadqueue.config;

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


    public static final String DIRECT_QUEUE_DEAD_PRE = "direct.queue.dead.pre";
    public static final String DIRECT_EXCHANGE_DEAD_PRE = "direct.exchange.dead.pre";
    public static final String DIRECT_ROUTING_KEY_DEAD_PRE = "direct.routing.key.dead.pre";

    public static final String DEAD_QUEUE = "mq.dead.queue";
    public static final String DEAD_EXCHANGE = "mq.dead.exchange";
    public static final String DEAD_ROUTING_KEY = "mq.dead.routing.key";


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

    /**
     * 确认消费模式为自动确认机制-AUTO,采用直连传输directExchange消息模型
     */
    @Bean
    public SimpleRabbitListenerContainerFactory singleListenerContainerAuto() {
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
        //确认消费模式为自动确认机制
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }


    @Bean
    public SimpleRabbitListenerContainerFactory singleListenerContainerManual() {
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
        //确认消费模式为自动确认机制
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
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
    public Queue directDeadPreQueue() {
        Map<String, Object> args = new HashMap<String, Object>(8);
        //设死信交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //死信队列的路由
        args.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
        return new Queue(DIRECT_QUEUE_DEAD_PRE, true, false, false, args);
    }

    @Bean
    public DirectExchange directDeadPreExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_DEAD_PRE, true, false);
    }


    @Bean
    public Binding directDeadPreBinding() {
        return BindingBuilder.bind(directDeadPreQueue()).to(directDeadPreExchange()).with(DIRECT_ROUTING_KEY_DEAD_PRE);
    }


    @Bean
    public Queue deadQueue() {
        return new Queue(DEAD_QUEUE, true);
    }


    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE, true, false);
    }


    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING_KEY);
    }
}
