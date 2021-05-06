package com.hualala.rabbitmqpriorityqueue.controller;

import cn.hutool.core.lang.UUID;
import com.hualala.core.domain.Employee;
import com.hualala.core.vo.AjaxResult;
import com.hualala.rabbitmqpriorityqueue.biz.PriorityPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api")
@RestController
public class PriorityQueueController {

    @Autowired
    private PriorityPublish priorityPublish;

    @RequestMapping("/publishPriorityMsg")
    public AjaxResult publishPriorityMsg() {
        Employee employee;
        for(int i = 0;i < 20; i++){
            employee = new Employee();
            employee.setEmpId(UUID.fastUUID().toString());
            if(i % 10 ==0){
                employee.setEmpName("aleenjavaV1");
                employee.setSalary(10000);
                priorityPublish.publish(employee,1);
            }else{
                employee.setEmpName("aleenjavaV2");
                employee.setSalary(50000);
                priorityPublish.publish(employee,2);
            }
        }
        return AjaxResult.success();
    }

}
