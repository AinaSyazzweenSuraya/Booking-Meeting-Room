package com.bookingmeetingroom.service;

import com.bookingmeetingroom.dto.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.exceptions.ApplicationException;
import com.bookingmeetingroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //login method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public UserEntity authenticateUser(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ApplicationException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ApplicationException("Invalid username or password");
        }

        return user;
    }

    //method add
    public UserEntity add(UserPostRequest userPostRequest) {
        UserEntity users = new UserEntity();
        users.setCreatedBy("admin");
        users.setCreatedAt(LocalDateTime.now());
        users.setUsername(userPostRequest.getUsername());
        users.setPassword(userPostRequest.getPassword());
        users.setType(userPostRequest.getType());

        return userRepository.save(users);
    }

    //method fetch
    public UsersDetail fetch(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        UserEntity user = userOptional
                .orElseThrow(() -> new ApplicationException("User not found"));

        UsersDetail usersDetail = new UsersDetail();
        usersDetail.setUsername(user.getUsername());
        usersDetail.setType(user.getType());
        return usersDetail;
    }

    //method update
    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException("User not found"));

        if (userUpdateRequest.getUsername() != null) {
            user.setUsername(userUpdateRequest.getUsername());
        }
        if (userUpdateRequest.getPassword() != null) {
            user.setPassword(userUpdateRequest.getPassword());
        }
        if (userUpdateRequest.getType() != null) {
            user.setType(userUpdateRequest.getType());
        }

        userRepository.save(user);
    }

    //method fetch all utk admin
    public List<UsersDetail> fetchAll() {
        List<UserEntity> allUsers = userRepository.findAll();

        return allUsers.stream()
                .map(userEntity -> new UsersDetail(userEntity.getUsername(),
                        userEntity.getPassword(),
                        userEntity.getType()))
                .collect(Collectors.toList());
    }

    public boolean isAdmin(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException("User not found"));
        return "admin".equals(user.getType());
    }

    //method delete
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ApplicationException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
