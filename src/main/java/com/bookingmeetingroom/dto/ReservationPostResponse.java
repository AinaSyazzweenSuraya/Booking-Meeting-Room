package com.bookingmeetingroom.dto;

import com.bookingmeetingroom.entity.ReservationEntity;

public class ReservationPostResponse {

    private ReservationEntity reservationEntity;

    public ReservationEntity getReservation() {
        return reservationEntity;
    }

    public void setReservation(ReservationEntity reservation) {
        this.reservationEntity = reservation;
    }
}
