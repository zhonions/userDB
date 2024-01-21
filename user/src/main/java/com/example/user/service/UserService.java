package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        return this.userRepository.save(user);

    }
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void deleteUserById(final Long userId) {
        this.userRepository.deleteById(userId);
    }

    public List<User> findByName(String name) {
        return this.userRepository.findByName(name);
    }

    public User updateUserById( User user, final Long userId){
        User updatedUser = userRepository.getReferenceById(userId);
        updatedUser.setName(user.getName());
        updatedUser.setPassword(user.getPassword());
        return userRepository.save(updatedUser);
    }



}
