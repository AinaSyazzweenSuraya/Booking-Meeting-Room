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

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestParam Long requesterId) {

        try {
            if (userService.isAdmin(requesterId) || requesterId.equals(id)) {
                userService.deleteUser(id);
                return ResponseEntity.ok("User deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this user");
            }
        } catch (ApplicationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request");
        }
    }
}
