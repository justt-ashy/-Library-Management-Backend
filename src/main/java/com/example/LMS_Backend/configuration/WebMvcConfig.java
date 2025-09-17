package com.example.LMS_Backend.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * WebMvcConfig:- This class is used to define web-related configurations for the application.
    Here, we mainly define a password encoder bean so that Spring Security can use it whenever passwords are stored or verified.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Password Encoder Bean:-
        => Spring Security requires a PasswordEncoder to securely store passwords.
        => BCrypt is a hashing algorithm designed for password security.
        => By registering it as a @Bean, Spring can inject it wherever needed.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }
}
