package com.djava.meetingRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long roomId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String bookedBy;
    private String status;
}
