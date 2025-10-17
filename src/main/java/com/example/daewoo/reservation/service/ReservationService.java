package com.example.daewoo.reservation.service;

import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.service.ParlorRepository;
import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.reservation.dto.ReservationEntity;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import com.example.daewoo.wish.dto.WishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParlorRepository parlorRepository;

    @Autowired
    private UserRepository userRepository;

    public void insert(Long userId, ReservationDto dto){
        ReservationEntity entity = dto.toEntity();

        ParlorEntity parlorEntity = resolveParlorForReservation(dto);
        entity.setParlorEntity(parlorEntity);

        UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        this.reservationRepository.save(entity);
    }


    public List<ReservationDto> findByUserId(Long userId){
        if (userId == null) {
            // userId가 null인 경우 처리
            return Collections.emptyList();
        }

        // 2. Repository 메서드 호출
        // JPQL 쿼리 덕분에, DB에서 이미 checkInTime과 checkOutTime이 채워진 DTO 리스트를 바로 가져옵니다.
        List<ReservationDto> reservations =
                reservationRepository.findAllReservationsByUserIdWithCheckInOut(userId);

        // 3. 결과 반환
        // 별도의 DTO 변환 과정 없이 바로 반환 가능 (Projection의 장점)
        return reservations;

    }


    public void update(Long userId, ReservationDto dto){
        ReservationEntity entity = dto.toEntity();

        ParlorEntity parlorEntity = resolveParlorForReservation(dto);
        entity.setParlorEntity(parlorEntity);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        this.reservationRepository.save(entity);
    }
    private ParlorEntity resolveParlorForReservation(ReservationDto dto) {
        if (dto.getAccId() == null) {
            throw new IllegalArgumentException("accId is required for auto assignment");
        }

        if (dto.getCheckIn() == null || dto.getCheckOut() == null) {
            throw new IllegalArgumentException("checkIn과 checkOut은 필수입니다.");
        }

        List<ParlorEntity> parlors = parlorRepository.findByAccRoomTypeEntityAccId(dto.getAccId());
        if (parlors.isEmpty()) {
            throw new IllegalStateException("예약 가능한 객실이 없습니다.");
        }

        for (ParlorEntity candidate : parlors) {
            boolean overlapping = reservationRepository
                    .existsByParlorEntityParIdAndCheckOutGreaterThanEqualAndCheckInLessThanEqual(
                            candidate.getParId(), dto.getCheckIn(), dto.getCheckOut());
            if (overlapping) {
                continue;
            }

            dto.setRoomNumber(candidate.getParContent());
            return candidate;
        }

        throw new IllegalStateException("요청 조건에 맞는 배정 가능한 객실이 없습니다.");
    }

    public void delete(Long id){
        this.reservationRepository.deleteById(id);
    }
}
