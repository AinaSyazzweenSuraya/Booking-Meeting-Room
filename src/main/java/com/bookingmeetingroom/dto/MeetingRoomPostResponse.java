package com.bookingmeetingroom.dto;

import com.bookingmeetingroom.entity.MeetingRoomEntity;

public class MeetingRoomPostResponse {
    private MeetingRoomEntity meetingRoom;
    private String message;

    public MeetingRoomEntity getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoomEntity meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
