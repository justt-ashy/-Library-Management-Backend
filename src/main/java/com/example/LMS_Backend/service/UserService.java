package com.example.LMS_Backend.service;

import com.example.LMS_Backend.model.User;
import com.example.LMS_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.Setter;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Setter
@Getter
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User addNew(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getCreatedDate(new Date());
        user.getLastModifiedDate(user.getCreatedDate());
        user.setActive(1);
        return userRepository.save(user);
    }

    public User update(User user){
        user.setLastModifiedDate(new Date());
        return userRepository.save(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }
}
