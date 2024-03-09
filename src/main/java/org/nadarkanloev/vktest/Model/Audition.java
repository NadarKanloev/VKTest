package org.nadarkanloev.vktest.Model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Audition {

    @PrimaryKey
    private Long UUID;

    private LocalDateTime timeStamp;

    private String userId;

    private String userRole;

    private String httpMethod;

    private HashMap<String, String> requestParams;

    private String responseStatus;

    private String serverResponse;

    private String errorLogs;

    private String cachingInfo;
}
