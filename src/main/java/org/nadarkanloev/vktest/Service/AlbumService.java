package org.nadarkanloev.vktest.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.Model.Album;
import org.nadarkanloev.vktest.Model.Audition;
import org.nadarkanloev.vktest.Repository.AuditionRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AlbumService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;
    private final UserService userService;
    private final AuditionRepository auditionRepository;
    private String url = "https://jsonplaceholder.typicode.com/albums";

    /**
     * Получение всех альбомов.
     *
     * @return Список всех альбомов.
     */
    public List<Album> getAllAlbums() {
        try {
            String json = restTemplate.getForObject(url, String.class);
            logAudition("GET", "None", "none", "200 OK", json, "-");
            log.info(objectMapper.readValue(json, new TypeReference<List<Album>>() {}).toString());
            return objectMapper.readValue(json, new TypeReference<List<Album>>() {});
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            logAudition("GET", "-", "None", "500 Internal Server Error", "-", "Ошибка при обработке ответа от API");
            return Collections.emptyList();
        }
    }

    /**
     * Получение альбома по идентификатору.
     *
     * @param id Идентификатор альбома.
     * @return Альбом с указанным идентификатором или пустой альбом, если не удалось получить данные.
     */
    public Album getAlbumById(int id) {
        Cache cache = cacheManager.getCache("albumCache");
        Cache.ValueWrapper valueWrapper = cache != null ? cache.get(id) : null;
        if (valueWrapper != null) {
            Album cachedAlbum = (Album) valueWrapper.get();
            logAudition("GET", String.valueOf(id), "from-cache", "200 OK", cachedAlbum.toString(), "-");
            return cachedAlbum;
        }

        String urlWithId = url + "/" + id;
        String json = restTemplate.getForObject(urlWithId, String.class);
        try {
            Album album = objectMapper.readValue(json, Album.class);
            cache.put(id, album);
            logAudition("GET", String.valueOf(id), "None", "200 OK", album.toString(), "-");
            return album;
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            logAudition("GET", String.valueOf(id), "None", "500 Internal Server Error", "-", "Ошибка при обработке ответа от API");
            return null;
        }
    }

    /**
     * Создание нового альбома.
     *
     * @param title  Заголовок альбома.
     * @param userId Идентификатор пользователя.
     * @return Созданный альбом.
     */
    public Album postAlbum(String title, int userId) {
        Album album = Album.builder()
                .title(title)
                .userId(userId)
                .build();
        String json = restTemplate.postForObject(url, album, String.class);

        try {
            logAudition("POST", "None", "none", "200 OK", "-", "-");
            return objectMapper.readValue(json, Album.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            logAudition("POST", title + ", " + userId, "none", "500 Internal Server Error", "-", "Ошибка при обработке ответа от API");
            return null;
        } catch (HttpClientErrorException e) {
            log.error(e.getStatusCode() + " " + e.getMessage());
            logAudition("POST", title + ", " + userId, "none", "500 Internal Server Error", "-", e.getMessage());
            return null;
        }
    }

    /**
     * Обновление существующего альбома.
     *
     * @param title  Заголовок альбома.
     * @param userId Идентификатор пользователя.
     * @param id     Идентификатор альбома.
     * @return true, если обновление выполнено успешно, в противном случае - false.
     */
    public boolean putAlbum(String title, int userId, int id) {
        String urlWithId = url + "/" + id;
        Album album = Album.builder()
                .title(title)
                .userId(userId)
                .id(id)
                .build();
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(urlWithId, HttpMethod.PUT, new HttpEntity<>(album), Void.class);
            log.info(responseEntity.getStatusCode());
            logAudition("PUT", title + ", " + userId + ", " + id, "none", "203 Accepted", "-", "-");
            return true;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("Альбом с id {} не найден", id);
                logAudition("PUT", title + ", " + userId + ", " + id, "none", "500 Internal Server Error", "-", String.format("Альбом с id {} не найден", id));
                return false;
            } else {
                log.error("Ошибка при выполнении запроса PUT для обновления альбома", e);
                logAudition("PUT", title + ", " + userId + ", " + id, "none", "500 Internal Server Error", "-", "Ошибка при выполнении запроса PUT для обновления альбома");
                return false;
            }
        }
    }

    /**
     * Удаление альбома по идентификатору.
     *
     * @param id Идентификатор альбома.
     */
    public void deleteAlbum(int id) {
        String urlWithId = url + "/" + id;
        try {
            restTemplate.delete(urlWithId);
            logAudition("DELETE", String.valueOf(id), "none", "204 No Content", "-", "-");
        } catch (HttpClientErrorException e) {
            log.info(e.getStatusCode() + " " + e.getMessage());
            logAudition("DELETE", String.valueOf(id), "none", "500 Internal Server Error", "-", "Ошибка при выполнении запроса DELETE для обновления альбома");
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
                .section("ALBUMS")
                .userRole(userService.getRole())
                .userId(String.valueOf(userService.getid()))
                .httpMethod(httpMethod)
                .requestParams(Collections.singletonMap("requestParams:", requestParams))
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
