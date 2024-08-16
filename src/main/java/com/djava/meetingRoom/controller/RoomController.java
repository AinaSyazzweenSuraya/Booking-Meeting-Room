package com.djava.meetingRoom.controller;

import com.djava.meetingRoom.dto.RoomResponse;
import com.djava.meetingRoom.dto.CreateRoomRequest;
import com.djava.meetingRoom.dto.DeleteRoomRequest;
import com.djava.meetingRoom.dto.UpdateRoomRequest;
import com.djava.meetingRoom.service.RoomService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Slf4j
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public RoomResponse createRoom(@Valid @RequestBody CreateRoomRequest request) {
        log.debug("REST request to get create room: {}", request);

        return roomService.create(request);
    }

    @GetMapping()
    public List<RoomResponse> getAllRooms(){
        log.debug("REST request to get all rooms");

        return roomService.getAll();
    }

    @GetMapping("/{id}")
    public RoomResponse getRoom(@PathVariable Long id){
        log.debug("REST request to get room by id: {}", id);

        return roomService.getById(id);
    }

    @PutMapping("/{id}")
    public RoomResponse updateRoom(@PathVariable Long id, @Valid @RequestBody UpdateRoomRequest request) {
        log.debug("REST request to update room : {}", request);

        return roomService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable Long id, @Valid @RequestBody DeleteRoomRequest request) {
        log.debug("REST request to delete room : {}", request);

        return roomService.delete(id, request);
    }

}
