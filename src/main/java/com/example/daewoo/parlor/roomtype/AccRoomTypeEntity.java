package com.example.daewoo.parlor.roomtype;

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
@Table(name = "acc_room_type")
public class AccRoomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accId;

    @ManyToOne
    @JoinColumn(name = "com_id")
    private AccommodationEntity accommodation;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomType;

    private Integer price;
    private Integer maxRoom;


}
