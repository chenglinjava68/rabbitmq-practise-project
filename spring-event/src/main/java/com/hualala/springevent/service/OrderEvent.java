package com.hualala.springevent.service;

import com.hualala.core.domain.Order;
import org.springframework.context.ApplicationEvent;

/**
 * @author cheng
 */
public class OrderEvent extends ApplicationEvent {

    public OrderEvent(Order source) {
        super(source);
    }
}
