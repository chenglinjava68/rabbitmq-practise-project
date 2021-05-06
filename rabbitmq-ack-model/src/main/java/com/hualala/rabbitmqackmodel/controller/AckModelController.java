package com.hualala.rabbitmqackmodel.controller;

import cn.hutool.core.lang.UUID;
import com.hualala.core.domain.Order;
import com.hualala.core.vo.AjaxResult;
import com.hualala.rabbitmqackmodel.biz.auto.AutoAckPublish;
import com.hualala.rabbitmqackmodel.biz.manual.ManualAckPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequestMapping("/api")
@RestController
public class AckModelController {

    @Autowired
    private AutoAckPublish basicPublisher;
    @Autowired
    private ManualAckPublish manualAckPublish;


    @RequestMapping("/publishAutoAckMsg")
    public AjaxResult publishAutoAckMsg() {
        Order order = new Order();
        order.setOrderNo(UUID.fastUUID().toString());
        order.setCreateBy("aleenjava");
        order.setOrderAmount(BigDecimal.valueOf(Math.random()));
        basicPublisher.publish(order);
        return AjaxResult.success();
    }

    @RequestMapping("/publishManualAckMsg")
    public AjaxResult publishManualAckMsg() {
        Order order = new Order();
        order.setOrderNo(UUID.fastUUID().toString());
        order.setCreateBy("aleenjava");
        order.setOrderAmount(BigDecimal.valueOf(Math.random()));
        manualAckPublish.publish(order);
        return AjaxResult.success();
    }

}
