package com.example.daewoo.user.card.dto;

import com.example.daewoo.user.dto.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String cardNumber;
    private String country;
    private String name;
    private String expDate;
    private Integer cvc;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
