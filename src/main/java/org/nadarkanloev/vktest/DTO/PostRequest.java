package org.nadarkanloev.vktest.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Модель запроса для создания поста")
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @Schema(description = "Заголовок поста", example = "Test Title")
    private String title;

    @Schema(description = "Текст поста", example = "Test Body")
    private String body;

    @Schema(description = "ID пользователя, создающего пост", example = "123")
    private int userId;
}
