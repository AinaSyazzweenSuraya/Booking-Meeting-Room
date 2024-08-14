package com.bookingmeetingroom.dto;

import com.bookingmeetingroom.entity.MeetingRoomEntity;

public class MeetingRoomPostResponse {
    private MeetingRoomEntity meetingRoom;

    public MeetingRoomEntity getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoomEntity meetingRoom) {
        this.meetingRoom = meetingRoom;
    }
}
