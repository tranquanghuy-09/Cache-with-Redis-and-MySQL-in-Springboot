package vn.edu.iuh.fit.sbrediscacheh2db.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import vn.edu.iuh.fit.sbrediscacheh2db.models.User;
import vn.edu.iuh.fit.sbrediscacheh2db.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private Jedis jedis = new Jedis();

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //    @PostMapping("users")
//    public User addUser(@RequestBody User user){
//        return userRepository.save(user);
//    }
    @PostMapping("/users")
//    @CachePut(value = "users", key = "#result.id")
    public User addEmployee(@RequestBody User user) {
        jedis.set(String.valueOf(user.getId()), user.getName());
        System.out.println("saved in cache");
        return userRepository.save(user);
    }

    @GetMapping("/users/{userId}")
//    @Cacheable(value = "users",key = "#userId")
    public User findUserByID(@PathVariable(value = "userId") long userId){
        User user = new User();
        System.out.println("fetching from database >>>>>>>>>>" + userId);
        user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User_id "+ userId +" not found"));
        String key = String.valueOf(user.getId());
        jedis.set(key, user.getName());
        System.out.println("saved in cache");
        return user;
    }

    @PutMapping("/users/{userId}")
//    @CachePut(value = "users", key = "#userId")
    public User updateUser(@PathVariable(value = "userId") long userId, @RequestBody User user){
        User userUpdate = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        userUpdate.setName(user.getName());
        jedis.set(String.valueOf(user.getId()), user.getName());
        System.out.println("saved in cache");
        return userRepository.save(userUpdate);
    }

    @DeleteMapping("/users/{userId}")
//    @CacheEvict(value = "users")
    public void deleteUser(@PathVariable(value = "userId") long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        userRepository.delete(user);
        jedis.del(String.valueOf(user.getId()));
        System.out.println("delete in cache");
        System.out.println("Delete complete!");
    }
}
