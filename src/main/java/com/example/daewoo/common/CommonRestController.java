package com.example.daewoo.common;

import org.springframework.http.ResponseEntity;

public class CommonRestController {
    public ResponseEntity<ResponseDto> getResponseEntity(ResponseCode code, String message, Object data, Throwable th) {
        if ( th == null ) {
            return ResponseEntity.ok().body(
                    ResponseDto.builder()
                            .code(code)
                            .message(message)
                            .result(data)
                            .build()
            );
        } else {
            return ResponseEntity.status(500).body(
                    ResponseDto.builder()
                    .code(code)
                    .message(message)
                    .result(data)
                    .build()
            );
        }
    }
}
