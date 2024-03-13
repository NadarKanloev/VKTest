package org.nadarkanloev.vktest.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.Model.Audition;
import org.nadarkanloev.vktest.Model.Post;
import org.nadarkanloev.vktest.Repository.AuditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import javax.sound.sampled.AudioFileFormat;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.nadarkanloev.vktest.Enum.Role.*;
import static org.nadarkanloev.vktest.Service.UUIDGenerator.generateUniqueId;

/**
 * Сервис для работы с постами.
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class PostService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;
    private final UserService userService;
    private final AuditionRepository auditionRepository;
    private String url = "https://jsonplaceholder.typicode.com/posts";

    /**
     * Получение всех постов.
     *
     * @return Список всех постов.
     */
    public List<Post> getAllPosts() {
        try {
            String json = restTemplate.getForObject(url, String.class);
            logAuditon("GET", "None", "none", "200 OK", json, "-");
            log.info(objectMapper.readValue(json, new TypeReference<List<Post>>() {}).toString());
            return objectMapper.readValue(json, new TypeReference<List<Post>>() {});
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            logAuditon("GET", "-", "None", "500 Internal Server Error", "-", "Ошибка при обработке ответа от API");
            return Collections.emptyList();
        }
    }

    /**
     * Получение поста по идентификатору.
     *
     * @param id Идентификатор поста.
     * @return Пост с указанным идентификатором или пустой пост, если не удалось получить данные.
     */

    public Post getPostById(int id) {
        //Проверка кэша
        Cache cache = cacheManager.getCache("postCache");
        Cache.ValueWrapper valueWrapper = cache != null ? cache.get(id) : null;
        if(valueWrapper != null){
            Post cachedPost = (Post) valueWrapper.get();
            logAuditon("GET", String.valueOf(id), "from-cache", "200 OK", cachedPost.toString(), "-");
            return cachedPost;
        }
        //Если кэша нет, то запрашиваемся к API
        String urlWithId = url + String.format("/%s", id);
        String json = restTemplate.getForObject(urlWithId, String.class);
        try {
            Post post = objectMapper.readValue(json, Post.class);
            cache.put(id, post);
            logAuditon("GET", String.valueOf(id), "None", "200 OK", post.toString(), "-");
            return post;
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            logAuditon("GET", String.valueOf(id), "None", "500 Internal Server Error", "-", "Ошибка при обработке ответа от API");
            return null;
        }
    }

    /**
     * Создание нового поста.
     *
     * @param title  Заголовок поста.
     * @param body   Тело поста.
     * @param userId Идентификатор пользователя.
     * @return Созданный пост.
     */
    public Post postPost(String title, String body, int userId) {
        Post post = Post.builder()
                .title(title)
                .userId(userId)
                .body(body)
                .build();
        String json = restTemplate.postForObject(url, post, String.class);

        try {
            logAuditon("POST", "None", "none", "200 OK", "-", "-");
            return objectMapper.readValue(json, Post.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            logAuditon("POST", title + ", " + body+ "," + userId, "none", "500 Internal Server Error", "-", "Ошибка при обработке ответа от API");
            return null;
        }
        catch (HttpClientErrorException e){
            log.error(e.getStatusCode() + " "+  e.getMessage());
            logAuditon("POST", title + ", " + body+ "," + userId, "none", "500 Internal Server Error", "-", e.getMessage());
            return null;
        }
    }

    /**
     * Обновление существующего поста.
     *
     * @param title  Заголовок поста.
     * @param body   Тело поста.
     * @param userId Идентификатор пользователя.
     * @param id     Идентификатор поста.
     * @return true, если обновление выполнено успешно, в противном случае - false.
     */
    public boolean putPost(String title, String body, int userId, int id) {
        url = url + String.format("/%s", id);
        Post post = Post.builder()
                .title(title)
                .userId(userId)
                .body(body)
                .id(id)
                .build();
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(post), Void.class);
            log.info(responseEntity.getStatusCode());
            logAuditon("PUT", title + ", " + body+ "," + userId + id, "none", "203 Accepted", "-", "-");
            return true;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("Пост с id {} не найден", id);
                logAuditon("PUT", title + ", " + body+ "," + userId + id, "none", "500 Internal Server Error", "-", String.format("Пост с id {} не найден", id));
                return false;
            } else {
                log.error("Ошибка при выполнении запроса PUT для обновления поста", e);
                logAuditon("PUT", title + ", " + body+ "," + userId + id, "none", "500 Internal Server Error", "-", "Ошибка при выполнении запроса PUT для обновления поста");
                return false;
            }
        }
    }

    /**
     * Удаление поста по идентификатору.
     *
     * @param id Идентификатор поста.
     */
    public void deletePost(int id) {
        url = url + String.format("/%s", id);
        try{
            restTemplate.delete(url);
            logAuditon("DELETE", String.valueOf(id), "none", "204 No Content", "-", "-");
        }catch (HttpClientErrorException e){
            logAuditon("DELETE", String.valueOf(id), "none", "500 Internal Server Error", "-", "-");
            log.info(e.getStatusCode() + e.getMessage());
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
    private void logAuditon(String httpMethod, String requestParams, String cacheInfo, String responseStatus, String serverResponse, String error) {
        Audition audition = Audition.builder()
                .section("POSTS")
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