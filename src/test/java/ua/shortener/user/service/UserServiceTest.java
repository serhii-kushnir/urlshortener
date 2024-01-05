package ua.shortener.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shortener.user.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private  UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername() {

    }

    @Test
    void findUserByEmail() {
        //GIVEN
        String email = "myEmail";
        User user = new User();
        user.setEmail(email);

        //WHEN
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        //THEN
        String actualEmail = userService.findUserByEmail(email).get().getEmail();
        String expectedEmail = "myEmail";
        Assertions.assertEquals(expectedEmail, actualEmail);


    }

    @Test
    void getLinksByUserId() {

    }

    @Test
    void getAllUser() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void editUser() {
    }

    @Test
    void editUserAdmin() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUserDetailsById() {
    }

    @Test
    void getUserInfoById() {
    }
}