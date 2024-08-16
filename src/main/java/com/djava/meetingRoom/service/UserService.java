package com.djava.meetingRoom.service;

import com.djava.meetingRoom.dto.UserResponse;
import com.djava.meetingRoom.entity.User;
import com.djava.meetingRoom.repository.UserRepository;
import com.djava.meetingRoom.service.error.ApplicationError;
import com.djava.meetingRoom.service.error.ApplicationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream().
                        map(user -> new UserResponse(user.getId(),
                        user.getUsername(),
                        user.getType()))
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ApplicationException(ApplicationError.USER_NOT_FOUND);
        }

        User user = optional.get();

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setType(user.getType());

        return response;
    }



}
