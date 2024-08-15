package com.bookingmeetingroom.controller;

import com.bookingmeetingroom.dto.MeetingRoomDetail;
import com.bookingmeetingroom.dto.MeetingRoomUpdateRequest;
import com.bookingmeetingroom.entity.MeetingRoomEntity;
import com.bookingmeetingroom.dto.MeetingRoomPostRequest;
import com.bookingmeetingroom.dto.MeetingRoomPostResponse;
import com.bookingmeetingroom.repository.MeetingRoomRepository;
import com.bookingmeetingroom.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    //nak create meeting rooms
    @PostMapping
    public MeetingRoomPostResponse createMeetingRooms(@Validated @RequestBody MeetingRoomPostRequest meetingRoomPostRequest) {

        // Proceed with the creation of the meeting room
        MeetingRoomEntity insertedMeetingRoom = meetingRoomService.add(meetingRoomPostRequest);
        MeetingRoomPostResponse resp = new MeetingRoomPostResponse();
        resp.setMeetingRoom(insertedMeetingRoom);

        return resp;
    }

    //Fetch All
    @GetMapping()
    public List<MeetingRoomDetail> getAllMeetingRooms(){
        return meetingRoomService.fetchAll();
    }

    //Fetch specific id
    @GetMapping("/{id}")
    public MeetingRoomDetail getMeetingRoomDetail(@PathVariable Long id){
        return meetingRoomService.fetch(id);
    }

    //nak susun data
    @GetMapping("/subset")
    public List<MeetingRoomDetail> subset(@RequestParam(defaultValue = "name") String order,
                                          @RequestParam(defaultValue = "asc") String direction,
                                          @RequestParam(defaultValue = "0") int page){
        return meetingRoomService.subset(order, direction, page);
    }

    //cara nk update
    @PutMapping("/{id} ")
    public MeetingRoomPostResponse updateMeetingRoom(@PathVariable Long id,
                                                     @Validated @RequestBody MeetingRoomUpdateRequest meetingRoomUpdateRequest,
                                                     @RequestParam Long userId) {

        MeetingRoomEntity updatedMeetingRoom = meetingRoomService.update(id, meetingRoomUpdateRequest, userId);

        MeetingRoomPostResponse resp = new MeetingRoomPostResponse();
        resp.setMeetingRoom(updatedMeetingRoom);
        resp.setMessage("Meeting room updated successfully");

        return resp;
    }

}
