package com.hualala.rabbitmqpriorityqueue.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitmqConfig {

    public static final String PRIORITY_QUEUE = "priority.direct.queue";
    public static final String PRIORITY_EXCHANGE = "priority.direct.exchange";
    public static final String PRIORITY_ROUTING_KEY = "priority.direct.routing.key";


    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean(name = "listenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setPrefetchCount(1);
        factory.setConcurrentConsumers(10);
        return factory;
    }
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
    @Bean
    public Queue priorityQueue() {
        Map<String, Object> args = new <String, Object>HashMap(8);
        args.put("x-max-priority", 10);
        return new Queue(PRIORITY_QUEUE, true, false, false, args);
    }
    @Bean
    public DirectExchange priorityExchange() {
        return new DirectExchange(PRIORITY_EXCHANGE, true, false);
    }
    @Bean
    public Binding priorityBinding() {
        return BindingBuilder.bind(priorityQueue()).to(priorityExchange()).with(PRIORITY_ROUTING_KEY);
    }

}
