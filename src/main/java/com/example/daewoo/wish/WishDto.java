package com.example.daewoo.wish;

import lombok.Data;

@Data
public class WishDto {
    private int wishId;
    private long userId;
    private long fk;
    private String email;
    private Long fk2;
    private Boolean field2;
}