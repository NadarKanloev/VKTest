package org.nadarkanloev.vktest.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * Класс, представляющий собой модель данных альбома.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Schema(name = "Модель данных альбома")
public class Album implements Serializable {
    private static final long serialVersionUID = 8462715334851597808L;

    /**
     * Идентификатор пользователя, которому принадлежит альбом.
     */
    @Schema(description = "Идентификатор пользователя, которому принадлежит альбом.", example = "1")
    private int userId;

    /**
     * Идентификатор альбома.
     */
    @Schema(description = "Идентификатор альбома.", example = "1")
    private int id;

    /**
     * Название альбома.
     */
    @Schema(description = "Название альбома.", example = "My Vacation")
    private String title;
}
