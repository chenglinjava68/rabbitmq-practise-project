package com.hualala.rabbitmqbasic.controller;

import cn.hutool.core.lang.UUID;
import com.hualala.core.domain.Order;
import com.hualala.core.vo.AjaxResult;
import com.hualala.rabbitmqbasic.biz.BasicPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping("/api")
@RestController
public class BasicController {

    @Autowired
    private BasicPublisher basicPublisher;

    @RequestMapping("/publishMsg")
    public AjaxResult publishMsg() {
        Order order = new Order();
        order.setOrderNo(UUID.fastUUID().toString());
        order.setCreateBy("aleenjava");
        order.setOrderAmount(BigDecimal.valueOf(Math.random()));
        basicPublisher.send(order);
        return AjaxResult.success();
    }

}
