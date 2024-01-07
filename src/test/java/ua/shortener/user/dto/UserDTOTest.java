package ua.shortener.user.dto;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void getId() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        assertEquals(1L, userDTO.getId());
    }

    @Test
    void getName() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("UserDTO");
        assertEquals("UserDTO", userDTO.getName());
    }

    @Test
    void getEmail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("UserDTO@gmail.com");
        assertEquals("UserDTO@gmail.com", userDTO.getEmail());
    }

    @Test
    void getCreatedAt() {
        UserDTO userDTO = new UserDTO();
        LocalDateTime testDateTime = LocalDateTime.of(2024, 1, 9, 12, 0, 0);
        userDTO.setCreatedAt(testDateTime);
        assertEquals(testDateTime, userDTO.getCreatedAt());
    }

    @Test
    void setId() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        assertEquals(1L, userDTO.getId());
    }

    @Test
    void setName() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("UserDTO");
        assertEquals("UserDTO", userDTO.getName());
    }

    @Test
    void setEmail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("UserDTO@gmail.com");
        assertEquals("UserDTO@gmail.com", userDTO.getEmail());
    }

    @Test
    void setCreatedAt() {
        UserDTO userDTO = new UserDTO();
        LocalDateTime testDateTime = LocalDateTime.of(2024, 1, 9, 12, 0, 0);
        userDTO.setCreatedAt(testDateTime);
        assertEquals(testDateTime, userDTO.getCreatedAt());
    }
}