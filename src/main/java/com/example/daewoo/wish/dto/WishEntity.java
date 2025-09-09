package com.example.daewoo.wish.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wish")
@Builder
public class WishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;
    private Long userId;
    private Long accommodationId;
}