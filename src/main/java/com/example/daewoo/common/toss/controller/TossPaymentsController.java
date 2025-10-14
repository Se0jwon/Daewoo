package com.example.daewoo.common.toss.controller;

import com.example.daewoo.common.toss.dto.TossPaymentDto;
import com.example.daewoo.common.toss.service.TossPaymentsService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class TossPaymentsController {

    private final TossPaymentsService tossPaymentsService;

    public TossPaymentsController(TossPaymentsService tossPaymentsService) {
        this.tossPaymentsService = tossPaymentsService;
    }

    @PostMapping("/toss/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody TossPaymentDto tossPaymentDto) {
        try {
            JsonNode payment = tossPaymentsService.confirmPayment(tossPaymentDto);
            // TODO: 성공 시, DB에 주문 정보 저장 등의 로직을 여기에 추가하세요.

            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            // TODO: 실패 시, 에러 처리 로직을 여기에 추가하세요.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}