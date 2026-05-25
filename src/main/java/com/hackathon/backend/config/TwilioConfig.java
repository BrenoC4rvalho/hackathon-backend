package com.hackathon.backend.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    private final TwilioProperties properties;

    public TwilioConfig(TwilioProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        String sid = properties.accountSid();
        String token = properties.authToken();
        if (sid != null && !sid.isBlank() && token != null && !token.isBlank()) {
            Twilio.init(sid, token);
        }
    }
}