package com.example.daewoo.accommodation.accommodation;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "amenities")
public class AmenitiesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amId;

    private String amCategory;
    private String iconName;

    @ManyToMany(mappedBy = "amenities")
    private List<AccommodationEntity> accommodations = new ArrayList<>();

}