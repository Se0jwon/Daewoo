package com.example.daewoo.accommodation.image.dto;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "com_image")
public class ComImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comImageId;
    private String imageUrl;
    private Boolean isMain;

    @ManyToOne
    @JoinColumn(name = "com_id")
    private AccommodationEntity accommodation;
}
