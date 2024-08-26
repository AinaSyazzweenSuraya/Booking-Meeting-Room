package com.djava.meetingRoom.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableJpaAuditing
public class AuditConfig implements AuditorAware<String> {

    public static final String DEFAULT = "system";

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(DEFAULT);
        }

        if (authentication.getName().equals("anonymousUser")) {
            return Optional.of(DEFAULT);
        }

        return Optional.of(authentication.getName());
    }
}
