package org.nadarkanloev.vktest.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответ с токеном доступа.

 * Этот класс используется для передачи токена доступа клиенту после успешной аутентификации.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с токеном доступа")
public class JwtAuthenticationResponse {

    /**
     * Токен доступа.
     *
     * Токен используется для аутентификации пользователя в системе.
     *
     * @example eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...
     */
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;
}
