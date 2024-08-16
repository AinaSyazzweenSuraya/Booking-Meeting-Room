package com.djava.meetingRoom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateBookingRequest {
    private Long roomId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;

    @NotNull(message = "userId is required")
    private Long userId;

}
