package com.example.daewoo.parlor.dto;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
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
@Table(name = "parlor")
public class ParlorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parId;

    @ManyToOne
    @JoinColumn(name = "com_id")
    private AccommodationEntity accommodationEntity;

    @ManyToOne
    @JoinColumn(name = "acc_id")
    private AccRoomTypeEntity accRoomTypeEntity;

    private String parContent;

}
