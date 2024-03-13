package org.nadarkanloev.vktest.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.Model.Audition;
import org.nadarkanloev.vktest.Model.UserApi;
import org.nadarkanloev.vktest.Repository.AuditionRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.nadarkanloev.vktest.Service.UUIDGenerator.generateUniqueId;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;
    private final UserService userService;
    private final AuditionRepository auditionRepository;
    private String url = "https://jsonplaceholder.typicode.com/users";

    /**
     * Получение всех пользователей.
     *
     * @return Список всех пользователей.
     */
    public List<UserApi> getAllUsers() {
        //Провеерка кэша, если в кэше есть подходящее значение, то возвращаем оттуда
        Cache cache = cacheManager.getCache("userCache");
        Cache.ValueWrapper valueWrapper = cache != null ? cache.get("allUsers") : null;
        if (valueWrapper != null) {
            String cachedUsers =  (String) valueWrapper.get();
            log.info("привет из кэша");
            logAudition("GET", "allUsers", "from-cache", "200 OK", cachedUsers.toString(), "-");
            try {
                return objectMapper.readValue(cachedUsers, new TypeReference<List<UserApi>>() {});
            } catch (JsonProcessingException e) {
                log.error("Ошибка при обработке ответа от API", e);
                return Collections.emptyList();
            }
        }
        try {
            String json = restTemplate.getForObject(url, String.class);
            logAudition("GET", "None", "none", "200 OK", json, "-");

            cache.put("allUsers", json);
            return objectMapper.readValue(json, new TypeReference<List<UserApi>>() {});
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            return Collections.emptyList();
        }
    }

    /**
     * Получение пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Пользователь с указанным идентификатором или null, если не удалось получить данные.
     */
    public UserApi getUserById(int id) {
        Cache cache = cacheManager.getCache("userCache");
        Cache.ValueWrapper valueWrapper = cache != null ? cache.get(id) : null;
        if(valueWrapper != null){
            UserApi cachedUser = (UserApi) valueWrapper.get();
            logAudition("GET", String.valueOf(id), "from-cache", "200 OK", cachedUser.toString(), "-");
            return cachedUser;
        }
        String urlWithId = url + "/" + id;
        String json = restTemplate.getForObject(urlWithId, String.class);
        try {
            cache.put(id, json);
            return objectMapper.readValue(json, UserApi.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            return null;
        }
    }

    /**
     * Создание нового пользователя.
     *
     * @param user Данные нового пользователя.
     * @return Созданный пользователь или null в случае ошибки.
     */
    public UserApi createUser(UserApi user) {
        String json;
        try {
            json = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при преобразовании объекта в JSON", e);
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            try {
                return objectMapper.readValue(responseEntity.getBody(), UserApi.class);
            } catch (JsonProcessingException e) {
                log.error("Ошибка при обработке ответа от API", e);
                return null;
            }
        } else {
            log.error("Ошибка при создании пользователя: {}", responseEntity);
            return null;
        }
    }

    /**
     * Обновление существующего пользователя.
     *
     * @param id   Идентификатор пользователя.
     * @param user Обновленные данные пользователя.
     * @return Обновленный пользователь или null в случае ошибки.
     */
    public UserApi updateUser(int id, UserApi user) {
        String urlWithId = url + "/" + id;
        String json;
        try {
            json = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при преобразовании объекта в JSON", e);
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(urlWithId, HttpMethod.PUT, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            try {
                UserApi updatedUser = objectMapper.readValue(responseEntity.getBody(), UserApi.class);
                logAudition("PUT", String.valueOf(id), "-", "200 OK", json, "-");
                return updatedUser;
            } catch (JsonProcessingException e) {
                log.error("Ошибка при обработке ответа от API", e);
                logAudition("PUT", String.valueOf(id), "-", "500 Internal Server Error", "-", "Ошибка при обработке ответа от API");
                return null;
            }
        } else {
            log.error("Ошибка при обновлении пользователя: {}", responseEntity);
            logAudition("PUT", String.valueOf(id), "-", "500 Internal Server Error", "-", "Ошибка при обновлении пользователя");
            return null;
        }
    }
    /**
     * Удаление пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return true, если удаление выполнено успешно, в противном случае - false.
     */
    public void deleteUser(int id) {
        String urlWithId = url + "/" + id;
        try {
            restTemplate.delete(urlWithId);
            logAudition("DELETE", String.valueOf(id), "-", "204 No Content", "-", "-");
        } catch (HttpClientErrorException e) {
            log.error("Ошибка при удалении пользователя: {}", e.getStatusCode());
            logAudition("DELETE", String.valueOf(id), "-", "500 Internal Server Error", "-", "Ошибка при удалении пользователя");
        }
    }

    /**
     * Запись аудита.
     *
     * @param httpMethod      HTTP метод.
     * @param requestParams   Параметры запроса.
     * @param cacheInfo       Информация о кэше.
     * @param responseStatus  Статус ответа.
     * @param serverResponse  Ответ от сервера.
     */
    private void logAudition(String httpMethod, String requestParams, String cacheInfo, String responseStatus, String serverResponse, String error) {
        Audition audition = Audition.builder()
                .section("USERS")
                .errorLogs("error")
                .userRole(userService.getRole())
                .userId(String.valueOf(userService.getid()))
                .httpMethod(httpMethod)
                .requestParams(Collections.singletonMap("requst Params:", requestParams))
                .cachingInfo(cacheInfo)
                .errorLogs("-")
                .timeStamp(LocalDateTime.now())
                .serverResponse(serverResponse)
                .UUID(generateUniqueId() + userService.getid())
                .responseStatus(responseStatus)
                .build();
        auditionRepository.save(audition);
        log.info(audition.toString());
    }
}
