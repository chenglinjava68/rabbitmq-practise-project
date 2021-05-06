package com.hualala.rabbitmqdelayqueue.controller;

import cn.hutool.core.lang.UUID;
import com.hualala.core.domain.Employee;
import com.hualala.core.domain.Order;
import com.hualala.core.vo.AjaxResult;
import com.hualala.rabbitmqdelayqueue.biz.DelayQueuePublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class DelayQueueController {

    @Autowired
    private DelayQueuePublish delayQueuePublish;


    @RequestMapping("/delayQueuePublish")
    public AjaxResult delayQueuePublish() {
        Employee employee = new Employee();
        employee.setEmpName(UUID.fastUUID().toString());
        employee.setSalary(30000);
        employee.setEmpId("1000");
        delayQueuePublish.publish(employee);
        return AjaxResult.success();
    }



}
