package org.nadarkanloev.vktest.Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.nadarkanloev.vktest.Controller.AlbumController;
import org.nadarkanloev.vktest.Model.Album;
import org.nadarkanloev.vktest.Service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
public class AlbumControllerTest {

    @Mock
    private AlbumService albumService;

    @InjectMocks
    private AlbumController controller;

    @Test
    public void testGetAllAlbums() {
        List<Album> mockAlbums = Arrays.asList(
                new Album(1, 1, "title" ),
                new Album(2, 2, "title2")
        );
        Mockito.when(albumService.getAllAlbums()).thenReturn(mockAlbums);

        ResponseEntity<List<Album>> response = controller.getAllAlbums();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAlbums, response.getBody());
    }

    @Test
    public void testGetAlbumByIdSuccess() {
        int albumId = 1;
        Album mockAlbum = new Album(albumId, 1, "Album 1");
        Mockito.when(albumService.getAlbumById(albumId)).thenReturn(mockAlbum);

        ResponseEntity<Album> response = controller.getAlbumById(albumId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockAlbum, response.getBody());
    }

    @Test
    public void testGetAlbumByIdNotFound() {
        int albumId = 1;
        Mockito.when(albumService.getAlbumById(albumId)).thenReturn(null);

        ResponseEntity<Album> response = controller.getAlbumById(albumId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateAlbumSuccess() {
        Album albumRequest = new Album(1, 1, "New Album");
        Album mockAlbum = new Album(1, albumRequest.getUserId(), albumRequest.getTitle());
        Mockito.when(albumService.postAlbum(albumRequest.getTitle(), albumRequest.getUserId())).thenReturn(mockAlbum);

        ResponseEntity<Album> response = controller.createAlbum(albumRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockAlbum, response.getBody());
    }

    @Test
    public void testCreateAlbumFailure() {
        Album albumRequest = new Album(1, 1, "New Album");
        Mockito.when(albumService.postAlbum(albumRequest.getTitle(), albumRequest.getUserId())).thenReturn(null);

        ResponseEntity<Album> response = controller.createAlbum(albumRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateAlbumSuccess() {
        int albumId = 1;
        Album albumRequest = new Album(1, 1, "Updated ALbum");
        Mockito.when(albumService.putAlbum(albumRequest.getTitle(), albumRequest.getUserId(), albumId)).thenReturn(true);

        ResponseEntity<Void> response = controller.updateAlbum(albumId, albumRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void testDeleteAlbum() {
        int albumId = 1;
        Mockito.doNothing().when(albumService).deleteAlbum(albumId);

        ResponseEntity<Void> response = controller.deleteAlbum(albumId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}