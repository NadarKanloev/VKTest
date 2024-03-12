package org.nadarkanloev.vktest.Model;

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
public class Audition {

    /**
     * Идентификатор аудита.
     */
    @PrimaryKey
    private Long UUID;

    /**
     * Раздел - posts, users или albums
     */
    private String section;

    /**
     * Временная метка аудита.
     */
    private LocalDateTime timeStamp;

    /**
     * Идентификатор пользователя.
     */
    private String userId;

    /**
     * Роль пользователя.
     */
    private String userRole;

    /**
     * HTTP метод запроса.
     */
    private String httpMethod;

    /**
     * Параметры запроса.
     */
    private Map<String, String> requestParams;

    /**
     * Статус ответа.
     */
    private String responseStatus;

    /**
     * Ответ от сервера.
     */
    private String serverResponse;

    /**
     * Логи ошибок.
     */
    private String errorLogs;

    /**
     * Информация о кэшировании.
     */
    private String cachingInfo;
}
