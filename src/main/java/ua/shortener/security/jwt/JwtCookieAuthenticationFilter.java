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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.shortener.user.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {
    private final JwtServiceImpl jwtService;
    private final UserService userService;
    @Lazy
    @Autowired
    private  AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("In jwt cookie auth filter");
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<String> paramKeys = parameterMap.keySet();
        for (String paramKey : paramKeys) {
            System.out.println(paramKey + ":::" + Arrays.toString(parameterMap.get(paramKey)));
        }
        String username = request.getParameter("email");
        String password = request.getParameter("password");
        String jwtFromCookie;

        if ((username != null && password != null) && !parameterMap.containsKey("name")){
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password));
                UserDetails userDetails = userService.loadUserByUsername(username);
                String token = jwtService.generateToken(userDetails);
                Cookie cookie1 = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwtToken")).findFirst().orElse(null);
                System.out.println("generated cookie = " + token);
                Cookie jwtCookie = new Cookie("jwtToken", token);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(3600);
                response.addCookie(jwtCookie);
                filterChain.doFilter(request, response);
                return;

            }catch (Exception e){
                filterChain.doFilter(request, response);
                return;
            }
        }


        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            Optional<Cookie> jwtTokenCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("jwtToken"))
                    .findFirst();

            if (jwtTokenCookie.isPresent()) {
                log.info("JWT TOKEN IS PRESENT");
                jwtFromCookie = jwtTokenCookie.get().getValue();
                log.info("TOKEN FROM COOKIE" + jwtTokenCookie.get().getValue());
                String extractUsername;
                try {
                    extractUsername = jwtService.extractUserName(jwtFromCookie);
                }catch (ExpiredJwtException e){
                    log.info("JWT TOKEN IS DEPRECATED");
                    filterChain.doFilter(request, response);
                    return;
                }

                UserDetails userByEmail = userService.loadUserByUsername(extractUsername);
                if (jwtService.isTokenValid(jwtFromCookie, userByEmail)) {
                    log.info("TOKEN IS VALID USER WILL BE ADDED TO CONTEXT");
                    String email = jwtService.extractUserName(jwtFromCookie);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email, null, userByEmail.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }

            }
        }
        filterChain.doFilter(request, response);
    }

}
