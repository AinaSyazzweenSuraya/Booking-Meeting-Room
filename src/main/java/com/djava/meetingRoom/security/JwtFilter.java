package com.djava.meetingRoom.security;

import com.djava.meetingRoom.config.ApplicationProperties;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final ApplicationProperties appProp;
    private final CustomUserDetailService userDetailsService;

    public JwtFilter(ApplicationProperties appProp, CustomUserDetailService userDetailsService) {
        this.appProp = appProp;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> whitelistAll = List.of(
                "/api/auth"
                                           );
        // ignore jwt authorization checking for whitelisted endpoints
        for (String path : whitelistAll) {
            if (request.getServletPath().contains(path)) {
                log.debug("Whitelisting request for path {}", path);
                filterChain.doFilter(request, response);
                return;
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwt = SecurityUtils.getJwtFromHeader(request, response);
        if (authentication == null && jwt != null) {
            Claims claims = SecurityUtils.extractAllClaims(jwt, appProp.getJwt().getSecret());
            String username = claims.getSubject();

            UserDetails user = userDetailsService.loadUserByUsername(username);
            SecurityUtils.loadUserContext(user, request);

            filterChain.doFilter(request, response);
        }
    }
}
