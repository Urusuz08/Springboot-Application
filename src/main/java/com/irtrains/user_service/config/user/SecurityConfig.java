package com.irtrains.user_service.config.user;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoderArg(){
        return new Argon2PasswordEncoder(
            16, 32, 1, 65536, 4
        );
    }
}