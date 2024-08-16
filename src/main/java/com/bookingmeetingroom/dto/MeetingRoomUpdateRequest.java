package com.bookingmeetingroom.dto;

import jakarta.validation.constraints.NotBlank;

public class MeetingRoomUpdateRequest {

    @NotBlank(message = "name is required")
    private String name;
    private int capacity;
    private RoomStatus isOccupied;
    Long userId;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomStatus getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(RoomStatus isOccupied) {
        this.isOccupied = isOccupied;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
