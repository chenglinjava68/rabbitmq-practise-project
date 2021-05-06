package com.hualala.rabbitmqretry.controller;

import com.hualala.core.domain.Employee;
import com.hualala.core.vo.AjaxResult;
import com.hualala.rabbitmqretry.biz.RabbitMqPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/")
public class RabbitMqController {

	@Autowired
	RabbitMqPublish rabbitMQSender;

	@GetMapping(value = "/retryQueuePublish")
	public AjaxResult retryQueuePublish() {
		Employee emp = new Employee();
		emp.setEmpId(UUID.randomUUID().toString());
		emp.setEmpName("aleenjava");
		emp.setSalary(1000);
		rabbitMQSender.send(emp);
		return AjaxResult.success();
	}

}
