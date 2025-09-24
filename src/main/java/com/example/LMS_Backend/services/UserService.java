package com.example.LMS_Backend.services;

import com.example.LMS_Backend.model.User;
import com.example.LMS_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User signup(User user) {
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> updateById(Long id, User updated) {
        return userRepository.findById(id).map(existing -> {
            if (updated.getUsername() != null) {
                existing.setUsername(updated.getUsername());
            }
            if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
                existing.setPassword(passwordEncoder.encode(updated.getPassword()));
            }
            if (updated.getRole() != null) {
                existing.setRole(updated.getRole());
            }
            if (updated.getDisplayName() != null) {
                existing.setDisplayName(updated.getDisplayName());
            }
            if (updated.getActive() != null) {
                existing.setActive(updated.getActive());
            }
            return userRepository.save(existing);
        });
    }

    public boolean deleteByIdIfExists(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
