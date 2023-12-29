package ua.shortener.security.auth;


import jakarta.servlet.http.HttpServletResponse;

import ua.shortener.security.auth.dto.login.JwtLoginResponse;
import ua.shortener.security.auth.dto.registration.JwtRegistrationResponse;
import ua.shortener.security.auth.dto.login.LoginRequest;
import ua.shortener.security.auth.dto.registration.RegistrationRequest;

public interface AuthenticationService {
    JwtRegistrationResponse register(RegistrationRequest request);

    JwtLoginResponse login(LoginRequest request, HttpServletResponse servletRequest);
}
