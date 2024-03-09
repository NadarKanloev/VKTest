package org.nadarkanloev.vktest.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.Model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class PostService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<Post> getAllPosts(){
        try {
            String url = "https://jsonplaceholder.typicode.com/posts";
            String json = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(json, new TypeReference<List<Post>>() {});
        }catch (IOException e) {
            log.error("Ошибка при обработке ответа от API", e);
        }
        return Collections.emptyList();
    }
}
