package com.bookingmeetingroom.controller;

import com.bookingmeetingroom.dto.ReservationDetail;
import com.bookingmeetingroom.dto.ReservationPostRequest;
import com.bookingmeetingroom.dto.ReservationPostResponse;
import com.bookingmeetingroom.dto.ReservationUpdateRequest;
import com.bookingmeetingroom.entity.ReservationEntity;
import com.bookingmeetingroom.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
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
    public ResponseEntity<List<ReservationDetail>> fetchAllReservations(@PathVariable Long userId){
        System.out.println("Received userId: " + userId);
        if (userId == null) {
            throw new IllegalArgumentException("User ID must be provided");
        }

        List<ReservationDetail> reservations = reservationService.fetchAll(userId);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationPostResponse> updateReservation(@PathVariable Long reservationId,
                                                                     @RequestBody ReservationUpdateRequest reservationUpdateRequest) {

        ReservationEntity updatedReservation = reservationService.update(reservationId, reservationUpdateRequest);
        ReservationPostResponse response = new ReservationPostResponse();
        response.setReservation(updatedReservation);
        response.setMessage("Reservation updated successfully");

        return ResponseEntity.ok(response);
    }

}

