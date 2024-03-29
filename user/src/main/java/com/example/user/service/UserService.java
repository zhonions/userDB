package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.exceptions.UserIdNotFoundException;
import com.example.user.exceptions.UserInvalidAttributesException;
import com.example.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        if (isUserInvalid(user)) {
            log.error("One or more user attributes was empty");
            throw new UserInvalidAttributesException("One or more user attributes was empty");
        }
        return this.userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void deleteUserById(final Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            this.userRepository.deleteById(userId);
        }else{
            log.error("No users found with this id: " + userId);
            throw new UserIdNotFoundException("User Not Found");
        }

    }
    public Optional<User> findUserById(final long id) {
        Optional<User> userList = this.userRepository.findById(id);

        log.info("Received request to find users with id:" + id);

        if (isNull(userList) || userList.isEmpty()) {
            log.error("No users found with this id:" + id);

            throw new UserIdNotFoundException("Users with name " + id + " do not exist");
        }

        return userList;
    }
    public List<User> findByName(String name) {
        List<User> userList = this.userRepository.findByName(name);

        log.info("Received request to find users with name " + name);

        if (isNull(userList) || userList.isEmpty()) {
            log.error("No users found with name " + name);

            throw new UserIdNotFoundException("Users with name " + name + " do not exist");
        }

        return userList;
    }


    public User updateUserById(User user, final Long userId) {
        if (user == null || user.getName() == null || user.getPassword() == null) {
            log.error("User or its properties cannot be null " + userId);
            throw new UserInvalidAttributesException("User or its properties cannot be null");
        }

        boolean userBlank = user.getName().isBlank() || user.getPassword().isBlank();
        boolean userEmpty = user.getName().isEmpty() || user.getPassword().isEmpty();

        if (userBlank || userEmpty) {
            log.error("Name or password cannot be empty or blank " + userId);
            throw new UserInvalidAttributesException("Name or password cannot be null, empty or blank");
        }

        if (userRepository.existsById(userId)) {
            User updatedUser = userRepository.getReferenceById(userId);
            updatedUser.setName(user.getName());
            updatedUser.setPassword(user.getPassword());
            return userRepository.save(updatedUser);
        } else {
            throw new UserInvalidAttributesException("User not found");
        }
    }
    private boolean isUserInvalid(User user) {
        return isNull(user) || Strings.isBlank(user.getPassword()) || Strings.isBlank(user.getName());
    }

}
