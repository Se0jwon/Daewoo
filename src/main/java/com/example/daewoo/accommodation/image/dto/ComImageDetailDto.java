package com.example.daewoo.accommodation.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComImageDetailDto {
    private Long comId;
    private String mainImage;
    private List<String> subImage;
}
