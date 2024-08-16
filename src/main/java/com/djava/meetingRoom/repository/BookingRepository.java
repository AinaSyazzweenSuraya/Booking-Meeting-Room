package com.djava.meetingRoom.repository;

import com.djava.meetingRoom.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
