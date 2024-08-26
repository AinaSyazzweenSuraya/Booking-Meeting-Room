package com.djava.meetingRoom.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "role is required")
    private String role;
}
