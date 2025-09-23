package com.example.daewoo.user.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private Long cardId;
    private String cardNumber;
    private String country;
    private String name;
    private String expDate;
    private Integer cvc;

    private Long userId;

    public CardEntity toEntity(){
        CardEntity entity = new CardEntity();
        entity.setCardId(this.cardId);
        entity.setCardNumber(this.cardNumber);
        entity.setCountry(this.country);
        entity.setName(this.name);
        entity.setExpDate(this.expDate);
        entity.setCvc(this.cvc);

        return entity;
    }

    public static CardDto fromEntity(CardEntity entity){
        CardDto dto = new CardDto();
        dto.setCardId(entity.getCardId());
        dto.setCardNumber(entity.getCardNumber());
        dto.setCountry(entity.getCountry());
        dto.setName(entity.getName());
        dto.setExpDate(entity.getExpDate());
        dto.setCvc(entity.getCvc());

        dto.setUserId(entity.getUserEntity().getUserId());

        return dto;
    }
}
