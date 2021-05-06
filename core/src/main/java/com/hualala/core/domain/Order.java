package com.hualala.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * @author cheng
 */
@Data
@EqualsAndHashCode
public class Order {

    private String orderNo;
    private BigDecimal orderAmount;
    private String createBy;
}
