package com.bookingmeetingroom.service;

import com.bookingmeetingroom.dto.*;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.exceptions.ApplicationException;
import com.bookingmeetingroom.repository.ReservationRepository;
import com.bookingmeetingroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    //login method
    /*@Override
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
    }*/

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

    @Transactional
    public void deleteUser(String username, String usernameToDelete) {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException("User not found 1"));

        //admin can delete all, user can only delete his own account
        if ("admin".equals(user.getType())) {
            deleteAccount(usernameToDelete);
        } else if (username.equals(usernameToDelete)) {
            deleteAccount(usernameToDelete);
        } else {
            throw new ApplicationException("Unauthorized to delete this account");
        }
    }

    private void deleteAccount(String usernameToDelete) {

        UserEntity userToDelete = userRepository.findByUsername(usernameToDelete)
                .orElseThrow(() -> new ApplicationException("User not found 2"));

        if (reservationRepository.existsByUserId(userToDelete.getId())) {
            throw new ApplicationException("User cannot be deleted because they have existing reservations");
        }

        reservationRepository.deleteByUserId(userToDelete.getId());
        userRepository.deleteByUsername(usernameToDelete);

        System.out.println("User " + usernameToDelete + " deleted successfully.");
    }
}
