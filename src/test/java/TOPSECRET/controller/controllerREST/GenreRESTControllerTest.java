package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.GenreLinkProvider;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.genre.request.GenreRequestDTO;
import TOPSECRET.dto.genre.response.GenreResponseDTO;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.services.GenreService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.MockedStatic;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
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

    @Mock
    private UserId userId;

    private GenreRequestDTO dto;
    private GenreRESTController sut;
    private MockedStatic<UserMapper> userMapperMock;

    private static final String VALID_USER_ID = "user-123";

    @BeforeEach
    void setup() {
        dto = new GenreRequestDTO("Fiction");
        sut = new GenreRESTController(service, provider);
        userMapperMock = mockStatic(UserMapper.class);
    }

    @AfterEach
    void tearDown() {
        userMapperMock.close();
    }

    // createGenre
    @Test
    void createGenre_shouldReturnCreated_whenSuccess() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenReturn(genre);
        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Fiction");
        when(provider.getLinks(userId)).thenReturn(List.of());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void createGenre_shouldReturnNonNullBody_whenSuccess() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenReturn(genre);
        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Fiction");
        when(provider.getLinks(userId)).thenReturn(List.of());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertNotNull(result.getBody());
    }

    @Test
    void createGenre_shouldAddLinks_whenProviderReturnsLinks() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        Link link = Link.of("/genres");
        when(service.createAndSave("Fiction", userId)).thenReturn(genre);
        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Fiction");
        when(provider.getLinks(userId)).thenReturn(List.of(link));

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertEquals(1, result.getBody().getLinks().toList().size());
    }

    @Test
    void createGenre_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void createGenre_shouldReturnNullBody_whenAccessDenied() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void createGenre_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void createGenre_shouldReturnNullBody_whenIllegalArgument() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    @Test
    void createGenre_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void createGenre_shouldReturnNullBody_whenUnexpectedException() throws Exception {

        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.createAndSave("Fiction", userId)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<GenreResponseDTO> result = sut.createGenre(dto, VALID_USER_ID);

        // ASSERT
        assertNull(result.getBody());
    }

    // getGenres
    @Test
    void getGenres_shouldReturnOk_whenGenresExist() {

        // ARRANGE
        when(service.findAll()).thenReturn(List.of(genre));
        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Drama");

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getGenres_shouldReturnOneItem_whenGenresExist() {

        // ARRANGE
        when(service.findAll()).thenReturn(List.of(genre));
        when(genre.id()).thenReturn(genreId);
        when(genreId.getID()).thenReturn("g1");
        when(genre.getGenreType()).thenReturn("Drama");

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(1, result.getBody().size());
    }

    @Test
    void getGenres_shouldReturnEmptyList_whenNoGenresExist() {

        // ARRANGE
        when(service.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void getGenres_shouldReturnBadRequest_whenIllegalArgument() {

        // ARRANGE
        when(service.findAll()).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void getGenres_shouldReturnInternalServerError_whenUnexpectedException() {

        // ARRANGE
        when(service.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<GenreResponseDTO>> result = sut.getGenres();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    // OPTIONS
    @Test
    void optionsRoot_shouldReturnOkStatus() {
        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.canCreateGenre(userId)).thenReturn(true);

        // ACT
        ResponseEntity<Void> response = sut.optionsRoot(VALID_USER_ID);

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void optionsRoot_shouldAllowGet() {
        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.canCreateGenre(userId)).thenReturn(true);

        // ACT
        ResponseEntity<Void> response = sut.optionsRoot(VALID_USER_ID);

        // ASSERT
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.GET));
    }

    @Test
    void optionsRoot_shouldAllowOptions() {
        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.canCreateGenre(userId)).thenReturn(true);

        // ACT
        ResponseEntity<Void> response = sut.optionsRoot(VALID_USER_ID);

        // ASSERT
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.OPTIONS));
    }

    @Test
    void optionsRoot_shouldAllowPost_whenUserCanCreateGenre() {
        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.canCreateGenre(userId)).thenReturn(true);

        // ACT
        ResponseEntity<Void> response = sut.optionsRoot(VALID_USER_ID);

        // ASSERT
        assertTrue(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.POST));
    }

    @Test
    void optionsRoot_shouldNotAllowPost_whenUserCannotCreateGenre() {
        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId(VALID_USER_ID)).thenReturn(userId);
        when(service.canCreateGenre(userId)).thenReturn(false);

        // ACT
        ResponseEntity<Void> response = sut.optionsRoot(VALID_USER_ID);

        // ASSERT
        assertFalse(response.getHeaders().getAllow().contains(org.springframework.http.HttpMethod.POST));
    }

    @Test
    void optionsRoot_shouldReturnBadRequest_whenUserIdIsInvalid() {
        // ARRANGE
        userMapperMock.when(() -> UserMapper.toUserId("invalid")).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<Void> response = sut.optionsRoot("invalid");

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}