package com.example.daewoo.wish.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishDto {
    private Long wishId;
    private Long userId;
    private Long accommodationId;
}