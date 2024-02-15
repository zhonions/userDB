package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.exceptions.UserDoesNotMatchException;
import com.example.user.exceptions.UserIdNotFoundException;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        if (userList == null) {
            throw new UserIdNotFoundException("Lista de usuários não encontrada");
        }
        return userList;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable(required = true) long id) {
        Optional<User> userOptional = userService.findUserById(id);
        return userOptional
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable long id) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserById(@RequestBody User user, @PathVariable final Long id){
        if (!id.equals(user.getId())) {
            throw new UserDoesNotMatchException("User IDs do not match");
        }
        return userService.updateUserById(user, id);
    }
    }
