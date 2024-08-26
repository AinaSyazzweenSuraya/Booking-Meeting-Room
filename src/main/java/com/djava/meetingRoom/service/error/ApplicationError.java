package com.djava.meetingRoom.service.error;

import lombok.Getter;

@Getter
public enum ApplicationError {
    // General
    MISSING_AUTHORIZATION_HEADER(100, "Missing Authorization header"),
    INVALID_AUTHORIZATION_HEADER(101, "Invalid Authorization header"),
    INTERNAL_SERVER_ERROR(102, "Internal server error"),
    FORBIDDEN_ACCESS(103, "Forbidden access"),

    // User Errors 1000 - 2000
    USER_NOT_FOUND(1000, "User not found"),
    USER_ROLE_NOT_FOUND(1001, "User role not found"),
    USER_ALREADY_EXISTS(1002, "User already exists"),
    INVALID_USER_ROLE(1003, "Invalid user role"),

    // Room Errors 2000 - 3000
    ROOM_NOT_FOUND(2000,"Room not found"),
    ROOM_IS_OCCUPIED(2001, "Room is occupied"),

    // Register Errors 3000 - 4000
    MUST_BE_ADMIN(3000, "Type must be ADMIN"),
    MUST_BE_USER(3001, "Type must be USER"),

    // Booking Errors 4000 - 5000
    BOOKING_NOT_FOUND(4000, "Booking not found"),
    BOOKING_ALREADY_APPROVED(4001, "Booking already approved"),;

    private final int code;
    private final String message;

    ApplicationError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
