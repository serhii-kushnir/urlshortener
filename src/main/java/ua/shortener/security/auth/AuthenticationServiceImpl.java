package ua.shortener.security.auth;


import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtRegistrationResponse register(RegistrationRequest request) {
        return getRegisterResponse(request);
    }

    @Override
    public JwtLoginResponse login(LoginRequest request, HttpServletResponse httpServletResponse) {
        return getLoginResponse(request, httpServletResponse);
    }

    private boolean isPasswordValid(String password){
        return (password.length() > 7)
                && (!password.replaceAll("\\d", "").equals(password))
                &&(!password.toLowerCase().equals(password))
                &&(!password.toUpperCase().equals(password));
    }

    private JwtLoginResponse getLoginResponse(LoginRequest request, HttpServletResponse httpServletResponse){
        JwtLoginResponse response = JwtLoginResponse.builder().messages(new ArrayList<>()).errors(new ArrayList<>()).build();
        Optional<User> user = userRepository.findUserByEmail(request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch (Exception e){
            response.getErrors().add(ResponseLoginError.BAD_PASSWORD_OR_LOGIN);
            response.getMessages().add(ResponseLoginError.BAD_PASSWORD_OR_LOGIN.getMessage());
            return response;
        }

        String jwt = jwtService.generateToken(userService.loadUserByUsername(request.getEmail()));
        httpServletResponse.addHeader("Authorization", "Bearer " + jwt);
        response.getErrors().add(ResponseLoginError.OK);
        response.getMessages().add(ResponseLoginError.OK.getMessage());
        response.getMessages().add(jwt);
        return response;
    }

    private JwtRegistrationResponse getRegisterResponse(RegistrationRequest request){
        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> user = userRepository.findUserByEmail(email);
        JwtRegistrationResponse response = JwtRegistrationResponse.builder().messages(new ArrayList<>()).errors(new ArrayList<>()).build();

        if (user.isEmpty() && isPasswordValid(password)){
            response.setStatus(HttpStatus.OK.value());
            response.getErrors().add(ResponseRegisterError.OK);
            response.getMessages().add(ResponseRegisterError.OK.getMessage());
            createUserFromRequest(request);
            return response;
        }

        if(!isPasswordValid(password)){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErrors().add(ResponseRegisterError.BAD_PASSWORD);
            response.getMessages().add(ResponseRegisterError.BAD_PASSWORD.getMessage());
        }

        if (user.isPresent()){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getErrors().add(ResponseRegisterError.BAD_EMAIL);
            response.getMessages().add(ResponseRegisterError.BAD_EMAIL.getMessage());
        }
        return response;
    }

    private void createUserFromRequest(RegistrationRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);
    }


        //can use for writing tests
//    public static void main(String[] args) {
//        String validPassword = "qwerTy12";
//        String passwordWithoutUpperCase = "qwerty12";
//        String passwordWithoutLowerCase = "QWERTY12";
//        String passwordOnlyLetters = "QWERTasdW";
//        String passwordWithoutLetters = "123456780";
//        String shortPassword = "qweQ1yu";
//
//        System.out.println("validatePassword(validPassword) = " + validatePassword(validPassword));
//        System.out.println("validatePassword(passwordWithoutUpperCase) = " + validatePassword(passwordWithoutUpperCase));
//        System.out.println("validatePassword(passwordWithoutLowerCase) = " + validatePassword(passwordWithoutLowerCase));
//        System.out.println("validatePassword(passwordOnlyLetters) = " + validatePassword(passwordOnlyLetters));
//        System.out.println("validatePassword(passwordWithoutLetters) = " + validatePassword(passwordWithoutLetters));
//        System.out.println("validatePassword(shortPassword) = " + validatePassword(shortPassword));
//
//    }
}
