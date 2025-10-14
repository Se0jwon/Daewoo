package com.example.daewoo.common.toss.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TossPaymentDto {
    private String paymentKey;
    private String orderId;
    private Long amount;
}