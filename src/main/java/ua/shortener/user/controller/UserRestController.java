package ua.shortener.user.controller;

import lombok.RequiredArgsConstructor;

import ua.shortener.link.Link;
import ua.shortener.user.User;
import ua.shortener.user.dto.EditUserDTO;
import ua.shortener.user.dto.UserEditAdminDTO;
import ua.shortener.user.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/links")
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

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
