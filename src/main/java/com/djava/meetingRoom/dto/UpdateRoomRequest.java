package com.djava.meetingRoom.dto;

import lombok.Data;

@Data
public class UpdateRoomRequest {
    private String name;
    private Integer capacity;
    private boolean occupied;
}
