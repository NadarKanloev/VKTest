package org.nadarkanloev.vktest.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сущность для хранения аудита в Cassandra.
 */

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Schema(description = "Класс, представляющий аудит.")
public class Audition {

    /**
     * Идентификатор аудита.
     */
    @Schema(description = "Идентификатор аудита.", example = "1")
    @PrimaryKey
    private Long UUID;

    /**
     * Раздел - posts, users или albums
     */
    @Schema(description = "Раздел - posts, users или albums", example = "posts")
    private String section;

    /**
     * Временная метка аудита.
     */
    @Schema(description = "Временная метка аудита.")
    private LocalDateTime timeStamp;

    /**
     * Идентификатор пользователя.
     */
    @Schema(description = "Идентификатор пользователя.", example = "123")
    private String userId;

    /**
     * Роль пользователя.
     */
    @Schema(description = "Роль пользователя.", example = "admin")
    private String userRole;

    /**
     * HTTP метод запроса.
     */
    @Schema(description = "HTTP метод запроса.", example = "GET")
    private String httpMethod;

    /**
     * Параметры запроса.
     */
    @Schema(description = "Параметры запроса.")
    private Map<String, String> requestParams;

    /**
     * Статус ответа.
     */
    @Schema(description = "Статус ответа.", example = "200")
    private String responseStatus;

    /**
     * Ответ от сервера.
     */
    @Schema(description = "Ответ от сервера.")
    private String serverResponse;

    /**
     * Логи ошибок.
     */
    @Schema(description = "Логи ошибок.")
    private String errorLogs;

    /**
     * Информация о кэшировании.
     */
    @Schema(description = "Информация о кэшировании.")
    private String cachingInfo;
}
