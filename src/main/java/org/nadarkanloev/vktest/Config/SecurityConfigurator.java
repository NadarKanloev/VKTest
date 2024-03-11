package org.nadarkanloev.vktest.Config;

import lombok.RequiredArgsConstructor;
import org.nadarkanloev.vktest.FIlter.JwtAuthenticationFilter;
import org.nadarkanloev.vktest.Service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Конфигуратор безопасности приложения.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfigurator {

    /**
     * Фильтр аутентификации JWT.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Сервис пользователей.
     */
    private final UserService userService;

    /**
     * Настройка цепочки фильтров безопасности.
     *
     * @param http Объект конфигурации безопасности HTTP.
     * @return Цепочка фильтров безопасности.
     * @throws Exception Если возникли проблемы с настройкой фильтров безопасности HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> { // Отключение конфигурации CORS
                    var corsConfigurator = new CorsConfiguration();
                    corsConfigurator.setAllowedOriginPatterns(List.of("*"));
                    corsConfigurator.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT"));
                    corsConfigurator.setAllowedHeaders(List.of("*"));
                    corsConfigurator.setAllowCredentials(true);
                    return corsConfigurator;
                }))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/sign-in/").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/*", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/endpoint", "/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Шифратор паролей.
     *
     * @return Шифратор паролей BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Провайдер аутентификации.
     *
     * @return Провайдер аутентификации.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Менеджер аутентификации.
     *
     * @param config Конфигурация аутентификации.
     * @return Менеджер аутентификации.
     * @throws Exception Если возникли проблемы с конфигурацией аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
