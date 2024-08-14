package com.bookingmeetingroom.service;

import com.bookingmeetingroom.dto.UsersDetail;
import com.bookingmeetingroom.entity.UserEntity;
import com.bookingmeetingroom.dto.UserPostRequest;
import com.bookingmeetingroom.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity add(UserPostRequest userPostRequest) {
        UserEntity users = new UserEntity();
        users.setCreatedBy("admin");
        users.setCreatedAt(LocalDateTime.now());
        users.setUsername(userPostRequest.getUsername());
        users.setPassword(userPostRequest.getPassword());
        users.setType(userPostRequest.getType());

        return userRepository.save(users);
    }

    public UsersDetail fetch(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        UserEntity user;

        if(userOptional.isPresent()){
            user = userOptional.get();
        }
        else{
            user = null;
        }

        UsersDetail usersDetail = new UsersDetail();
        usersDetail.setUsername(user.getUsername());
        usersDetail.setType(user.getType());
        return usersDetail;
    }

    public List<UsersDetail> fetchAll() {
        List<UserEntity> allUsers = userRepository.findAll();

        return allUsers.stream().
                map(userEntity -> new UsersDetail(userEntity.getUsername(),
                        userEntity.getPassword(),
                        userEntity.getType()))
                .collect(Collectors.toList());
    }

}
