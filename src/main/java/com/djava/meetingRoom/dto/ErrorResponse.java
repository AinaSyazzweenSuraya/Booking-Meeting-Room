package com.djava.meetingRoom.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private int code;
    private String message;
}
