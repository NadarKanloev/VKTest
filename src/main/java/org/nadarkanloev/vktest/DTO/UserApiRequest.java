package org.nadarkanloev.vktest.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Модель запроса для создания пользователя")
@AllArgsConstructor
@NoArgsConstructor
public class UserApiRequest {
    private String name;
    private String username;
    private String email;
}
