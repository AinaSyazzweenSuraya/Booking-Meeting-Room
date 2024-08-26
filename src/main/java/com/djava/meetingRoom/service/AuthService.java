package com.djava.meetingRoom.service;

import com.djava.meetingRoom.common.UserRole;
import com.djava.meetingRoom.config.ApplicationProperties;
import com.djava.meetingRoom.dto.LoginRequest;
import com.djava.meetingRoom.dto.LoginResponse;
import com.djava.meetingRoom.dto.RegisterRequest;
import com.djava.meetingRoom.dto.RegisterResponse;
import com.djava.meetingRoom.entity.User;
import com.djava.meetingRoom.repository.UserRepository;
import com.djava.meetingRoom.security.SecurityUtils;
import com.djava.meetingRoom.service.error.ApplicationError;
import com.djava.meetingRoom.service.error.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationProperties appProp;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationProperties appProp) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.appProp = appProp;
    }

    public RegisterResponse register(RegisterRequest request) {
        List<String> validRoles = List.of(UserRole.ADMIN.name(), UserRole.USER.name());
        if (!validRoles.contains(request.getRole().toUpperCase())) {
            throw new ApplicationException(ApplicationError.INVALID_USER_ROLE);
        }

        Optional<User> optional = userRepository.findByUsername(request.getUsername());
        if (optional.isPresent()) {
            throw new ApplicationException(ApplicationError.USER_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());

        user = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setType(user.getRole());

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> optional = userRepository.findByUsername(request.getUsername());
        if (optional.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        User user = optional.get();
        String jwt = SecurityUtils.generateJWT(user, appProp.getJwt().getSecret(), appProp.getJwt().getExpirationMinutes());

        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setToken(jwt);

        return response;
    }
}
