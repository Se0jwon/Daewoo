package com.example.daewoo.accommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDto {
    private Long accommodationId;
    private String name;
    private String location;
    private Integer price;
}