package org.nadarkanloev.vktest.Controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nadarkanloev.vktest.DTO.JwtAuthenticationResponse;
import org.nadarkanloev.vktest.DTO.SignInRequest;
import org.nadarkanloev.vktest.DTO.SignUpRequest;
import org.nadarkanloev.vktest.Service.Auth.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для аутентификации пользователей.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {

    /**
     * Сервис аутентификации.
     */
    private final AuthenticationService authenticationService;

    /**
     * Регистрация нового пользователя.
     *
     * @param request Запрос на регистрацию.
     * @return Ответ с JWT токеном.
     */
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        JwtAuthenticationResponse response = authenticationService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Авторизация пользователя.
     *
     * @param request Запрос на авторизацию.
     * @return Ответ с JWT токеном.
     */
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request) {
        JwtAuthenticationResponse response = authenticationService.signIn(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
