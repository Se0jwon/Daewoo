// UserDto.java (최종 수정)

package com.example.daewoo.user.dto;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.reservation.dto.ReservationEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collections; // 🚨 Collections 임포트 추가
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private String username;
    private String password;
    private String userAddress;
    private String userPhone;
    private String userEmail;
    private LocalDate userBirth;
    private String imageUrl;

    private List<ReservationDto> reservations;

    // 💡 기존의 생성자 (Entity -> Dto 변환)
    public UserDto(UserEntity entity) {
        this.userId = entity.getUserId();
        this.username = entity.getUsername();
        this.password = null; // 보안을 위해 비밀번호는 DTO에 포함하지 않거나 null 처리
        this.userAddress = entity.getUserAddress();
        this.userPhone = entity.getUserPhone();
        this.userEmail = entity.getUserEmail();
        this.userBirth = entity.getUserBirth();
        this.imageUrl = entity.getImageUrl();

        // 예약 정보 변환
        if (entity.getReservations() != null) {
            this.reservations = entity.getReservations().stream()
                    .map(ReservationDto::fromEntity) // ReservationDto에도 fromEntity 메서드가 있어야 함
                    .collect(Collectors.toList());
        } else {
            this.reservations = Collections.emptyList();
        }
    }

    // ⭐ [필수 추가] Entity -> DTO 변환 (static 메서드 형식, UserDto(entity) 생성자를 활용)
    public static UserDto fromEntity(UserEntity entity) {
        return new UserDto(entity);
    }

    // ⭐ [필수 추가] DTO -> Entity 변환 (UserService.insert 등에서 사용)
    public UserEntity toEntity() {
        return UserEntity.builder() // UserEntity에는 @Builder가 있으므로 사용합니다.
                .userId(this.userId)
                .username(this.username)
                .password(this.password)
                .userAddress(this.userAddress)
                .userPhone(this.userPhone)
                .userEmail(this.userEmail)
                .userBirth(this.userBirth)
                .imageUrl(this.imageUrl)
                // 소셜/권한 정보는 일반 회원가입 DTO에는 없을 수 있으나, 빌더 패턴이 처리할 수 있습니다.
                .build();
    }
}