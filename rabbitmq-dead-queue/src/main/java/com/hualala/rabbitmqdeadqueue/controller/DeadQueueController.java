package com.hualala.rabbitmqdeadqueue.controller;

import cn.hutool.core.lang.UUID;
import com.hualala.core.domain.Employee;
import com.hualala.core.vo.AjaxResult;
import com.hualala.rabbitmqdeadqueue.biz.DeadQueuePublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class DeadQueueController {

    @Autowired
    private DeadQueuePublish deadQueuePublish;


    @RequestMapping("/deadQueuePublish")
    public AjaxResult deadQueuePublish() {
        Employee employee = new Employee();
        employee.setEmpName(UUID.fastUUID().toString());
        employee.setSalary(30000);
        employee.setEmpId("1000");
        deadQueuePublish.publish(employee);
        return AjaxResult.success();
    }



}
