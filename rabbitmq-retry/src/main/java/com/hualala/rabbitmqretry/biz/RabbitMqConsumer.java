package com.hualala.rabbitmqretry.biz;


import com.hualala.core.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqConsumer {


	@RabbitListener(queues = "aleenjava.queue")
	public void recievedMessage(@Payload  Employee employee){
		log.info("Recieved Message From RabbitMQ: {}", employee);
		try{
			double result = 1/0;
		}catch (Exception e){
			log.error("error occur",e);
			throw new RuntimeException("InvalidSalaryException");
		}

	}
}