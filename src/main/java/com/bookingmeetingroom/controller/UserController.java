package com.bookingmeetingroom.controller;

import com.bookingmeetingroom.dto.*;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.exceptions.ApplicationException;
import com.bookingmeetingroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /*@PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            UserEntity user = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok("Login successful for user: " + user.getUsername());
        } catch (ApplicationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }*/

    @PostMapping
    public UserPostResponse createUsers(@RequestBody UserPostRequest userPostRequest) {
        UserEntity insertedUsers = userService.add(userPostRequest);
        UserPostResponse resp = new UserPostResponse();
        resp.setUsers(insertedUsers);

        return resp;
    }

    @GetMapping("/{id}")
    public UsersDetail getUser(@PathVariable Long id){
        return userService.fetch(id);
    }

    @GetMapping()
    public List<UsersDetail> getAllUsers(){
        return userService.fetchAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{usernameToDelete}")
    public ResponseEntity<Void> deleteUser(@PathVariable String usernameToDelete,
                                           @RequestParam String username) {
        userService.deleteUser(username, usernameToDelete);
        return ResponseEntity.noContent().build();
    }
}
