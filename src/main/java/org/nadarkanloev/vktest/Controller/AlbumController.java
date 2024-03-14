package org.nadarkanloev.vktest.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.nadarkanloev.vktest.Model.Album;
import org.nadarkanloev.vktest.Service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

/**
 * Контроллер для работы с альбомами.
 */
@RestController
@RequestMapping("/api/albums")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ALBUMS')")
@RequiredArgsConstructor
@Tag(name = "Работа с альбомами")
public class AlbumController {
    private final AlbumService albumService;

    /**
     * Получение списка всех альбомов.
     *
     * @return Список всех альбомов
     */
    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums();
        return ResponseEntity.ok(albums);
    }

    /**
     * Получение альбома по его идентификатору.
     *
     * @param id Идентификатор альбома
     * @return Альбом, если найден, иначе статус "Not Found"
     */
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable int id) {
        Album album = albumService.getAlbumById(id);
        if (album != null) {
            return ResponseEntity.ok(album);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создание нового альбома.
     *
     * @param album Данные нового альбома
     * @return Созданный альбом или статус "Internal Server Error" в случае ошибки
     */
    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        Album createdAlbum = albumService.postAlbum(album.getTitle(), album.getUserId());
        if (createdAlbum != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAlbum);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Обновление информации об альбоме.
     *
     * @param id    Идентификатор альбома, который нужно обновить
     * @param album Обновленные данные альбома
     * @return Статус "Accepted", если обновление прошло успешно, иначе "Not Found"
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAlbum(@PathVariable int id, @RequestBody Album album) {
        boolean success = albumService.putAlbum(album.getTitle(), album.getUserId(), id);
        if (success) {
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаление альбома по его идентификатору.
     *
     * @param id Идентификатор альбома для удаления
     * @return Статус "No Content", если альбом успешно удален, иначе "Not Found"
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable int id){
        try {
            albumService.deleteAlbum(id);
            return ResponseEntity.noContent().build();
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
}
