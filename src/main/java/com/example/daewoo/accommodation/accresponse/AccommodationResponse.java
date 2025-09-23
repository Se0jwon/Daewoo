package com.example.daewoo.accommodation.accresponse;

import com.example.daewoo.accommodation.dto.AccommodationAllDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationResponse {
    private Long totalCount;
    private Slice<AccommodationAllDto> accommodations;
}