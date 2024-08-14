package com.bookingmeetingroom.controller;

import com.bookingmeetingroom.dto.MeetingRoomDetail;
import com.bookingmeetingroom.entity.MeetingRoomEntity;
import com.bookingmeetingroom.dto.MeetingRoomPostRequest;
import com.bookingmeetingroom.dto.MeetingRoomPostResponse;
import com.bookingmeetingroom.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;
    @PostMapping
    public MeetingRoomPostResponse createMeetingRooms(@RequestBody MeetingRoomPostRequest meetingRoomPostRequest){
        //create/insert tasks
        MeetingRoomEntity insertedMeetingRooms = meetingRoomService.add(meetingRoomPostRequest);
        MeetingRoomPostResponse resp = new MeetingRoomPostResponse();
        resp.setMeetingRoom(insertedMeetingRooms);

        return  resp;
    }

    @GetMapping("{id}")
    public MeetingRoomDetail getMeetingRoomDetail(@PathVariable Long id){
        return meetingRoomService.fetch(id);
    }

    @GetMapping()
    public List<MeetingRoomDetail> getAllMeetingRooms(){
        return meetingRoomService.fetchAll();
    }

    @GetMapping("/subset")
    public List<MeetingRoomDetail> subset(@RequestParam(defaultValue = "name") String order,
                                          @RequestParam(defaultValue = "asc") String direction,
                                          @RequestParam(defaultValue = "0") int page){
        return meetingRoomService.subset(order, direction, page);
    }
}
