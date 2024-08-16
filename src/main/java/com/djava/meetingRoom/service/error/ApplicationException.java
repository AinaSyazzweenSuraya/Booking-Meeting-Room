package com.djava.meetingRoom.service.error;

public class ApplicationException extends RuntimeException {

    private final int code;
    private final String message;

    public ApplicationException(ApplicationError error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
