package com.example.daewoo.accommodation.location.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;
    private String locationName;

    public LocationEntity toEntity(){
        LocationEntity entity = new LocationEntity();
        entity.setLocationId(this.locationId);
        entity.setLocationName(this.locationName);

        return entity;
    }

    public static LocationDto fromEntity(LocationEntity entity){
        LocationDto dto = new LocationDto();
        dto.setLocationId(entity.getLocationId());
        dto.setLocationName(entity.getLocationName());

        return dto;
    }
}
