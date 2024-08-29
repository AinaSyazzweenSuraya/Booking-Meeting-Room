package com.bookingmeetingroom.service;

import com.bookingmeetingroom.dto.MeetingRoomDetail;
import com.bookingmeetingroom.dto.MeetingRoomPostRequest;
import com.bookingmeetingroom.dto.MeetingRoomUpdateRequest;
import com.bookingmeetingroom.entity.MeetingRoomEntity;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.exceptions.ApplicationException;
import com.bookingmeetingroom.repository.MeetingRoomRepository;
import com.bookingmeetingroom.repository.UserRepository;
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

    private final MeetingRoomRepository meetingRoomRepository;
    private final UserRepository userRepository;
    private final JsonPlaceholderClient jsonPlaceholderClient;

    @Autowired
    public MeetingRoomService(MeetingRoomRepository meetingRoomRepository, UserRepository userRepository, JsonPlaceholderClient jsonPlaceholderClient) {
        this.meetingRoomRepository = meetingRoomRepository;
        this.userRepository = userRepository;
        this.jsonPlaceholderClient = jsonPlaceholderClient;
    }

    public MeetingRoomEntity add(MeetingRoomPostRequest meetingRoomPostRequests) {
        // get user from db
        Optional<UserEntity> userEntityOptional = userRepository.findById(meetingRoomPostRequests.getUserId());
        if (userEntityOptional.isEmpty()) {
            throw new ApplicationException("Only admins can add a meeting room");
        }

        UserEntity user = userEntityOptional.get();
        if (!user.getType().equals("admin")) {
            throw new ApplicationException("Only admins can add a meeting room");
        }

        MeetingRoomEntity meetingRooms = new MeetingRoomEntity();
        meetingRooms.setCreatedBy(user.getType());
        meetingRooms.setCreatedAt(LocalDateTime.now());
        meetingRooms.setName(meetingRoomPostRequests.getName());
        meetingRooms.setCapacity(meetingRoomPostRequests.getCapacity());
        meetingRooms.setIsOccupied(meetingRoomPostRequests.getIsOccupied());

        MeetingRoomEntity savedMeetingRoom = meetingRoomRepository.save(meetingRooms);

        // Create a corresponding post in JSONPlaceholder
        String postJson = "{\"title\":\"" + savedMeetingRoom.getName() + "\", \"body\":\"Capacity: " + savedMeetingRoom.getCapacity() + "\", \"userId\":" + meetingRoomPostRequests.getUserId() + "}";
        jsonPlaceholderClient.createPost(postJson);

        return savedMeetingRoom;
    }

    public List<MeetingRoomDetail> fetchAll() {
        List<MeetingRoomEntity> allMeetingRooms = meetingRoomRepository.findAll();

        return allMeetingRooms.stream()
                .map(meetingRoomEntity -> new MeetingRoomDetail(meetingRoomEntity.getName(),
                        meetingRoomEntity.getCapacity(),
                        meetingRoomEntity.getIsOccupied()))
                .collect(Collectors.toList());
    }

    public List<MeetingRoomDetail> subset(String orderBy, String direction, int page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), orderBy));

        return meetingRoomRepository.findAll(pageable).stream()
                .map(meetingRoomEntity -> new MeetingRoomDetail(meetingRoomEntity.getName(),
                        meetingRoomEntity.getCapacity(),
                        meetingRoomEntity.getIsOccupied()))
                .collect(Collectors.toList());
    }

    public MeetingRoomDetail fetch(Long id) {
        Optional<MeetingRoomEntity> roomsOptional = meetingRoomRepository.findById(id);
        MeetingRoomEntity room = roomsOptional.orElse(null);

        if (room == null) {
            throw new ApplicationException("Meeting room not found");
        }

        return new MeetingRoomDetail(room.getName(), room.getCapacity(), room.getIsOccupied());
    }

    public MeetingRoomEntity update(Long roomId, MeetingRoomUpdateRequest meetingRoomUpdateRequest) {

        Optional<UserEntity> userEntityOptional = userRepository.findById(meetingRoomUpdateRequest.getUserId());
        if (userEntityOptional.isEmpty()) {
            throw new ApplicationException("User not found");
        }

        UserEntity user = userEntityOptional.get();
        if (!user.getType().equals("admin")) {
            throw new ApplicationException("Only admins can update a meeting room");
        }

        // Find existing meeting room
        MeetingRoomEntity meetingRoom = meetingRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApplicationException("Meeting room not found"));

        // Update meeting room details
        meetingRoom.setName(meetingRoomUpdateRequest.getName());
        meetingRoom.setCapacity(meetingRoomUpdateRequest.getCapacity());
        meetingRoom.setIsOccupied(meetingRoomUpdateRequest.getIsOccupied());
        meetingRoom.setUpdatedBy(user.getType());
        meetingRoom.setUpdatedAt(LocalDateTime.now());

        MeetingRoomEntity updatedMeetingRoom = meetingRoomRepository.save(meetingRoom);

        // Update corresponding post in JSONPlaceholder
        String postJson = "{\"title\":\"" + updatedMeetingRoom.getName() + "\", \"body\":\"Capacity: " + updatedMeetingRoom.getCapacity() + "\", \"userId\":" + meetingRoomUpdateRequest.getUserId() + "}";
        jsonPlaceholderClient.updatePost(roomId, postJson);

        return updatedMeetingRoom;
    }

    public void delete(Long roomId, Long userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new ApplicationException("User not found");
        }

        UserEntity user = userEntityOptional.get();
        if (!user.getType().equals("admin")) {
            throw new ApplicationException("Only admins can delete a meeting room");
        }

        MeetingRoomEntity meetingRoom = meetingRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApplicationException("Meeting room not found"));

        meetingRoomRepository.delete(meetingRoom);

        // Delete the corresponding post in JSONPlaceholder
        jsonPlaceholderClient.deletePost(roomId);
    }
}
