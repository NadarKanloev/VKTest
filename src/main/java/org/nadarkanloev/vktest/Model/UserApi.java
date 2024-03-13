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
@Schema(description = "Пользователи API")
public class UserApi implements Serializable {
    private static final long serialVersionUID = 8462715334851590908L;

    @Schema(description = "Идентификатор пользователя.", example = "123")
    private int id;

    @Schema(description = "Имя пользователя.", example = "John Doe")
    private String name;

    @Schema(description = "Имя пользователя для входа.", example = "johndoe")
    private String username;

    @Schema(description = "Электронная почта пользователя.", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Адрес пользователя.")
    private Address address;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class Address implements Serializable {
        @Schema(description = "Улица.", example = "123 Main St")
        private String street;

        @Schema(description = "Номер квартиры.", example = "Apt 101")
        private String suite;

        @Schema(description = "Город.", example = "New York")
        private String city;

        @Schema(description = "Почтовый индекс.", example = "10001")
        private String zipcode;

        @Schema(description = "Географические координаты.")
        private Geo geo;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @ToString
        @EqualsAndHashCode
        public static class Geo implements Serializable {
            @Schema(description = "Широта.", example = "40.7128")
            private String lat;

            @Schema(description = "Долгота.", example = "-74.0060")
            private String lng;
        }
    }
}
