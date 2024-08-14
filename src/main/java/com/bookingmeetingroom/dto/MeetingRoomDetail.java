package com.bookingmeetingroom.dto;

public class MeetingRoomDetail {
    private String name;
    private int capacity;
    private RoomStatus isOccupied;

    public MeetingRoomDetail() {

    }

    public MeetingRoomDetail(String name, int capacity, RoomStatus isOccupied) {
        this.name = name;
        this.capacity = capacity;
        this.isOccupied = isOccupied;
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
        return "MeetingRoomDetail{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", isOccupied=" + isOccupied +
                '}';
    }
}
