package org.nadarkanloev.vktest.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

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
    private int userId;
    private int id;
    private String title;
}
