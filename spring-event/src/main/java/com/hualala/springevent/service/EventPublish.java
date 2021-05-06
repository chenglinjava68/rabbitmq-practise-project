package com.hualala.springevent.service;

import cn.hutool.core.lang.UUID;
import com.hualala.core.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EventPublish {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish() {
        Order order = new Order();
        order.setOrderNo(UUID.fastUUID().toString());
        order.setCreateBy("aleenjava");
        order.setOrderAmount(BigDecimal.valueOf(Math.random()));
        OrderEvent orderEvent = new OrderEvent(order);
        applicationEventPublisher.publishEvent(orderEvent);
    }
}
