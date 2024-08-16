package com.djava.meetingRoom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteRoomRequest {
    @NotNull(message = "userId is required")
    private Long userId;
}
