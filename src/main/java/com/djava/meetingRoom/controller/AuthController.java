package com.djava.meetingRoom.controller;

import com.djava.meetingRoom.dto.LoginRequest;
import com.djava.meetingRoom.dto.LoginResponse;
import com.djava.meetingRoom.dto.RegisterRequest;
import com.djava.meetingRoom.dto.RegisterResponse;
import com.djava.meetingRoom.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        log.debug("REST request to register: {}", request);

        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.debug("REST request to login: {}", request);

        return authService.login(request);
    }
}
