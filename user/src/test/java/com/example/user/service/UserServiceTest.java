package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.exceptions.UserIdNotFoundException;
import com.example.user.exceptions.UserInvalidAttributesException;
import com.example.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    @Test
    void testNullFromDBDoesNotReturnNull() {
        when(userRepository.findByName("manel")).thenReturn(null);

        assertThrows(UserIdNotFoundException.class, () -> userService.findByName("manel"));

        verify(userRepository, times(1)).findByName(any());
    }
    @Test
    void testNullFromFindByUserIdDBDoesNotReturnNull() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(null);

        assertThrows(UserIdNotFoundException.class, () -> userService.findUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    void testEmptyListFromDBThrowsException() {
        when(userRepository.findByName(any())).thenReturn(List.of());

        assertThrows(UserIdNotFoundException.class, () -> userService.findByName("manel"));
        verify(userRepository, times(1)).findByName(any());

    }
    @Test
    void testEmptyFindByIdFromDBThrowsException() {
        long userId = 1L;

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserIdNotFoundException.class, () -> userService.findUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    void testNotEmptyListFromDBSuccess() {
        User user = new User();
        user.setName("Manel");

        when(userRepository.findByName(any())).thenReturn(List.of(user));

        var userList = assertDoesNotThrow(() -> userService.findByName("manel"));

        assertFalse(userList.isEmpty());
        assertEquals("Manel", userList.get(0).getName());
        verify(userRepository, times(1)).findByName(any());

    }
    @Test
    void testFindByIdSuccess() {
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Manel");
        user.setPassword("pass");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> resultOptional = assertDoesNotThrow(() -> userService.findUserById(userId));

        assertTrue(resultOptional.isPresent());
        User result = resultOptional.get();

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getPassword(), result.getPassword());
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    void testCreateAccountUserWithEmptyAttributesFails() {
        User user = new User();
        user.setName("");
        user.setPassword("");

        assertThrows(UserInvalidAttributesException.class, () -> userService.createUser(user));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testCreateAccountSuccess() {
        User user = new User();
        user.setName("Nome");
        user.setPassword("password123");

        assertDoesNotThrow(() -> userService.createUser(user));
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testCreateAccountUserWithEmptyNameFails() {
        User user = new User();
        user.setName("");
        user.setPassword("password1234");

        assertThrows(UserInvalidAttributesException.class, () -> userService.createUser(user));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testCreateAccountUserWithEmptyPasswordFails() {
        User user = new User();
        user.setName("Nome");
        user.setPassword("");

        assertThrows(UserInvalidAttributesException.class, () -> userService.createUser(user));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testCreateAccountUserWithNoAttributesFails() {
        User user = new User();

        assertThrows(UserInvalidAttributesException.class, () -> userService.createUser(user));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void testDeleteNoInputId() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserIdNotFoundException.class, () -> userService.deleteUserById(1L));
        verify(userRepository, times(0)).save(any());
    }
    @Test
    void testUserDeletedSuccessfully() {
        long userId = 1L;
        User userToDelete = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        assertDoesNotThrow(() -> userService.deleteUserById(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
    @Test
    void testUpdateUserSuccess() {
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setName("name");
        originalUser.setPassword("pass");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.getReferenceById(1L)).thenReturn(originalUser);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("manel");
        updatedUser.setPassword("123");

        userService.updateUserById(updatedUser, 1L);
        User result = updatedUser;

        verify(userRepository, times(1)).save(originalUser);

        assertEquals(1L, result.getId());
        assertEquals("manel", result.getName());
        assertEquals("123", result.getPassword());
    }




}