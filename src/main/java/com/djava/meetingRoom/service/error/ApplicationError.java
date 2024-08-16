package com.djava.meetingRoom.service.error;

public enum ApplicationError {
    // User Errors 1000 - 2000
    USER_NOT_FOUND(1000, "User not found"),
    USER_IS_NOT_ADMIN(1001, "User is not an admin"),

    // Room Errors 2000 - 3000
    ROOM_NOT_FOUND(2000,"Room not found"),
    ROOM_IS_OCCUPIED(2001, "Room is occupied"),

    // Register Errors 3000 - 4000
    MUST_BE_ADMIN(3000, "Type must be ADMIN"),
    MUST_BE_USER(3001, "Type must be USER"),

    // Booking Errors 4000 - 5000
    BOOKING_NOT_FOUND(4000, "Booking not found"),
    BOOKING_ALREADY_APPROVED(4001, "Booking already approved"),;

    private int code;
    private String message;

    ApplicationError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
