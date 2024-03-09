package org.nadarkanloev.vktest.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Schema(description = "Модель данных для поста")
public class Post {
    @Schema(description = "ID пользователя", example = "1")
    private int userId;

    @Schema(description = "ID поста", example = "1")
    private int id;

    @Schema(description = "Заголовок поста", example = "sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
    private String title;

    @Schema(description = "Текст поста", example = "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto")
    private String body;
}
