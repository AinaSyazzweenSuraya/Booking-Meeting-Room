package com.djava.meetingRoom.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        private String secret;
        private String expirationMinutes;
    }

}
