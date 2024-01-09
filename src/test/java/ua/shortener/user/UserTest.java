package ua.shortener.user;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ua.shortener.link.Link;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserTest {

    @Test
    void onCreate() {
        User user = new User();
        user.onCreate();
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(1L);
        user.setName("JohnDoe");
        user.setPassword("SecurePass123");
        user.setEmail("john.doe@example.com");
        user.setRole(Role.USER);
        user.setEnabled(true);

        String expectedToString = "User{" +
                "id=1, " +
                "name='JohnDoe', " +
                "password='SecurePass123', " +
                "email='john.doe@example.com', " +
                "role=USER, " +
                "enabled=true, " +
                "createdAt=" + user.getCreatedAt() +
                '}';

        assertEquals(expectedToString, user.toString());
    }

    @Test
    void getId() {
        User user = new User();
        user.setPassword("password123");
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void getName() {
        User user = new User();
        user.setName("JohnDoe");
        assertEquals("JohnDoe", user.getName());
    }

    @Test
    void getPassword() {
        User user = new User();
        user.setPassword("SecurePass123");
        assertEquals("SecurePass123", user.getPassword());
    }

    @Test
    void getEmail() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void getRole() {
        User user = new User();
        user.setRole(Role.USER);
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void isEnabled() {
        User user = new User();
        user.setEnabled(true);
        assertTrue(user.isEnabled());
    }

    @Test
    void getCreatedAt() {
        User user = new User();
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    void getLinks() {
        User user = new User();
        List<Link> links = new ArrayList<>();
        user.setLinks(links);
        assertEquals(links, user.getLinks());
    }

    @Test
    void setId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void setName() {
        User user = new User();
        user.setName("JohnDoe");
        assertEquals("JohnDoe", user.getName());
    }

    @Test
    void setPassword() {
        User user = new User();
        user.setPassword("SecurePass123");
        assertEquals("SecurePass123", user.getPassword());
    }

    @Test
    void setEmail() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void setRole() {
        User user = new User();
        user.setRole(Role.USER);
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void setEnabled() {
        User user = new User();
        user.setEnabled(true);
        assertTrue(user.isEnabled());
    }

    @Test
    void setCreatedAt() {
        User user = new User();
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    void setLinks() {
        User user = new User();
        List<Link> links = new ArrayList<>();
        user.setLinks(links);
        assertEquals(links, user.getLinks());
    }

    @Test
    void testEquals() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1, user2);
    }
}
