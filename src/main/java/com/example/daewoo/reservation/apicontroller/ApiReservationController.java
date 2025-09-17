package com.example.daewoo.reservation.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.reservation.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/reservation")
public class ApiReservationController extends CommonRestController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("")
    public ResponseEntity<ResponseDto> insert(@RequestBody ReservationDto dto){
        try{
            this.reservationService.insert(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Insert Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Insert Error", null, e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@RequestBody ReservationDto dto,@PathVariable Long id){
        try{
            dto.setReservationId(id);
            this.reservationService.update(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Update Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Update Error", null, e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id){
        try{
            this.reservationService.delete(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Delete Ok", id, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.DELETE_FAIL, "Delete Error", null, e);
        }
    }
}
