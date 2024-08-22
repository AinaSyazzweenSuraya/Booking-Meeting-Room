package com.bookingmeetingroom.service;

import com.bookingmeetingroom.dto.ReservationDetail;
import com.bookingmeetingroom.dto.ReservationPostRequest;
import com.bookingmeetingroom.dto.ReservationUpdateRequest;
import com.bookingmeetingroom.dto.RoomStatus;
import com.bookingmeetingroom.entity.MeetingRoomEntity;
import com.bookingmeetingroom.entity.ReservationEntity;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.exceptions.ApplicationException;
import com.bookingmeetingroom.repository.MeetingRoomRepository;
import com.bookingmeetingroom.repository.ReservationRepository;
import com.bookingmeetingroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

        @Autowired
        private ReservationRepository reservationRepository;

        @Autowired
        private MeetingRoomRepository meetingRoomRepository;

        @Autowired
        private UserRepository userRepository;


        //Add reservations
        public ReservationEntity add(ReservationPostRequest reservationRequest){

            // Check room id
            MeetingRoomEntity room = meetingRoomRepository.findById(reservationRequest.getRoomId())
                    .orElseThrow(() -> new ApplicationException("Meeting room not found"));

            //check occupied or not
            if (room.getIsOccupied() == RoomStatus.OCCUPIED) {
                throw new ApplicationException("The meeting room is already occupied");
            }

            //check user id
            UserEntity user = userRepository.findById(reservationRequest.getUserId())
                    .orElseThrow(() -> new ApplicationException("User not found"));

            ReservationEntity reservation = new ReservationEntity();
            reservation.setMeetingRoom(room);
            reservation.setUser(user);
            reservation.setStartDate(reservationRequest.getStartDate());
            reservation.setEndDate(reservationRequest.getEndDate());
            reservation.setCreatedAt(LocalDateTime.now());

            ReservationEntity savedReservation = reservationRepository.save(reservation);

            room.setIsOccupied(RoomStatus.OCCUPIED);
            meetingRoomRepository.save(room);

            return savedReservation;
        }

        //get reservations
        public List<ReservationDetail> fetchAll(Long userId) {
            Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
            if (userEntityOptional.isEmpty()) {
                throw new ApplicationException("User not found");
            }

            UserEntity user = userEntityOptional.get();
            List<ReservationEntity> reservations;

            if (user.getType().equals("admin")) {
                reservations = reservationRepository.findAll();
            } else {
                reservations = reservationRepository.findByUserId(userId);
            }

            /*System.out.println("User Type: " + user.getType());
            System.out.println("Number of Reservations: " + reservations.size());*/

            return reservations.stream()
                    .map(this::convertToDetail)
                    .collect(Collectors.toList());
        }

        private ReservationDetail convertToDetail(ReservationEntity reservationEntity) {
            ReservationDetail reservationDetail = new ReservationDetail();
            reservationDetail.setId(reservationEntity.getId());
            reservationDetail.setStartDate(reservationEntity.getStartDate());
            reservationDetail.setEndDate(reservationEntity.getEndDate());
            reservationDetail.setRoomName(reservationEntity.getMeetingRoom() != null ? reservationEntity.getMeetingRoom().getName() : "Room not found");
            reservationDetail.setUserName(reservationEntity.getUser() != null ? reservationEntity.getUser().getUsername() : "User not found");

            return reservationDetail;
        }

        public ReservationEntity update(Long reservationId, ReservationUpdateRequest reservationUpdateRequest) {
            ReservationEntity reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new ApplicationException("Reservation not found"));

            // Check if the meeting room exists and is valid for update
            MeetingRoomEntity room = meetingRoomRepository.findById(reservationUpdateRequest.getRoomId())
                    .orElseThrow(() -> new ApplicationException("Meeting room not found"));

            if (room.getIsOccupied() == RoomStatus.OCCUPIED) {
                throw new ApplicationException("The meeting room is currently occupied");
            }

            // Update reservation details
            if (reservation != null) {
                reservation.setMeetingRoom(room);
                reservation.setStartDate(reservationUpdateRequest.getStartDate());
                reservation.setEndDate(reservationUpdateRequest.getEndDate());
                reservation.setUpdatedAt(LocalDateTime.now());


            return reservationRepository.save(reservation);
        }
            else{
                throw new ApplicationException("Failed to update reservation");
            }
        }
}
