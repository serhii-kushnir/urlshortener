package ua.shortener.security.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import ua.shortener.security.auth.dto.login.JwtLoginResponse;
import ua.shortener.security.auth.dto.login.LoginRequest;
import ua.shortener.security.auth.dto.login.ResponseLoginError;
import ua.shortener.security.auth.dto.registration.JwtRegistrationResponse;
import ua.shortener.security.auth.dto.registration.RegistrationRequest;
import ua.shortener.security.auth.dto.registration.ResponseRegisterError;

import ua.shortener.security.jwt.JwtService;

import ua.shortener.user.Role;
import ua.shortener.user.User;

import ua.shortener.user.service.UserRepository;
import ua.shortener.user.service.UserService;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtRegistrationResponse register(final RegistrationRequest request) {
        return getRegisterResponse(request);
    }

    @Override
    public JwtLoginResponse login(final LoginRequest request) {
        return getLoginResponse(request);
    }

    private boolean isPasswordValid(final String password) {
        return (password.length() > 7)
                && (!password.replaceAll("\\d", "").equals(password))
                &&(!password.toLowerCase().equals(password))
                &&(!password.toUpperCase().equals(password));
    }

    private JwtLoginResponse getLoginResponse(final LoginRequest request) {
        JwtLoginResponse response = JwtLoginResponse.builder().errors(new HashMap<>()).build();
        Optional<User> user = userRepository.findUserByEmail(request.getEmail());

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            System.out.println("authenticate.getPrincipal() = " + authenticate.getPrincipal());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErrors().put(ResponseLoginError.BAD_PASSWORD_OR_LOGIN, ResponseLoginError.BAD_PASSWORD_OR_LOGIN.getMessage());
            return response;
        }

        String jwt = jwtService.generateToken(userService.loadUserByUsername(request.getEmail()));
        response.setStatus(HttpStatus.OK.value());
        response.getErrors().put(ResponseLoginError.OK, ResponseLoginError.OK.getMessage());
        response.getErrors().put(ResponseLoginError.JWT, jwt);
        return response;
    }

    private JwtRegistrationResponse getRegisterResponse(final RegistrationRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();

        Optional<User> user = userRepository.findUserByEmail(email);
        JwtRegistrationResponse response = JwtRegistrationResponse.builder().errorMap(new HashMap<>()).build();

        if (user.isEmpty() && isPasswordValid(password) && isEmailValid(email) && isNameValid(name)) {
            response.setStatus(HttpStatus.OK.value());
            response.getErrorMap().put(ResponseRegisterError.OK, ResponseRegisterError.OK.getMessage());
            createUserFromRequest(request);
            return response;
        }

        if (!isPasswordValid(password)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErrorMap().put(ResponseRegisterError.BAD_PASSWORD, ResponseRegisterError.BAD_PASSWORD.getMessage());
        }

        if (!isNameValid(name)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErrorMap().put(ResponseRegisterError.BAD_NAME, ResponseRegisterError.BAD_NAME.getMessage());
        }

        if (!isEmailValid(email)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErrorMap().put(ResponseRegisterError.BAD_EMAIL, ResponseRegisterError.BAD_EMAIL.getMessage());
        }

        if (user.isPresent()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErrorMap().put(ResponseRegisterError.EXISTING_EMAIL, ResponseRegisterError.EXISTING_EMAIL.getMessage());
        }

        return response;
    }

    public void createUserFromRequest(final RegistrationRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);
    }

    private boolean isNameValid(final String name) {
        return !name.isBlank()
                && name.length() > 2
                && !name.replaceAll("[A-Za-zа-яА-я]", "").equals(name);
    }

    private static boolean isEmailValid(final String email) {
        String pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(pattern);
    }
}
