package com.djava.meetingRoom.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateBookingRequest {
    private Long roomId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
}
