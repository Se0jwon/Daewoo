package com.example.daewoo.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDto {
    private ResponseCode code;
    private String message;
    private Object result;
}
