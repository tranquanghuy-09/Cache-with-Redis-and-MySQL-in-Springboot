package vn.edu.iuh.fit.sbrediscacheh2db.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.sbrediscacheh2db.models.User;
import vn.edu.iuh.fit.sbrediscacheh2db.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //    @PostMapping("users")
//    public User addUser(@RequestBody User user){
//        return userRepository.save(user);
//    }
    @PostMapping("/users")
    @CachePut(value = "users", key = "#result.id")
    public User addEmployee(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/users/{userId}")
    @Cacheable(value = "users",key = "#userId")
    public User findUserByID(@PathVariable(value = "userId") long userId){
        System.out.println("fetching from database >>>>>>>>>>" + userId);
        return userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User_id "+ userId +" not found"));
    }

    @PutMapping("/users/{userId}")
    @CachePut(value = "users", key = "#userId")
    public User updateUser(@PathVariable(value = "userId") long userId, @RequestBody User user){
        User userUpdate = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        userUpdate.setName(user.getName());
        return userRepository.save(userUpdate);
    }

    @DeleteMapping("/users/{userId}")
    @CacheEvict(value = "users")
    public void deleteUser(@PathVariable(value = "userId") long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        userRepository.delete(user);
        System.out.println("Delete complete!");
    }
}
