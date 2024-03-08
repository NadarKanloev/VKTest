package org.nadarkanloev.vktest.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос на вход в систему.

 * Этот класс используется для передачи данных о имени пользователя и пароле при входе в систему.
 */
@Data
@Schema
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    /**
     * Имя пользователя.
     *
     * @example Nadar
     * @min 5
     * @max 50
     * @NotBlank
     */
    @Schema(description = "Имя пользователя", example = "Nadar")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String username;

    /**
     * Пароль.
     *
     * @example jdfvmdfmkv
     * @min 8
     * @max 255
     * @NotBlank
     */
    @Schema(description = "Пароль", example = "jdfvmdfmkv")
    @Size(min = 8, max = 255, message = "Пароль должен быть от 5 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
