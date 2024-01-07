package ua.shortener.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;

import ua.shortener.link.Link;
import ua.shortener.user.Role;
import ua.shortener.user.User;
import ua.shortener.user.dto.EditUserDTO;
import ua.shortener.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String email = "testuser@example.com";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void loadUserByUsername() {
        //GIVEN
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("ExistingUser");
        existingUser.setPassword("password123");
        existingUser.setEmail("existinguser@example.com");
        existingUser.setRole(Role.USER);

        //WHEN
        Mockito.when(userRepository.findUserByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        UserService userService = new UserService(userRepository);

        //THEN
        UserDetails userDetails = userService.loadUserByUsername(existingUser.getEmail());
        assertEquals(existingUser.getEmail(), userDetails.getUsername());
        assertEquals(existingUser.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("USER", userDetails.getAuthorities().iterator().next().getAuthority());
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
        //Given
        Long userId = 1L;
        User User = new User();
        User.setId(1L);
        User.setName("TestUser");
        User.setPassword("TestPassword123");
        User.setEmail("testuser@example.com");
        Link link1 = new Link();
        link1.setShortLink("testlik");
        link1.setUrl("https://example1.com");
        link1.setUser(User);
        List<Link> list2 = new ArrayList<>();
        list2.add(link1);
        User.setLinks(list2);

        //When
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(User));
        //Then
        List<Link> userLinks = userService.getLinksByUserId(1L);
        assertEquals(1, userLinks.size());
    }

    @Test
    void getAllUser() {
        //Given
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User1");
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User2");
        //When
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        //Then
        List<User> allUsers = userService.getAllUser();
        assertEquals(2, allUsers.size());
        assertEquals("User1", allUsers.get(0).getName());
        assertEquals("User2", allUsers.get(1).getName());
    }

    @Test
    void getUserById() {
        //GIVEN
        Long id = 1l;
        User user = new User();
        user.setId(1L);
        user.setName("User1");

        //WHEN
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        //THEN
        String actualUser = userService.getUserById(id).get().getName();
        String expectedUser = "User1";
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void editUser() {
        //GIVEN
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("ExistingUser");

        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setName("EditedUser");

        //WHEN
        Mockito.when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserService userService = new UserService(userRepository);
        // THEN
        User editedUser = userService.editUser(existingUser, editUserDTO);
        assertEquals("EditedUser", editedUser.getName());
    }

    @Test
    void deleteUser() {
        //GIVEN
        Long id = 1l;
        UserService userService = new UserService(userRepository);
        //WHEN
        userService.deleteUser(id);
        //THEN
        verify(userRepository).deleteById(id);
    }
    @Test
    void getUserInfoById(){
        //GIVEN
        Long id = 1l;
        User user = new User();
        user.setId(1L);
        user.setName("User1");
        user.setEmail("User1@gmail");
        LocalDateTime testDateTime = LocalDateTime.of(2024, 1, 9, 12, 0, 0);
        user.setCreatedAt(testDateTime);

        UserService userService = new UserService(userRepository);

        //WHEN
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        UserDTO userDTO = userService.getUserInfoById(id);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getCreatedAt(), userDTO.getCreatedAt());


    }
}