package com.hualala.rabbitmqackmodel.config;

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


@Slf4j
@Configuration
public class RabbitMqConfig {


    public static final String AUTO_ACKNOWLEDGE_QUEUE = "auto.acknowledge.direct.queue";

    public static final String AUTO_ACKNOWLEDGE_ROUTING_KEY = "auto.acknowledge.direct.routing.key";

    public static final String AUTO_ACKNOWLEDGE_EXCHANGE = "auto.acknowledge.direct.exchange";

    public static final String MANUAL_ACKNOWLEDGE_QUEUE = "manual.acknowledge.direct.queue";

    public static final String MANUAL_ACKNOWLEDGE_ROUTING_KEY = "manual.acknowledge.direct.routing.key";

    public static final String MANUAL_ACKNOWLEDGE_EXCHANGE = "manual.acknowledge.direct.exchange";



    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

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
    public DirectExchange autoAckDirectExchange() {
        return new DirectExchange(AUTO_ACKNOWLEDGE_EXCHANGE, true, false);
    }


    @Bean
    public Queue autoAckDirectQueue() {
        return new Queue(AUTO_ACKNOWLEDGE_QUEUE, true);
    }

    @Bean
    public Binding autoAckDirectBinding() {
        return BindingBuilder.bind(autoAckDirectQueue()).to(autoAckDirectExchange()).with(AUTO_ACKNOWLEDGE_ROUTING_KEY);
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
    public DirectExchange manualAckDirectExchange() {
        return new DirectExchange(MANUAL_ACKNOWLEDGE_EXCHANGE, true, false);
    }
    @Bean
    public Queue manualAckDirectQueue() {
        return new Queue(MANUAL_ACKNOWLEDGE_QUEUE, true);
    }

    @Bean
    public Binding manualAckDirectBinding() {
        return BindingBuilder.bind(manualAckDirectQueue()).to(manualAckDirectExchange()).with(MANUAL_ACKNOWLEDGE_ROUTING_KEY);
    }
}
