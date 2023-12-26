package ua.shortener.security.auth;


import jakarta.servlet.http.HttpServletResponse;
import ua.shortener.security.auth.dto.JwtAuthenticationResponse;
import ua.shortener.security.auth.dto.SignInRequest;
import ua.shortener.security.auth.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request, HttpServletResponse servletRequest);
}
