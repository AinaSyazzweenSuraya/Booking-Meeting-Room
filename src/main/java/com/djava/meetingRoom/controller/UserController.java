package com.djava.meetingRoom.controller;

import com.djava.meetingRoom.dto.UserResponse;
import com.djava.meetingRoom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserResponse> getAllUsers(){
        return userService.getAll();
    }

    @GetMapping("{id}")
    public UserResponse getUser(@PathVariable Long id){
        log.debug("REST request to get user by id: {}", id);

        return userService.getUserById(id);
    }
}
