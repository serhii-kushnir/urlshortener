package ua.shortener.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ua.shortener.security.jwt.JwtAuthenticationFilter;

import ua.shortener.security.jwt.JwtCookieAuthenticationFilter;
import ua.shortener.user.service.UserService;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/swagger/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swaggerresources/**",
                                "/webjars/**").permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/**"))
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/sh/**"))
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/shortify/register"))
                        .permitAll()
                        .requestMatchers("/shortify/home_user/example","/shortify/about","/shortify/thanks_to")
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/static/**"))
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/static/images/**"))
                        .permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/styles/**"))
                        .permitAll()
                        .anyRequest().authenticated()).formLogin(l -> l
                                                                        .loginPage("/shortify/login")
                                                                        .permitAll()
//                                                                        .usernameParameter("email")
                                                                        .defaultSuccessUrl("/shortify/home_user", true)
//                                                                        .loginProcessingUrl("/shortify/login")
                                                                        .usernameParameter("email")
                                                                        .passwordParameter("password"))
                .logout(logout -> logout
                        .deleteCookies("jwtToken")
                        .logoutUrl("/logout"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
