package com.bookingmeetingroom.dto;

import com.bookingmeetingroom.entity.ReservationEntity;

public class ReservationPostResponse {

    private ReservationEntity reservationEntity;
    private String message;

    public ReservationEntity getReservation() {
        return reservationEntity;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservationEntity = reservation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
