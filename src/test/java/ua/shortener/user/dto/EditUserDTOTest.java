package ua.shortener.user.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditUserDTOTest {

    @Test
    void getName() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setName("EditUserDTO");
        assertEquals("EditUserDTO", editUserDTO.getName());
    }

    @Test
    void getPassword() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setPassword("EditUserDTO");
        assertEquals("EditUserDTO", editUserDTO.getPassword());
    }

    @Test
    void getEmail() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setEmail("EditUserDTO");
        assertEquals("EditUserDTO", editUserDTO.getEmail());
    }

    @Test
    void setName() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setName("EditUserDTO");
        assertEquals("EditUserDTO", editUserDTO.getName());
    }

    @Test
    void setPassword() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setPassword("EditUserDTO");
        assertEquals("EditUserDTO", editUserDTO.getPassword());
    }

    @Test
    void setEmail() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setEmail("EditUserDTO");
        assertEquals("EditUserDTO", editUserDTO.getEmail());
    }
}