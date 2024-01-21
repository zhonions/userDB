package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/{search}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<User> findByName(@RequestParam(required = false) String name){
        return userService.findByName(name);
    }


    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable final Long userId){
        userService.deleteUserById(userId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserById(@RequestBody User user, @PathVariable final Long userId){
        return userService.updateUserById(user,userId);
    }



}
