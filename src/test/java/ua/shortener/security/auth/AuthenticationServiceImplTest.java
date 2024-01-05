package ua.shortener.security.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void registerOk() {
        //GIVEN
        String email = "valid@gmail.com";
        RegistrationRequest registrationRequest = createRegRequest("Sergiy", email, "Password123");

        //WHEN
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn(new BCryptPasswordEncoder().encode("Password123"));

        //THEN
        JwtRegistrationResponse response = authenticationService.register(registrationRequest);
        String actualRespMessage = response.getErrorMap().get(ResponseRegisterError.OK);
        String expectedRespMessage = ResponseRegisterError.OK.getMessage();
        Assertions.assertEquals(expectedRespMessage, actualRespMessage);

    }

    @Test
    void registerFailedBadPassword(){
        //GIVEN
        String email = "valid@gmail.com";
        RegistrationRequest registrationRequest = createRegRequest("Sergiy", email, "Password");

        //WHEN
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        //THEN
        JwtRegistrationResponse response = authenticationService.register(registrationRequest);
        String expectedRespMessage = response.getErrorMap().get(ResponseRegisterError.BAD_PASSWORD);
        Assertions.assertEquals(expectedRespMessage, ResponseRegisterError.BAD_PASSWORD.getMessage());
    }

    @Test
    void registerFailedBadName(){
        //GIVEN
        String email = "valid@gmail.com";
        RegistrationRequest registrationRequest = createRegRequest("Se", email, "Password");

        //WHEN
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        //THEN
        JwtRegistrationResponse response = authenticationService.register(registrationRequest);
        String expectedRespMessage = response.getErrorMap().get(ResponseRegisterError.BAD_NAME);
        Assertions.assertEquals(expectedRespMessage, ResponseRegisterError.BAD_NAME.getMessage());
    }

    @Test
    void registerFailedExistingEmail(){
        //GIVEN
        String email = "valid@gmail.com";
        RegistrationRequest registrationRequest = createRegRequest("Sergiy", email, "Password123");


        //WHEN
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(new User()));

        //THEN
        JwtRegistrationResponse response = authenticationService.register(registrationRequest);
        String expectedRespMessage = response.getErrorMap().get(ResponseRegisterError.EXISTING_EMAIL);
        Assertions.assertEquals(expectedRespMessage, ResponseRegisterError.EXISTING_EMAIL.getMessage());
    }

    @Test
    void registerFailedBadEmail(){
        //GIVEN
        String email = "invalidgmail.com";
        RegistrationRequest registrationRequest = createRegRequest("Sergiy", email, "Password");

        //WHEN
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        //THEN
        JwtRegistrationResponse response = authenticationService.register(registrationRequest);
        String expectedRespMessage = response.getErrorMap().get(ResponseRegisterError.BAD_EMAIL);
        Assertions.assertEquals(expectedRespMessage, ResponseRegisterError.BAD_EMAIL.getMessage());
    }

    @Test
    void loginOk() {
        //GIVEN
        LoginRequest request = new LoginRequest();
        String password = "Password123";
        String email = "validemail@gmail.com";
        request.setPassword(password);
        request.setEmail(email);
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                email,
                password,
                Stream.of(Role.USER)
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .toList());

        //WHEN
//        Mockito.when(passwordEncoder.encode(password)).thenReturn(new BCryptPasswordEncoder().encode(password));
        Mockito.when(userService.loadUserByUsername(email)).thenReturn(user);
        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())))
                        .thenReturn(new AbstractAuthenticationToken(user.getAuthorities()) {
                            @Override
                            public Object getCredentials() {
                                return user.getAuthorities();
                            }

                            @Override
                            public Object getPrincipal() {
                                return user.getUsername();
                            }
                        });
        Mockito.when(jwtService.generateToken(user)).thenReturn("");

        //THEN
        JwtLoginResponse login = authenticationService.login(request);
        String actualOkMessage = login.getErrors().get(ResponseLoginError.OK);
        String actualJwt = login.getErrors().get(ResponseLoginError.JWT);
        String expectedOkMessage = ResponseLoginError.OK.getMessage();
        String expectedJwt = ResponseLoginError.JWT.getMessage();
        Assertions.assertEquals(expectedOkMessage, actualOkMessage);
        Assertions.assertEquals(expectedJwt, actualJwt);

    }

    @Test
    void loginFailed() {
        //GIVEN
        LoginRequest request = new LoginRequest();
        String password = "Password123";
        String email = "validemail@gmail.com";
        request.setPassword(password);
        request.setEmail(email);

        //WHEN
        Mockito.when(authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())))
                .thenThrow(new RuntimeException());

        //THEN
        JwtLoginResponse login = authenticationService.login(request);
        String actualBadMessage = login.getErrors().get(ResponseLoginError.BAD_PASSWORD_OR_LOGIN);
        String expectedBadMessage = ResponseLoginError.BAD_PASSWORD_OR_LOGIN.getMessage();
        Assertions.assertEquals(expectedBadMessage, actualBadMessage);


    }

    private RegistrationRequest createRegRequest(String name, String email, String password){
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setName(name);
        registrationRequest.setEmail(email);
        registrationRequest.setPassword(password);
        return  registrationRequest;
    }
}