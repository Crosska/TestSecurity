package com.crosska.security.TestSecurity.config;

import com.crosska.security.TestSecurity.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    // Определение SecurityFilterChain для настройки доступа
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Отключаем CSRF, если это API (по умолчанию для REST API)
                .authorizeRequests().requestMatchers("/public/**").permitAll()  // Разрешить доступ ко всем /public эндпоинтам без аутентификации
                // Только для пользователей с ролью ROLE_USER\
                .requestMatchers("/user/**").hasRole("USER")
                // Только для пользователей с ролью ROLE_ADMIN
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Все остальные эндпоинты требуют аутентификации
                .anyRequest().authenticated()
                .and()
                .httpBasic(); // Использование базовой аутентификации (username:password)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
