package com.djava.meetingRoom.security;

import com.djava.meetingRoom.entity.User;
import com.djava.meetingRoom.repository.UserRepository;
import com.djava.meetingRoom.service.error.ApplicationError;
import com.djava.meetingRoom.service.error.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ApplicationError.USER_NOT_FOUND));

        List<String> roles = List.of(user.getRole());

        return SecurityUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
    }
}
