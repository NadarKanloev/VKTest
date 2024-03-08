package org.nadarkanloev.vktest.Model;

import jakarta.persistence.*;
import lombok.*;
import org.nadarkanloev.vktest.Enum.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Представляет сущность пользователя.
 * Реализует интерфейс UserDetails для Spring Security.
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    /**
     * Имя пользователя.
     * Уникальное значение.
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * Пароль пользователя.
     * Не может быть пустым.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Email-адрес пользователя.
     * Уникальное значение.
     * Не может быть пустым.
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Роль пользователя.
     * Хранится как строковое представление enum {@link Role}.
     * Не может быть пустым.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Возвращает разрешения, предоставленные пользователю.
     *
     * @return Коллекция объектов {@link GrantedAuthority}, представляющих роли пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Указывает, истек ли срок действия учетной записи пользователя.
     *
     * @return true, если срок действия учетной записи пользователя не истек, false - в противном случае
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Указывает, заблокирована ли учетная запись пользователя.
     *
     * @return true, если учетная запись пользователя не заблокирована, false - в противном случае
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Указывает, истек ли срок действия учетных данных пользователя.
     *
     * @return true, если срок действия учетных данных пользователя не истек, false - в противном случае
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Указывает, активирована ли учетная запись пользователя.
     *
     * @return true, если учетная запись пользователя активирована, false - в противном случае
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
