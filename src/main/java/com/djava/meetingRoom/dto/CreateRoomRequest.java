package com.djava.meetingRoom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRoomRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "capacity is required")
    private Integer capacity;

    @NotNull(message = "userId is required")
    private Long userId;
}
