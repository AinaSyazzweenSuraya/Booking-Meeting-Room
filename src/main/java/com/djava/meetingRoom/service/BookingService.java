package com.djava.meetingRoom.service;

import com.djava.meetingRoom.common.BookingStatus;
import com.djava.meetingRoom.common.UserRole;
import com.djava.meetingRoom.dto.ApproveBookingRequest;
import com.djava.meetingRoom.dto.BookingResponse;
import com.djava.meetingRoom.dto.CreateBookingRequest;
import com.djava.meetingRoom.dto.UpdateBookingRequest;
import com.djava.meetingRoom.entity.Booking;
import com.djava.meetingRoom.entity.Room;
import com.djava.meetingRoom.repository.BookingRepository;
import com.djava.meetingRoom.repository.RoomRepository;
import com.djava.meetingRoom.security.SecurityUtils;
import com.djava.meetingRoom.service.error.ApplicationError;
import com.djava.meetingRoom.service.error.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public BookingResponse createBooking(CreateBookingRequest request) {
        log.debug("Request to book room : {}", request);

        Optional<Room> optional = roomRepository.findById(request.getRoomId());
        if(optional.isEmpty()){
            throw new ApplicationException(ApplicationError.ROOM_NOT_FOUND);
        }

        Room room = optional.get();
        if (room.isOccupied()) {
            throw new ApplicationException(ApplicationError.ROOM_IS_OCCUPIED);
        }

        String username = SecurityUtils.currentLoggedInUsername();

        Booking booking = new Booking();
        booking.setRoomId(room.getId());
        booking.setStart(parseDate(request.getStart()));
        booking.setEnd(parseDate(request.getEnd()));
        booking.setBookedBy(username);
        booking.setStatus(BookingStatus.PENDING_APPROVAL.name());

        booking = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setRoomId(booking.getRoomId());
        response.setStart(booking.getStart());
        response.setEnd(booking.getEnd());
        response.setBookedBy(booking.getBookedBy());
        response.setStatus(booking.getStatus());

        return response;
    }

    private LocalDateTime parseDate(String date) {
        // 2018-05-05T11:50:55
        return LocalDateTime.parse(date);
    }

    public List<BookingResponse> getAll() {
        log.debug("Request to get all bookings");

        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .map(booking -> new BookingResponse(booking.getId(),
                        booking.getRoomId(),
                        booking.getStart(),
                        booking.getEnd(),
                        booking.getBookedBy(),
                        booking.getStatus()))
                .collect(Collectors.toList());
    }

    public BookingResponse getById(Long id) {
        log.debug("Request to get booking by id: {}", id);

        Optional<Booking> optional = bookingRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ApplicationException(ApplicationError.BOOKING_NOT_FOUND);
        }

        Booking booking = optional.get();

        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setRoomId(booking.getRoomId());
        response.setStart(booking.getStart());
        response.setEnd(booking.getEnd());
        response.setBookedBy(booking.getBookedBy());
        response.setStatus(booking.getStatus());

        return response;
    }

    public BookingResponse updateBooking(Long id, UpdateBookingRequest request) {
        log.debug("Request to book room : {}", request);

        Optional<Booking> optional = bookingRepository.findById(id);
        if(optional.isEmpty()){
            throw new ApplicationException(ApplicationError.BOOKING_NOT_FOUND);
        }
        Booking booking = optional.get();

        String userRole = SecurityUtils.currentLoggedInUserRole();
        if (userRole.equals(UserRole.USER.name())) {
            String username = SecurityUtils.currentLoggedInUsername();

            if (!username.equals(booking.getCreatedBy())) {
                throw new ApplicationException(ApplicationError.FORBIDDEN_ACCESS);
            }
        }

        if (!Objects.equals(booking.getRoomId(), request.getRoomId())) {
            booking.setRoomId(request.getRoomId());
        }
        if (!Objects.equals(booking.getStart(), request.getStart())) {
            booking.setStart(request.getStart());
        }
        if (!Objects.equals(booking.getEnd(), request.getEnd())) {
            booking.setEnd(request.getEnd());
        }

        booking = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setRoomId(booking.getRoomId());
        response.setStart(booking.getStart());
        response.setEnd(booking.getEnd());
        response.setBookedBy(booking.getBookedBy());
        response.setStatus(booking.getStatus());

        return response;
    }

    public BookingResponse approveBooking(Long id, ApproveBookingRequest request) {
        log.debug("Request to approve booking : {}", request);

        Optional<Booking> optional = bookingRepository.findById(id);
        if(optional.isEmpty()){
            throw new ApplicationException(ApplicationError.BOOKING_NOT_FOUND);
        }

        Booking booking = optional.get();
        if (booking.getStatus().equalsIgnoreCase(BookingStatus.APPROVED.name())) {
            throw new ApplicationException(ApplicationError.BOOKING_ALREADY_APPROVED);
        }

        booking.setStatus(BookingStatus.APPROVED.name());

        booking = bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setRoomId(booking.getRoomId());
        response.setStart(booking.getStart());
        response.setEnd(booking.getEnd());
        response.setBookedBy(booking.getBookedBy());
        response.setStatus(booking.getStatus());

        return response;
    }
}
