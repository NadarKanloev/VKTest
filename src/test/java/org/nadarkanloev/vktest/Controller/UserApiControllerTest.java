package org.nadarkanloev.vktest.Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.nadarkanloev.vktest.DTO.UserApiRequest;
import org.nadarkanloev.vktest.Model.UserApi;
import org.nadarkanloev.vktest.Service.UserApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
public class UserApiControllerTest {

    @Mock
    private UserApiService userApiService;

    @InjectMocks
    private UserApiController controller;

    @Test
    public void testGetAllUsers() {
        List<UserApi> mockUsers = Arrays.asList(
                new UserApi(1, "Leanne Graham", "Bret", "Sincere@april.biz"),
                new UserApi(2, "Ervin Howell", "Antonette", "Shanna@melissa.tv"),
                new UserApi(3, "Clementine Bauch", "Samantha", "Nathan@yesenia.net")
        );
        Mockito.when(userApiService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<UserApi>> response = controller.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
    }

    @Test
    public void testGetUserByIdSuccess() {
        int userId = 1;
        UserApi mockUser = new UserApi(userId, "Leanne Graham", "Bret", "Sincere@april.biz");
        Mockito.when(userApiService.getUserById(userId)).thenReturn(mockUser);

        ResponseEntity<UserApi> response = controller.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    public void testGetUserByIdNotFound() {
        int userId = 1;
        Mockito.when(userApiService.getUserById(userId)).thenReturn(null);

        ResponseEntity<UserApi> response = controller.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateUserSuccess() {
        UserApiRequest userRequest = new UserApiRequest("Leanne Graham", "Bret", "Sincere@april.biz");
        UserApi mockUser = new UserApi(1, userRequest.getName(), userRequest.getUsername(), userRequest.getEmail());
        Mockito.when(userApiService.createUser(Mockito.any(UserApi.class))).thenReturn(mockUser);

        ResponseEntity<UserApi> response = controller.createUser(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    public void testCreateUserFailure() {
        UserApiRequest userRequest = new UserApiRequest("Leanne Graham", "Bret", "Sincere@april.biz");
        Mockito.when(userApiService.createUser(Mockito.any(UserApi.class))).thenReturn(null);

        ResponseEntity<UserApi> response = controller.createUser(userRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateUserSuccess() {
        int userId = 1;
        UserApiRequest userRequest = new UserApiRequest("Leanne Graham Updated", "Bret", "Sincere@april.biz");
        UserApi mockUser = new UserApi(userId, userRequest.getName(), userRequest.getUsername(), userRequest.getEmail());
        Mockito.when(userApiService.updateUser(Mockito.eq(userId), Mockito.any(UserApi.class))).thenReturn(mockUser);

        ResponseEntity<UserApi> response = controller.putUser(userId, userRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    public void testUpdateUserFailure() {
        int userId = 1;
        UserApiRequest userRequest = new UserApiRequest("Leanne Graham Updated", "Bret", "Sincere@april.biz");
        Mockito.when(userApiService.updateUser(Mockito.eq(userId), Mockito.any(UserApi.class))).thenReturn(null);

        ResponseEntity<UserApi> response = controller.putUser(userId, userRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteUser() {
        int userId = 1;
        Mockito.doNothing().when(userApiService).deleteUser(userId);

        ResponseEntity<Void> response = controller.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
