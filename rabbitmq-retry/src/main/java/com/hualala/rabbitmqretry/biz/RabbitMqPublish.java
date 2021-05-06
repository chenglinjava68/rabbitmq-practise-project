package com.hualala.rabbitmqretry.biz;


import com.alibaba.fastjson.JSON;
import com.hualala.core.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMqPublish {


	@Autowired
	private AmqpTemplate amqpTemplate;

	public void send(Employee employee) {
		amqpTemplate.convertAndSend("aleenjava-direct-exchange", "aleenjava", employee);
		log.info("Send msg {}", JSON.toJSONString(employee));
	}
}