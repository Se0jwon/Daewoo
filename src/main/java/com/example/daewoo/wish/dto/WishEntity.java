package com.example.daewoo.wish.dto;

import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.user.dto.UserEntity;
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
    @Column(name = "wish_id")
    private Long wishId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "par_id")
    private ParlorEntity parlorEntity;

}