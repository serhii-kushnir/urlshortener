package ua.shortener.test_controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.shortener.UrlShortenerApplication;

import ua.shortener.test_controller.config.ContainersEnvironment;
import ua.shortener.user.User;
import ua.shortener.user.service.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UrlShortenerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class UserRepositoriTest extends ContainersEnvironment {
    @Autowired
    public UserRepository userRepository;

    @Test
    void WhenGetLinkExpectEmptyList(){
        List<User> list = userRepository.findAll();
        assertEquals(2, list.size());
    }
}