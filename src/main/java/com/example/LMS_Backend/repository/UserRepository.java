package com.example.LMS_Backend.repository;

import com.example.LMS_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by username (for login/authentication)
    Optional<User> findByUsername(String username);
}
