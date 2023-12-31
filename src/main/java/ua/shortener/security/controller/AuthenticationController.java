package ua.shortener.security.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.shortener.security.auth.AuthenticationService;
import ua.shortener.security.auth.dto.login.JwtLoginResponse;
import ua.shortener.security.auth.dto.registration.JwtRegistrationResponse;
import ua.shortener.security.auth.dto.login.LoginRequest;
import ua.shortener.security.auth.dto.registration.RegistrationRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для аутентифікації")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    @Operation(summary = "Реєстрація нового користувача")
    public ResponseEntity<JwtRegistrationResponse> register(@RequestBody @Parameter(description = "Дані для реєстрації")
                                                                RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Вхід в систему")
    public ResponseEntity<JwtLoginResponse> login(@RequestBody @Parameter(description = "Дані для входу")
                                                      LoginRequest request, HttpServletResponse servletRequest) {
        return ResponseEntity.ok(authenticationService.login(request, servletRequest));
                //ResponseEntity.ok(authenticationService.login(request, servletRequest));
    }
}
