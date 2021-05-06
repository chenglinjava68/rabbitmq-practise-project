package com.hualala.rabbitexchangemodel.controller;

import cn.hutool.core.lang.UUID;
import com.hualala.core.domain.Employee;
import com.hualala.core.vo.AjaxResult;
import com.hualala.rabbitexchangemodel.biz.direct.DirectPublish;
import com.hualala.rabbitexchangemodel.biz.fanout.FanoutPublish;
import com.hualala.rabbitexchangemodel.biz.topic.TopicPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api")
@RestController
public class ExchangeModelController {

    @Autowired
    private DirectPublish directPublish;
    @Autowired
    private FanoutPublish fanoutPublish;
    @Autowired
    private TopicPublish topicPublish;

    @RequestMapping("/publishExchangeModelMsg")
    public AjaxResult publishExchangeModelMsg(@RequestParam(value = "type") Integer type) {
       switch (type){
           case 1:
               Employee  employee = new Employee();
               employee.setEmpName("helloworld");
               employee.setEmpId(UUID.fastUUID().toString());
               employee.setSalary(100);
               fanoutPublish.publishFanout(employee);
               break;
           case 2:
               Employee  employeeV1 = new Employee();
               employeeV1.setEmpId(UUID.fastUUID().toString());
               employeeV1.setSalary(10000);

               Employee  employeeV2 = new Employee();
               employeeV2.setEmpId(UUID.fastUUID().toString());
               employeeV2.setSalary(10000);

               directPublish.publishDirectOne(employeeV1);
               directPublish.publishDirectTwo(employeeV2);
               break;
           case 3:
               String routingKeyOne = "topic.routing.key.java";
               //此时相当于#：即 php.python 替代了#的位置
               String routingKeyTwo = "topic.routing.key.php.python";

               //此时相当于#：即0个单词
               String routingKeyThree = "topic.routing.key";

               Employee  employeeV3 = new Employee();
               employeeV3.setEmpId(UUID.fastUUID().toString());
               employeeV3.setSalary(10000);
               //topicPublish.publicTopicMsg(employeeV3, routingKeyOne);
               //topicPublish.publicTopicMsg(employeeV3, routingKeyTwo);
               topicPublish.publicTopicMsg(employeeV3, routingKeyThree);
               break;

       }
        return AjaxResult.success();
    }

}
