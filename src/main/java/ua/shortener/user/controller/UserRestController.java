package ua.shortener.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "API для користувачів")
public class UserRestController {

    private final UserService userService;

    @GetMapping("/list")
    @Operation(summary = "Отримати всіх користувачів")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Отримати користувача за ID")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/links")
    @Operation(summary = "Отримати посилання користувача за ID")
    public ResponseEntity<List<Link>> getLinksByUserId(@PathVariable Long id) {
        List<Link> links = userService.getLinksByUserId(id);
        return ResponseEntity.ok(links);
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "Оновити користувача")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody EditUserDTO editUserDTO) {
        return userService.getUserById(id)
                .map(existingUser -> ResponseEntity.ok(userService.editUser(existingUser, editUserDTO)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/update/{id}/admin")
    @Operation(summary = "Редагувати користувача як адміністратор")
    public ResponseEntity<User> editUserAdmin(@PathVariable long id, @RequestBody UserEditAdminDTO userEditAdminDTO) {
        return userService.getUserById(id)
                .map(existingUser -> ResponseEntity.ok(userService.editUserAdmin(existingUser, userEditAdminDTO)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "Видалити користувача")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
