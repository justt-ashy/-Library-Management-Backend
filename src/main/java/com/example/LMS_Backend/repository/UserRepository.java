package com.example.LMS_Backend.repository;

import com.example.LMS_Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findAllByOrderByDisplayNameAsc();

    public List<User> findAllActiveOrderByDisplayNameAsc(Integer active);

    // Find a user by username (for login/authentication)
    public User findByUsername(String username);
}
