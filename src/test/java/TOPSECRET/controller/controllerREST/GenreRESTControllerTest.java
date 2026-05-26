package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.GenreLinkProvider;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.genre.request.GenreRequestDTO;
import TOPSECRET.dto.genre.response.GenreResponseDTO;
import TOPSECRET.services.GenreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link GenreRESTController}.
 *
 * <p>Validates HTTP responses, exception handling and DTO mapping.</p>
 */
@ExtendWith(MockitoExtension.class)
class GenreRESTControllerTest {

    @Mock
    private GenreService service;

    @Mock
    private GenreLinkProvider provider;

    @Mock
    private Genre genre;

    @Mock
    private GenreId genreId;

    private UserId userId;
    private GenreRequestDTO dto;

    @BeforeEach
    void setup() {

        userId = new UserId("user-1");
        dto = new GenreRequestDTO("Fiction");
    }

    @Test
    void createGenre_shouldReturnCreated_whenSuccess() throws Exception {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.createAndSave("Fiction", userId)).thenReturn(genre);

        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Fiction");

        when(provider.getLinks(userId)).thenReturn(List.of());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void createGenre_shouldAddLinks_whenProviderReturnsLinks() throws Exception {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        Link link = Link.of("/genres");

        when(service.createAndSave("Fiction", userId)).thenReturn(genre);

        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Fiction");

        when(provider.getLinks(userId)).thenReturn(List.of(link));

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, userId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getLinks().toList().size());
    }

    @Test
    void createGenre_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.createAndSave("Fiction", userId)).thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createGenre_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.createAndSave("Fiction", userId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createGenre_shouldReturnInternalServerError_whenUnexpectedException()
            throws Exception {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.createAndSave("Fiction", userId)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getGenres_shouldReturnOk_whenGenresExist() {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.findAll()).thenReturn(List.of(genre));

        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Drama");

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void getGenres_shouldReturnEmptyList_whenNoGenresExist() {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void getGenres_shouldReturnBadRequest_whenIllegalArgument() {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.findAll()).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getGenres_shouldReturnInternalServerError_whenUnexpectedException() {

        // ARRANGE
        GenreRESTController sut = new GenreRESTController(service, provider);

        when(service.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }
}