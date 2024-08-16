package com.djava.meetingRoom.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Audit {
    private String createdBy;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String updatedBy;
    private LocalDateTime updatedAt = LocalDateTime.now();
}
