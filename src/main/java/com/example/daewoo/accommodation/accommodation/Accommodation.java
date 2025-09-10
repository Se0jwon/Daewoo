package com.example.daewoo.accommodation.accommodation;

import com.example.daewoo.accommodation.accommodation.AmenitiesEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comId;

    private String comTitle;

    @ManyToMany
    @JoinTable(
            name = "accommodation_amenity",
            joinColumns = @JoinColumn(name = "com_id"),
            inverseJoinColumns = @JoinColumn(name = "am_id")
    )
    private List<AmenitiesEntity> amenities;
}