package com.djava.meetingRoom.service;

import com.djava.meetingRoom.dto.LoginRequest;
import com.djava.meetingRoom.dto.LoginResponse;
import com.djava.meetingRoom.dto.RegisterRequest;
import com.djava.meetingRoom.dto.RegisterResponse;
import com.djava.meetingRoom.entity.User;
import com.djava.meetingRoom.repository.UserRepository;
import com.djava.meetingRoom.service.error.ApplicationError;
import com.djava.meetingRoom.service.error.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setType(request.getType());

        user = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setType(user.getType());

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> optional = userRepository.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        User user = optional.get();

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        // TODO: implement JWT token generation
        response.setToken("dummy jwt token");

        return response;
    }
}
