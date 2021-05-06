package com.hualala.rabbitexchangemodel.config;

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


    public static final String FANOUT_ONE_QUEUE = "fanout.one.queue";
    public static final String FANOUT_TWO_QUEUE = "fanout.two.queue";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";

    public static final String DIRECT_ONE_QUEUE = "direct.one.queue";
    public static final String DIRECT_TWO_QUEUE = "direct.two.queue";
    public static final String DIRECT_ONE_ROUTING_KEY = "direct.routing.key.one";
    public static final String DIRECT_TWO_ROUTING_KEY = "direct.routing.key.two";
    public static final String DIRECT_EXCHANGE = "direct.exchange";


    public static final String TOPIC_ONE_QUEUE = "topic.one.queue";
    public static final String TOPIC_TWO_QUEUE = "topic.two.queue";
    public static final String TOPIC_ONE_ROUTING_KEY = "topic.routing.key.*";
    public static final String TOPIC_TWO_ROUTING_KEY = "topic.routing.key.#";
    public static final String TOPIC_EXCHANGE = "topic.exchange";


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
        //设置并发消费者实例的初始数量10
        factory.setConcurrentConsumers(10);
        //设置并发消费者实例的最大数量15
        factory.setMaxConcurrentConsumers(15);
        //设置并发消费者实例中每个实例拉取的消息数量10
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
    public Queue fanoutQueueOne() {
        return new Queue(FANOUT_ONE_QUEUE, true);
    }


    @Bean
    public Queue fanoutQueueTwo() {
        return new Queue(FANOUT_TWO_QUEUE, true);
    }



    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE, true, false);
    }


    @Bean
    public Binding fanoutBindingOne() {
        return BindingBuilder.bind(fanoutQueueOne()).to(fanoutExchange());
    }


    @Bean
    public Binding fanoutBindingTwo() {
        return BindingBuilder.bind(fanoutQueueTwo()).to(fanoutExchange());
    }


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }


    @Bean
    public Queue directQueueOne() {
        return new Queue(DIRECT_ONE_QUEUE, true);
    }


    @Bean
    public Queue directQueueTwo() {
        return new Queue(DIRECT_TWO_QUEUE, true);
    }


    @Bean
    public Binding directBindingOne() {
        return BindingBuilder.bind(directQueueOne()).to(directExchange()).with(DIRECT_ONE_ROUTING_KEY);
    }


    @Bean
    public Binding directBindingTwo() {
        return BindingBuilder.bind(directQueueTwo()).to(directExchange()).with(DIRECT_TWO_ROUTING_KEY);
    }


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }


    @Bean
    public Queue topicQueueOne() {
        return new Queue(TOPIC_ONE_QUEUE, true);
    }


    @Bean
    public Queue topicQueueTwo() {
        return new Queue(TOPIC_TWO_QUEUE, true);
    }


    @Bean
    public Binding topicBindingOne() {
        return BindingBuilder.bind(topicQueueOne()).to(topicExchange()).with(TOPIC_ONE_ROUTING_KEY);
    }


    @Bean
    public Binding topicBindingTwo() {
        return BindingBuilder.bind(topicQueueTwo()).to(topicExchange()).with(TOPIC_TWO_ROUTING_KEY);
    }

}
