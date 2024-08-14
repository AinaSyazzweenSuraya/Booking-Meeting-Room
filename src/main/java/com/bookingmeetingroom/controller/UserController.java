package com.bookingmeetingroom.controller;

import com.bookingmeetingroom.dto.UserPostResponse;
import com.bookingmeetingroom.dto.UserPostRequest;
import com.bookingmeetingroom.dto.UsersDetail;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserPostResponse createUsers(@RequestBody UserPostRequest userPostRequest) {
        //create/insert users
        UserEntity insertedUsers = userService.add(userPostRequest);
        UserPostResponse resp = new UserPostResponse();
        resp.setUsers(insertedUsers);

        return resp;
    }

    @GetMapping("{id}")
    public UsersDetail getUser(@PathVariable Long id){
        return userService.fetch(id);
    }

    @GetMapping()
    public List<UsersDetail> getAllUsers(){
        return userService.fetchAll();
    }
}
