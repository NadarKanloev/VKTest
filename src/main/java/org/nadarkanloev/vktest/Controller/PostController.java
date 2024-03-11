package org.nadarkanloev.vktest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.nadarkanloev.vktest.DTO.PostRequest;
import org.nadarkanloev.vktest.Model.Post;
import org.nadarkanloev.vktest.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Работа со статьями")
@Log4j2
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id){
        Post post = postService.getPostById(id);
        if(post == null){
            log.error("чзх");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }
    @PostMapping
    @Operation(summary = "Создание нового поста", description = "Этот метод создает новый пост на основе переданных данных")
    @ApiResponse(responseCode = "200", description = "Пост успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Post.class),
                    examples = @ExampleObject(value = "{\"userId\": 123, \"id\": 101, \"title\": \"Test Title\", \"body\": \"Test Body\"}")))
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.postPost(postRequest.getTitle(), postRequest.getBody(), postRequest.getUserId());
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление поста по id", description = "Этот метод обновляет пост на основе переданных данных")
    public ResponseEntity<Post> putPost(@PathVariable int id, @RequestBody PostRequest postRequest) {
        Boolean response = postService.putPost(postRequest.getTitle(), postRequest.getBody(), postRequest.getUserId(), id);
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .body(postRequest.getBody())
                .id(id)
                .userId(postRequest.getUserId())
                .build();
        if (response == true) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление поста по id")
    public ResponseEntity<Void> deletePost(@PathVariable int id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
