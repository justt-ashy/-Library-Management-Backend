package com.example.LMS_Backend.service;

import com.example.LMS_Backend.model.User;
import com.example.LMS_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<User> getAllUsers(){
        return userRepository.findAllByOrderByDisplayNameAsc();
    }

    public List<User> getAllActiveUsers(){
        return userRepository.findAllByActiveOrderByDisplayNameAsc(1);
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User getById(Long id){
        return userRepository.findById(id).get();
    }

    public User addNew(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(new Date());
        user.setLastModifiedDate(user.getCreatedDate());
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

    public Optional<User> getUserByUsername(String username){
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public List<User> getAll(){
        return userRepository.findAllByActiveOrderByDisplayNameAsc(1);
    }

    public User get(Long id){
        return userRepository.findById(id).orElse(null);
    }


}
