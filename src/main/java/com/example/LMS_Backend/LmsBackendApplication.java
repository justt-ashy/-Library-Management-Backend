package com.example.LMS_Backend;

import com.example.LMS_Backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class LmsBackendApplication {
//	public static void main(String[] args) {
//        SpringApplication.run(LmsBackendApplication.class, args);
//	}
//}
@SpringBootApplication
public class LmsBackendApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(LmsBackendApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // This runs right after the app starts
        System.out.println("Application started");

        // Example: check if "admin" user exists
        userService.getUserByUsername("admin").ifPresent(user -> {
            if (user.getPassword().equals("admin123")) {
                System.out.println("Login successful for: " + user.getUsername());
            } else {
                System.out.println("Invalid password");
            }
        });
    }
}
