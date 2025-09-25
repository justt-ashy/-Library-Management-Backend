package com.example.LMS_Backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(com.example.LMS_Backend.repository.UserRepository userRepository) {
        return new com.example.LMS_Backend.security.UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of("http://localhost:8000"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setExposedHeaders(java.util.List.of("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
        // static and auth
        .requestMatchers("/", "/index.html", "/styles.css", "/app.js", "/favicon.ico").permitAll()
        .requestMatchers("/auth/signup", "/auth/login").permitAll()

        // USER can only view books/categories
        .requestMatchers(org.springframework.http.HttpMethod.GET, "/books/**", "/categories/**").hasAnyRole("USER","ADMIN")

        // ADMIN can modify books/categories
        .requestMatchers(org.springframework.http.HttpMethod.POST, "/books/**", "/categories/**").hasRole("ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.PUT,  "/books/**", "/categories/**").hasRole("ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.DELETE,"/books/**", "/categories/**").hasRole("ADMIN")

        // Issued-books: READ for USER/ADMIN, WRITE for ADMIN only
        .requestMatchers(org.springframework.http.HttpMethod.GET, "/issued-books/**").hasAnyRole("USER","ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.POST, "/issued-books/**").hasRole("ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.PUT,  "/issued-books/**").hasRole("ADMIN")
        .requestMatchers(org.springframework.http.HttpMethod.DELETE,"/issued-books/**").hasRole("ADMIN")
        .anyRequest().hasRole("ADMIN")
    )
    .httpBasic(org.springframework.security.config.Customizer.withDefaults())
    .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}