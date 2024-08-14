package com.bookingmeetingroom.service;

import com.bookingmeetingroom.dto.MeetingRoomDetail;
import com.bookingmeetingroom.dto.MeetingRoomPostRequest;
import com.bookingmeetingroom.dto.UsersDetail;
import com.bookingmeetingroom.entity.MeetingRoomEntity;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingRoomService {

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    public MeetingRoomEntity add(MeetingRoomPostRequest meetingRoomPostRequests) {
        MeetingRoomEntity meetingRooms = new MeetingRoomEntity();
        meetingRooms.setCreatedBy("admin");
        meetingRooms.setCreatedAt(LocalDateTime.now());
        meetingRooms.setName(meetingRoomPostRequests.getName());
        meetingRooms.setCapacity(meetingRoomPostRequests.getCapacity());
        meetingRooms.setIsOccupied(meetingRoomPostRequests.getIsOccupied());

        return meetingRoomRepository.save(meetingRooms);
    }


    public List<MeetingRoomDetail> fetchAll() {
        List<MeetingRoomEntity> allMeetingRooms = meetingRoomRepository.findAll();

        return allMeetingRooms.stream().
                map(MeetingRoomEntity -> new MeetingRoomDetail(MeetingRoomEntity.getName(),
                        MeetingRoomEntity.getCapacity(),
                        MeetingRoomEntity.getIsOccupied()))
                .collect(Collectors.toList());
    }

    public List<MeetingRoomDetail> fetchByCreatedBy(String name) {
        List<MeetingRoomEntity> meetingRoomByUser = meetingRoomRepository.findAll();

        return meetingRoomByUser.stream().
                map(MeetingRoomEntity -> new MeetingRoomDetail(MeetingRoomEntity.getName(),
                        MeetingRoomEntity.getCapacity(),
                        MeetingRoomEntity.getIsOccupied()))
                .collect(Collectors.toList());
    }

    public List<MeetingRoomDetail> subset(String orderBy, String direction, int page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), orderBy));

        return meetingRoomRepository.findAll(pageable).stream().
                map(MeetingRoomEntity -> new MeetingRoomDetail(MeetingRoomEntity.getName(),
                        MeetingRoomEntity.getCapacity(),
                        MeetingRoomEntity.getIsOccupied()))
                .collect(Collectors.toList());
    }

    public MeetingRoomDetail fetch(Long id) {
        Optional<MeetingRoomEntity> roomsOptional = meetingRoomRepository.findById(id);
        MeetingRoomEntity room;

        if(roomsOptional.isPresent()){
            room = roomsOptional.get();
        }
        else{
            room = null;
        }

        MeetingRoomDetail roomsDetail = new MeetingRoomDetail();
        roomsDetail.setName(room.getName());
        roomsDetail.setCapacity(room.getCapacity());
        roomsDetail.setIsOccupied(room.getIsOccupied());
        return roomsDetail;
    }
}
