package org.nadarkanloev.vktest.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.DTO.UserApiRequest;
import org.nadarkanloev.vktest.Model.UserApi;
import org.nadarkanloev.vktest.Service.UserApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Работа с пользователями API")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@Log4j2
public class UserApiController {

    private final UserApiService userApiService;

    /**
     * Получение всех пользователей.
     */
    @GetMapping
    public ResponseEntity<List<UserApi>> getAllUsers() {
        List<UserApi> users = userApiService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Получение пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserApi> getUserById(@PathVariable int id) {
        UserApi userApi = userApiService.getUserById(id);
        if (userApi == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userApi, HttpStatus.OK);
    }

    /**
     * Создание нового пользователя.
     *
     * @param userApiRequest Запрос на создание пользователя.
     */
    @PostMapping
    public ResponseEntity<UserApi> createUser(@RequestBody UserApiRequest userApiRequest) {
        UserApi user = UserApi.builder().email(userApiRequest.getEmail())
                .username(userApiRequest.getUsername())
                .name(userApiRequest.getName())
                .build();
        UserApi responseUser = userApiService.createUser(user);
        if (responseUser != null) {
            return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Обновление информации о пользователе.
     *
     * @param id             Идентификатор пользователя.
     * @param userApiRequest Запрос на обновление информации о пользователе.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserApi> putUser(@PathVariable int id, @RequestBody UserApiRequest userApiRequest) {
        UserApi user = UserApi.builder().email(userApiRequest.getEmail())
                .username(userApiRequest.getUsername())
                .name(userApiRequest.getName())
                .build();
        UserApi respnseUser = userApiService.updateUser(id, user);
        if (respnseUser != null) {
            return new ResponseEntity<>(respnseUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Удаление пользователя.
     *
     * @param id Идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userApiService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
