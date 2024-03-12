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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
            return objectMapper.readValue(json, new TypeReference<List<Post>>() {});
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
        }
        return Collections.emptyList();
    }

    /**
     * Получение поста по идентификатору.
     *
     * @param id Идентификатор поста.
     * @return Пост с указанным идентификатором или пустой пост, если не удалось получить данные.
     */

    public Post getPostById(int id) {
        Cache cache = cacheManager.getCache("postCache");
        Cache.ValueWrapper valueWrapper = cache.get(id);
        if (valueWrapper != null) {
            Audition audition = Audition.builder()
                    .userRole(userService.getRole())
                    .userId(String.valueOf(userService.getid()))
                    .httpMethod("GET")
                    .requestParams(new HashMap<String, String>() {{
                        put("id", String.valueOf(id));
                    }})
                    .cachingInfo("from-cache")
                    .responseStatus("200 OK")
                    .errorLogs("-")
                    .timeStamp(LocalDateTime.now())
                    .UUID(100030L)
                    .serverResponse(valueWrapper.get().toString())
                    .build();
            auditionRepository.save(audition);
            log.info(audition.toString());
            return (Post) valueWrapper.get();
        }
        String urlWithId = url + String.format("/%s", id);
        String json = restTemplate.getForObject(urlWithId, String.class);
        try {
            Post post = objectMapper.readValue(json, Post.class);
            post.setCached(true);
            cache.put(id, post);
            post.setCached(false);
            return post;
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
            log.info("Во время запроса произошла ошибка");
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
            return objectMapper.readValue(json, Post.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при обработке ответа от API", e);
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
            return true;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("Пост с id {} не найден", id);
            } else {
                log.error("Ошибка при выполнении запроса PUT для обновления поста", e);
            }
        }
        return false;
    }

    /**
     * Удаление поста по идентификатору.
     *
     * @param id Идентификатор поста.
     */
    public void deletePost(int id) {
        url = url + String.format("/%s", id);
        restTemplate.delete(url);
    }
}
