package ua.shortener.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import ua.shortener.link.Link;
import ua.shortener.user.User;

import ua.shortener.user.dto.EditUserDTO;
import ua.shortener.user.dto.UserDTO;
import ua.shortener.user.dto.UserEditAdminDTO;
import ua.shortener.user.dto.UserWithLinkDTO;
import ua.shortener.user.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    public ResponseEntity<User> getUserById(final @PathVariable
                                                @Parameter(description = "ID користувача") long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/links")

    @Operation(summary = "Отримати посилання користувача за ID")
    public ResponseEntity<List<Link>> getLinksByUserId(final @PathVariable
                                                           @Parameter(description = "ID користувача") Long id) {
        List<Link> links = userService.getLinksByUserId(id);
        return ResponseEntity.ok(links);
    }

    @PostMapping("/update/{id}")

    @Operation(summary = "Оновити користувача")
    public ResponseEntity<User> updateUser(final @PathVariable @Parameter(description = "ID користувача") long id,
                                           final @RequestBody @Parameter(description = "Дані для оновлення користувача")
                                           EditUserDTO editUserDTO) {
        return userService.getUserById(id)
                .map(existingUser -> ResponseEntity.ok(userService.editUser(existingUser, editUserDTO)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/update/{id}/admin")
    @Operation(summary = "Редагувати користувача як адміністратор")
    public ResponseEntity<User> editUserAdmin(final @PathVariable @Parameter(description = "ID користувача") long id,
                                              final @RequestBody
                                              @Parameter(description = "Дані для редагування користувача адміністратором")
                                              UserEditAdminDTO userEditAdminDTO) {
        return userService.getUserById(id)
                .map(existingUser -> ResponseEntity.ok(userService.editUserAdmin(existingUser, userEditAdminDTO)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/delete/{id}")

    @Operation(summary = "Видалити користувача")
    public ResponseEntity<Object> deleteUser(final @PathVariable @Parameter(description = "ID користувача") long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Отримати деталі користувача")
    public ResponseEntity<UserWithLinkDTO> getUserDetails(final @PathVariable
                                                              @Parameter(description = "ID користувача") long id) {
        UserWithLinkDTO userWithLinkDTO = userService.getUserDetailsById(id);
        return ResponseEntity.ok(userWithLinkDTO);
    }

    @GetMapping("/{id}/user_info")
    @Operation(summary = "Отримати інформацію про користувача")
    public ResponseEntity<UserDTO> getUserInfo(final @PathVariable
                                                   @Parameter(description = "ID користувача") long id) {
        UserDTO userDTO = userService.getUserInfoById(id);
        return ResponseEntity.ok(userDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @Operation(summary = "Обробник винятків для EntityNotFoundException", description =
            "Повертає повідомлення про помилку та статус 404, коли виникає EntityNotFoundException")
    public ResponseEntity<String> handleEntityNotFoundException(final EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
