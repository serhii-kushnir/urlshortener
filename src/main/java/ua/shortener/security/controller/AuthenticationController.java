package ua.shortener.security.controller;


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
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<JwtRegistrationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtLoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse servletRequest) {
        return ResponseEntity.ok(authenticationService.login(request, servletRequest));
                //ResponseEntity.ok(authenticationService.login(request, servletRequest));
    }
}
