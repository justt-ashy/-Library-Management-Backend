package com.example.LMS_Backend.configuration;

import com.example.LMS_Backend.common.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

    /**
     Password encoder bean:- => Spring Security requires passwords to be stored in an encoded format. Here we use BCrypt (a strong hashing function).
        */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     Security filter chain:- It defines how authentication and authorization should work.
        */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                /**
                 Authorization rules:-
                    =>Allow everyone to access /login
                    => Restrict all other endpoints (/**) to ADMIN and LIBRARIAN roles
                    => Any other request must be authenticated
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/rest/**").permitAll()  // Allow REST endpoints for frontend
                        .requestMatchers("/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_LIBRARIAN)
                        .anyRequest().authenticated()
                )

                /**
                 CSRF protection:-
                    => Disabled here (for simplicity).
                    => In real apps, it should usually be enabled.
                 */
                .csrf(csrf -> csrf.disable())

                /**
                 Login configuration:-
                    => Custom login page: /login
                    => Redirect to /login?error=true on failure
                    => On success, redirect to /home
                    => Username field = "username"
                    => Password field = "password"
                 */
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/home", true)
                        .usernameParameter("username")
                        .passwordParameter("password")
                )


                /**
                 * Logout configuration:-
                    => Logout URL: /logout
                    => After logout, redirect to /
                 */
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                )


                /**
                 Exception handling:- If user tries to access a forbidden page, redirect to /access-denied
                    */
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}
