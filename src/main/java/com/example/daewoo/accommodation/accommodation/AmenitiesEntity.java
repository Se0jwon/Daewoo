package com.example.daewoo.accommodation.accommodation;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class AmenitiesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amId;

    private String amCategory;
    private String iconName;

    @ManyToMany(mappedBy = "amenities")
    private List<Accommodation> accommodations = new ArrayList<>();

}