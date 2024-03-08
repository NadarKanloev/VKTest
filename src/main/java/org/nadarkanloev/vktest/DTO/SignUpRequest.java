package org.nadarkanloev.vktest.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос на регистрацию.

 * Этот класс используется для передачи данных о имени пользователя, адресе электронной почты и пароле при регистрации.
 */
@Data
@Schema(description = "Запрос на регистрацию")
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

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
     * Адрес электронной почты.
     *
     * @example donat290817@gmail.com
     * @min 5
     * @max 255
     * @NotBlank
     * @Email
     */
    @Schema(description = "Адрес электронной почты", example = "donat290817@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Поле адреса электронной почты не должно быть пустым")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    /**
     * Пароль.
     *
     * @example jfsvdckzxl
     * @max 255
     */
    @Schema(description = "Пароль", example = "jfsvdckzxl")
    @Size(max = 255, message = "Длина пароль не должна превышать 255 символов")
    private String password;
}
