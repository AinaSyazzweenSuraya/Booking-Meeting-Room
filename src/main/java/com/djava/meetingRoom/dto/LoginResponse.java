package com.djava.meetingRoom.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private String token;
}
