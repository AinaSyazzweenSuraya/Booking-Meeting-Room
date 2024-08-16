package com.djava.meetingRoom.controller;

import com.djava.meetingRoom.dto.ApproveBookingRequest;
import com.djava.meetingRoom.dto.CreateBookingRequest;
import com.djava.meetingRoom.dto.BookingResponse;
import com.djava.meetingRoom.dto.UpdateBookingRequest;
import com.djava.meetingRoom.service.BookingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
        log.debug("REST request to create booking : {}", request);

        return bookingService.createBooking(request);
    }

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        log.debug("REST request to get all bookings");

        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable Long id) {
        log.debug("REST request to booking by id : {}", id);

        return bookingService.getById(id);
    }

    @PutMapping("/{id}")
    public BookingResponse updateBooking(@PathVariable Long id, @Valid @RequestBody UpdateBookingRequest request) {
        log.debug("REST request to update booking : {}", request);

        return bookingService.updateBooking(id, request);
    }

    @PutMapping("/approve/{id}")
    public BookingResponse approveBooking(@PathVariable Long id, @Valid @RequestBody ApproveBookingRequest request) {
        log.debug("REST request to approve booking : {}", request);

        return bookingService.approveBooking(id, request);
    }
}
