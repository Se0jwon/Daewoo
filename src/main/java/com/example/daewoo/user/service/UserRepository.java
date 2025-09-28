// UserRepository.java

package com.example.daewoo.user.service;

import com.example.daewoo.user.dto.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// Long은 UserEntity의 기본 키(ID) 타입이라고 가정합니다.
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 이메일로 사용자 찾는 메서드 (CustomOAuth2UserService, UserService에서 사용)
    Optional<UserEntity> findByUserEmail(String userEmail);

    // ⭐⭐⭐ [오류 해결] 소셜 회원가입 완료 시 사용자 정보를 찾기 위해 필요한 메서드 ⭐⭐⭐
    // UserEntity의 oauthId와 registrationId를 조합하여 사용자를 찾습니다.
    Optional<UserEntity> findByOauthIdAndRegistrationId(String oauthId, String registrationId);

    // UserEntity의 findById 대신 JpaRepository의 findById(Long id)를 사용해도 되지만,
    // 기존 UserService 코드와의 일관성을 위해 findById(Long id)는 JpaRepository 기본 메서드를 사용한다고 가정합니다.
}