package com.bookingmeetingroom.dto;

public class MeetingRoomUpdateRequest {

    private String name;
    private int capacity;
    private RoomStatus isOccupied;

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
}
