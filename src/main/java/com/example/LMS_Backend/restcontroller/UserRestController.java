package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.model.User;
import com.example.LMS_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="/rest/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping(value={"/","/list"})
    public List<User> all(){
        return userService.getAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name="id") Long id){
        try {
            User user = userService.get(id);
            if(user != null){
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value="/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable(name="username") String username){
        Optional<User> user = userService.getUserByUsername(username);
        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/save")
    public ResponseEntity<String> saveUser(@RequestBody User user){
        try {
            userService.addNew(user);
            return ResponseEntity.ok("User saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving user: " + e.getMessage());
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<String> updateUser(@PathVariable(name="id") Long id, @RequestBody User user){
        try {
            user.setId(id);
            userService.update(user);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name="id") Long id){
        try {
            userService.delete(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting user: " + e.getMessage());
        }
    }
}
