package com.bookingmeetingroom.dto;

public class MeetingRoomPostRequest {

    private String name;
    private int capacity;
    private RoomStatus isOccupied;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    @Override
    public String toString() {
        return "MeetingRoomPostRequest{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", isOccupied=" + isOccupied +
                '}';
    }
}
