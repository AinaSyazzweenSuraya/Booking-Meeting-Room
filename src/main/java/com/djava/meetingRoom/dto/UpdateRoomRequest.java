package com.djava.meetingRoom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoomRequest {
    private String name;
    private Integer capacity;
    private boolean occupied;

    @NotNull(message = "userId is required")
    private Long userId;
}
