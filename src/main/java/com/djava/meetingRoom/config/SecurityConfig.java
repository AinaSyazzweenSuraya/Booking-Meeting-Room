package com.djava.meetingRoom.config;

import com.djava.meetingRoom.common.UserRole;
import com.djava.meetingRoom.security.CustomUserDetailService;
import com.djava.meetingRoom.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    public SecurityConfig(CustomUserDetailService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authz ->
                                // prettier-ignore
                                authz
                                        .requestMatchers("/api/auth/**").permitAll()
                                        // only admin can get all users
                                        .requestMatchers(HttpMethod.GET,"/api/users").hasAuthority(UserRole.ADMIN.name())
                                        // only admin can create room
                                        .requestMatchers(HttpMethod.POST,"/api/rooms").hasAuthority(UserRole.ADMIN.name())
                                        // only admin can update room
                                        .requestMatchers(HttpMethod.PUT,"/api/rooms").hasAuthority(UserRole.ADMIN.name())
                                        // only admin can delete room
                                        .requestMatchers(HttpMethod.DELETE,"/api/rooms").hasAuthority(UserRole.ADMIN.name())
                                        // only admin can approve bookings
                                        .requestMatchers(HttpMethod.PUT,"/api/bookings/approve/**").hasAuthority(UserRole.ADMIN.name())
                                        .anyRequest()
                                        .authenticated()
                                      )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider())
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
