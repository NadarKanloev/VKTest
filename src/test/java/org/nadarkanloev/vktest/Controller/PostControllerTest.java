package org.nadarkanloev.vktest.Controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nadarkanloev.vktest.Controller.PostController;
import org.nadarkanloev.vktest.DTO.PostRequest;
import org.nadarkanloev.vktest.Enum.Role;
import org.nadarkanloev.vktest.Model.Post;
import org.nadarkanloev.vktest.Model.User;
import org.nadarkanloev.vktest.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController controller;

    private User admin;

    public void setup(){
        admin = User.builder()
                .role(Role.ROLE_ADMIN)
                .username("admin")
                .id(1L)
                .email("admim@,mail,ru")
                .password("11111")
                .build();
    }

    /** Тест GET all posts
     *
     */
    @Test
    public void testGetAllPosts() throws Exception {

        List<Post> mockPosts = Arrays.asList(new Post(1, 123, "Title 1", "Body 1"),
                new Post(2, 456, "Title 2", "Body 2"));
        Mockito.when(postService.getAllPosts()).thenReturn(mockPosts);


        ResponseEntity<List<Post>> response = controller.getAllPosts();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPosts, response.getBody());
    }

    /** Тест GET post by id - Success
     *
     */
    @Test
    public void testGetPostByIdSuccess() throws Exception {
        int postId = 1;
        Post mockPost = new Post(postId, 123, "Title 1", "Body 1");
        Mockito.when(postService.getPostById(postId)).thenReturn(mockPost);

        ResponseEntity<Post> response = controller.getPostById(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPost, response.getBody());
    }

    /** Тест GET post by id - Not Found
     *
     */
    @Test
    public void testGetPostByIdNotFound() throws Exception {
        int postId = 1;
        Mockito.when(postService.getPostById(postId)).thenReturn(null);

        ResponseEntity<Post> response = controller.getPostById(postId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreatePostSuccess() throws Exception {
        PostRequest postRequest = new PostRequest("New Title", "New Body", 789);
        Post mockPost = new Post(10, postRequest.getUserId(), postRequest.getTitle(), postRequest.getBody());
        PostService mockPostService = Mockito.mock(PostService.class);
        Mockito.when(mockPostService.postPost(postRequest.getTitle(), postRequest.getBody(), postRequest.getUserId())).thenReturn(mockPost);

        PostController controller = new PostController(mockPostService);

        ResponseEntity<Post> response = controller.createPost(postRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockPost, response.getBody());
    }

    @Test
    public void testCreatePostFailure() throws Exception {
        PostRequest postRequest = new PostRequest("New Title", "New Body", 789);
        PostService mockPostService = Mockito.mock(PostService.class);
        Mockito.when(mockPostService.postPost(postRequest.getTitle(), postRequest.getBody(), postRequest.getUserId())).thenReturn(null);

        PostController controller = new PostController(mockPostService);

        ResponseEntity<Post> response = controller.createPost(postRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testPutPostSuccess() throws Exception {
        int postId = 1;
        PostRequest postRequest = new PostRequest("Updated Title", "Updated Body", 789);
        PostService mockPostService = Mockito.mock(PostService.class);
        Mockito.when(mockPostService.putPost(postRequest.getTitle(), postRequest.getBody(), postRequest.getUserId(), postId)).thenReturn(true);

        PostController controller = new PostController(mockPostService);

        ResponseEntity<Post> response = controller.putPost(postId, postRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postRequest.getTitle(), response.getBody().getTitle());
        assertEquals(postRequest.getBody(), response.getBody().getBody());
    }
    @Test
    public void testDeletePost() throws Exception {
        int postId = 1;


        Mockito.doNothing().when(postService).deletePost(postId);

        ResponseEntity<Void> response = controller.deletePost(postId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
