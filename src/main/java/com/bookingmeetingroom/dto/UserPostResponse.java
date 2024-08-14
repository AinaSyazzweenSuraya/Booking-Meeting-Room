package com.bookingmeetingroom.dto;

import com.bookingmeetingroom.entity.UserEntity;

public class UserPostResponse {
    private UserEntity users;

    public UserEntity getUsers() {
        return users;
    }

    public void setUsers(UserEntity users) {
        this.users = users;
    }
}
