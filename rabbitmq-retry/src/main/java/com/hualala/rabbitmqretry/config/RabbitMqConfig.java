package com.hualala.rabbitmqretry.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	@Bean
	public DirectExchange deadLetterExchange() {
		return new DirectExchange("deadLetterExchange");
	}
	@Bean
	public Queue dlq() {
		return QueueBuilder.durable("deadLetterQueue").build();
	}

	@Bean
	public Binding DLQbinding(Queue dlq, DirectExchange deadLetterExchange) {
		return BindingBuilder.bind(dlq).to(deadLetterExchange).with("deadLetter");
	}


	@Bean
	public DirectExchange exchange() {
		return new DirectExchange("aleenjava-direct-exchange");
	}


	@Bean
	public Queue queue() {
		return QueueBuilder.durable("aleenjava.queue")
				.withArgument("x-dead-letter-exchange","deadLetterExchange")
				.withArgument("x-dead-letter-routing-key", "deadLetter").build();
	}

	@Bean
	public Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("aleenjava");
	}


	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}
