package com.djava.meetingRoom.security;

import com.djava.meetingRoom.entity.User;
import com.djava.meetingRoom.service.error.ApplicationError;
import com.djava.meetingRoom.service.error.ApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class SecurityUtils {
    private static final String AUTH_HEADER_KEY = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

    public static String getJwtFromHeader(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader(AUTH_HEADER_KEY);

        if (authorization == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            sendFilterError(response, ApplicationError.MISSING_AUTHORIZATION_HEADER);
            return null;
        }

        if (!authorization.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            sendFilterError(response, ApplicationError.INVALID_AUTHORIZATION_HEADER);
            return null;
        }

        return authorization.substring(AUTH_HEADER_VALUE_PREFIX.length());
    }

    private static void sendFilterError(HttpServletResponse response, ApplicationError error) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("code", error.getCode());
            map.put("message", error.getMessage());

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(map);

            response.setContentType("application/json");
            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (Exception ignored) {}
    }

    public static void loadUserContext(UserDetails user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                user.getPassword(),
                user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public static String generateJWT(User user, String jwtSecret, String expirationMinutes) {
        Instant now = Instant.now();
        Instant expired = now.plus(Long.parseLong(expirationMinutes), ChronoUnit.MINUTES);
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expired))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .compact();
    }

    public static String currentLoggedInUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            throw new ApplicationException(ApplicationError.INTERNAL_SERVER_ERROR);
        }

        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        return user.getUsername();
    }

    public static String currentLoggedInUserRole() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            throw new ApplicationException(ApplicationError.INTERNAL_SERVER_ERROR);
        }

        return authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ApplicationError.USER_ROLE_NOT_FOUND))
                .getAuthority();
    }

    public static Claims extractAllClaims(String jwt, String jwtSecret) {
        return Jwts.parser()
                .verifyWith(getSignInKey(jwtSecret))
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private static SecretKey getSignInKey(String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
