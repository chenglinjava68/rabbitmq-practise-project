package com.hualala.springevent.service;

import com.hualala.core.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @author cheng
 */
@Component
@Slf4j
public class EventConsumer implements ApplicationListener<OrderEvent> {

    @Override
    @Async
    public void onApplicationEvent(OrderEvent event) {
        Order order = (Order) event.getSource();
        log.info("监听订单号:{},订单金额{}元", order.getOrderNo(), order.getOrderAmount());
    }
}
