package com.bookingmeetingroom.controller;

import com.bookingmeetingroom.dto.ReservationDetail;
import com.bookingmeetingroom.dto.ReservationPostRequest;
import com.bookingmeetingroom.dto.ReservationPostResponse;
import com.bookingmeetingroom.entity.ReservationEntity;
import com.bookingmeetingroom.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/{userId}")
    public ReservationPostResponse createReservation(@PathVariable Long userId,
                                                     @RequestBody ReservationPostRequest reservationPostRequest){

        reservationPostRequest.setUserId(userId);
        ReservationEntity createdReservation = reservationService.add(reservationPostRequest);
        ReservationPostResponse resp = new ReservationPostResponse();
        resp.setReservation(createdReservation);

        return resp;
    }

    @GetMapping("/{userId}")
    public List<ReservationDetail> getAllReservations(@RequestParam Long userId){
        if (userId == null) {
            throw new IllegalArgumentException("User ID must be provided");
        }
        return reservationService.fetchAll(userId);
    }


    /*@GetMapping("/subset")
    public void subset(){

    }*/

}

