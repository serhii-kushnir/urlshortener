package ua.shortener.security.auth;


import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.shortener.security.auth.dto.JwtAuthenticationResponse;
import ua.shortener.security.auth.dto.SignInRequest;
import ua.shortener.security.auth.dto.SignUpRequest;
import ua.shortener.security.jwt.JwtService;

import ua.shortener.user.Role;
import ua.shortener.user.User;
import ua.shortener.user.service.UserRepository;
import ua.shortener.user.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if(userRepository.findUserByEmail(request.getEmail()).isPresent()){
            return JwtAuthenticationResponse.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("User with email " + request.getEmail() + " already exist")
                    .build();
        }

        User user = new User();
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);


        return JwtAuthenticationResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Successfully added")
                .build();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request, HttpServletResponse servletRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }catch (Exception e){
            return JwtAuthenticationResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Incorrect username or password")
                    .build();
        }

        String jwt = jwtService.generateToken(userService.loadUserByUsername(request.getEmail()));
        servletRequest.addHeader("Authorization", "Bearer " + jwt);
        return JwtAuthenticationResponse.builder()
                .status(HttpStatus.OK.value())
                .message(jwt).build();
    }
}
