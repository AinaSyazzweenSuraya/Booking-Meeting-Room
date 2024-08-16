package com.djava.meetingRoom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBookingRequest {
    @NotNull(message = "roomId is required")
    private Long roomId;

    @NotBlank(message = "start is required")
    private String start;

    @NotBlank(message = "end is required")
    private String end;

    @NotNull(message = "userId is required")
    private Long userId;
}
