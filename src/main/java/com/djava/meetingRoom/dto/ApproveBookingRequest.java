package com.djava.meetingRoom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApproveBookingRequest {
    @NotNull(message = "roomId is required")
    private Long roomId;

    @NotNull(message = "userId is required")
    private Long userId;
}
