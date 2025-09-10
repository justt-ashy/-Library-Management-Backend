package com.example.LMS_Backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Security rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll() // public endpoints
                        .anyRequest().authenticated() // everything else needs login
                )
                .httpBasic(); // use HTTP Basic authentication
        return http.build();
    }

    // Temporary in-memory user (later we replace with DB users)
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin123") // {noop} means no password encoding
                .roles("ADMIN")
                .build();

        UserDetails student = User.withUsername("student")
                .password("{noop}stud123")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, student);
    }
}
