package com.djava.meetingRoom.service;

import com.djava.meetingRoom.common.UserRole;
import com.djava.meetingRoom.dto.CreateRoomRequest;
import com.djava.meetingRoom.dto.RoomResponse;
import com.djava.meetingRoom.dto.UpdateRoomRequest;
import com.djava.meetingRoom.entity.Room;
import com.djava.meetingRoom.repository.RoomRepository;
import com.djava.meetingRoom.security.SecurityUtils;
import com.djava.meetingRoom.service.error.ApplicationError;
import com.djava.meetingRoom.service.error.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomResponse create(CreateRoomRequest request) {
        log.debug("Request to save room : {}", request);

        Room room = new Room();
        room.setName(request.getName());
        room.setCapacity(request.getCapacity());
        room.setOccupied(false);

        room = roomRepository.save(room);

        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setCapacity(room.getCapacity());
        response.setOccupied(room.isOccupied());

        return response;
    }

    public List<RoomResponse> getAll() {
        log.debug("Request to get all rooms");

        List<Room> rooms = roomRepository.findAll();

        return rooms.stream().
                map(room -> new RoomResponse(room.getId(),
                        room.getName(),
                        room.getCapacity(),
                        room.isOccupied()))
                .collect(Collectors.toList());
    }

    public RoomResponse getById(Long id) {
        log.debug("Request to get room : {}", id);

        Optional<Room> optional = roomRepository.findById(id);
        if(optional.isEmpty()){
            throw new ApplicationException(ApplicationError.ROOM_NOT_FOUND);
        }

        Room room = optional.get();

        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setCapacity(room.getCapacity());
        response.setOccupied(room.isOccupied());

        return response;
    }

    public RoomResponse update(Long id, UpdateRoomRequest request) {
        log.debug("Request to update room : {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ApplicationError.INTERNAL_SERVER_ERROR));

        String userRole = SecurityUtils.currentLoggedInUserRole();
        if (userRole.equals(UserRole.USER.name())) {
            String username = SecurityUtils.currentLoggedInUsername();

            if (!username.equals(room.getCreatedBy())) {
                throw new ApplicationException(ApplicationError.FORBIDDEN_ACCESS);
            }
        }

        if (!room.getName().equals(request.getName())) {
            room.setName(request.getName());
        }
        if (room.getCapacity() != request.getCapacity()) {
            room.setCapacity(request.getCapacity());
        }
        if (room.isOccupied() != request.isOccupied()) {
            room.setOccupied(request.isOccupied());
        }
        roomRepository.save(room);

        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setCapacity(room.getCapacity());
        response.setOccupied(room.isOccupied());

        return response;
    }

    public String delete(Long id) {
        log.debug("Request to delete room : {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ApplicationError.ROOM_NOT_FOUND));

        roomRepository.delete(room);

        return "Success";
    }
}
