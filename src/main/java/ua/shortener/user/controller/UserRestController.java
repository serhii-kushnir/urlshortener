package ua.shortener.user.controller;

import ua.shortener.link.Link;
import ua.shortener.user.User;
import ua.shortener.user.dto.EditUserDTO;
import ua.shortener.user.dto.UserEditAdminDTO;
import ua.shortener.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}/links")
    public ResponseEntity<List<Link>> getLinksByUserId(@PathVariable Long id) {
        List<Link> links = userService.getLinksByUserId(id);
        return ResponseEntity.ok(links);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody EditUserDTO editUserDTO) {
        return userService.getUserById(id)
                .map(existingUser -> ResponseEntity.ok(userService.editUser(existingUser, editUserDTO)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/update/{id}/admin")
    public ResponseEntity<User> editUserAdmin(@PathVariable long id, @RequestBody UserEditAdminDTO userEditAdminDTO) {
        return userService.getUserById(id)
                .map(existingUser -> ResponseEntity.ok(userService.editUserAdmin(existingUser, userEditAdminDTO)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
