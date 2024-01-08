package ua.shortener.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import ua.shortener.user.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtServiceImpl jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    protected void doFilterInternal(final @NonNull HttpServletRequest request,
                                    final @NonNull HttpServletResponse response,
                                    final @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("In doFilterInternal");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String jwtFromCookie;
        final String userEmail;

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String s : parameterMap.keySet()) {
            System.out.println(s + ": "+ Arrays.toString(parameterMap.get(s)));
        }

        String email = request.getParameter("username");
        String password = request.getParameter("password");
       System.out.println("EMAIL = " + email);
        System.out.println("PASSWORD = " + password);
        if (email != null && password != null) {
            log.info("HAVE USERNAME AND PASSWORD PARAMETERS");
            UserDetails userByEmail = userService.loadUserByUsername(email);
            if (passwordEncoder.matches(password, userByEmail.getPassword())) {
                log.info("PASSWORDS MATCHES ADDING COOKIE");
                String token = jwtService.generateToken(userByEmail);
                Cookie cookie = new Cookie("jwtToken", token);
                cookie.setDomain(null);
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(3600);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            Optional<Cookie> jwtTokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("jwtToken"))
                    .findFirst();
            if(parameterMap.keySet().contains("logout")){
                log.info("DO LOGOUT COOKIE WILL BE CHANGE TO DEPRECATE ONE");
                String oldToken = jwtTokenCookie.get().getValue();
                String invalidToken = jwtService.generateTokenInvalidToken(oldToken);
                Cookie cookie = new Cookie("jwtToken", invalidToken);
                response.addCookie(cookie);
            }

            if (jwtTokenCookie.isPresent()) {
                log.info("JWT TOKEN IS PRESENT");
                System.out.println(parameterMap);
                jwtFromCookie = jwtTokenCookie.get().getValue();
                log.info("TOKEN FROM COOKIE" + jwtTokenCookie.get().getValue());
                String username;
                try {
                    username = jwtService.extractUserName(jwtFromCookie);
                }catch (ExpiredJwtException e){
                    log.info("JWT TOKEN IS DEPRECATED");
                    filterChain.doFilter(request, response);
                    return;
                }

                UserDetails userByEmail = userService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwtFromCookie, userByEmail)) {
                    log.info("TOKEN IS VALID USER WILL BE ADDED TO CONTEXT");
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email, null, userByEmail.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }

                filterChain.doFilter(request, response);
                return;
            }
        }


        if ((authHeader == null || !authHeader.startsWith("Bearer "))) {
            log.info("authHeader == null || !authHeader.startsWith( \"Bearer \")");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserName(jwt);
        if ((userEmail != null
                && SecurityContextHolder.getContext().getAuthentication() == null)) {

            log.info("userEmail != null\n" +
                    "                && SecurityContextHolder.getContext().getAuthentication() == null");

            UserDetails userDetails = userService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("jwtService.isTokenValid(jwt, userDetails)");
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
            log.info("all is clear");
            filterChain.doFilter(request, response);

        }
    }

}
