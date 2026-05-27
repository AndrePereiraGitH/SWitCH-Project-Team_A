package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.AuthorMapper;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorRESTController}.
 *
 * <p>Validates HTTP responses, exception handling and DTO mapping.</p>
 */
@ExtendWith(MockitoExtension.class)
class AuthorRESTControllerTest {

    @Mock
    private AuthorService service;

    @Mock
    private AuthorLinkProvider provider;

    @Mock
    private AuthorMapper mapper;

    @Mock
    private Author author;

    @Mock
    private AuthorId authorId;

    private UserId userId;
    private CreateAuthorDTO dto;

    @BeforeEach
    void setup() {
        userId = new UserId("user-1");
        dto = new CreateAuthorDTO(
                "José Saramago",
                "PORTUGAL",
                "1922-11-16",
                "Portuguese Nobel Prize-winning author."
        );
    }

    // =========================================================================
    // createAuthor — success
    // =========================================================================

    @Test
    void createAuthor_shouldReturnCreated_whenSuccess() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, mapper);

        when(service.createAndSave(any(), any(), any(), any(), any())).thenReturn(author);
        when(mapper.toDTO(author)).thenReturn(new AuthorDTO(
                "some-uuid", "José Saramago", "Portugal", "1922-11-16",
                "Portuguese Nobel Prize-winning author."));
        when(provider.getLinks(any(UserId.class))).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId.getUserId());

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void createAuthor_shouldAddLinks_whenProviderReturnsLinks() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, mapper);

        Link link = Link.of("/authors");

        when(service.createAndSave(any(), any(), any(), any(), any())).thenReturn(author);
        when(mapper.toDTO(author)).thenReturn(new AuthorDTO(
                "some-uuid", "José Saramago", "Portugal", "1922-11-16",
                "Portuguese Nobel Prize-winning author."));

        List<Link> linkList = new ArrayList<>();
        linkList.add(link);
        when(provider.getLinks(any(UserId.class))).thenReturn(linkList);

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId.getUserId());

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().getLinks().toList().size());
    }

    // =========================================================================
    // createAuthor — exceptions
    // =========================================================================

    @Test
    void createAuthor_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, mapper);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId.getUserId());

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createAuthor_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, mapper);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId.getUserId());

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createAuthor_shouldReturnConflict_whenInstanceAlreadyExists() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, mapper);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new InstanceAlreadyExistsException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId.getUserId());

        // ASSERT
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createAuthor_shouldReturnInternalServerError_whenUnexpectedException()
            throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, mapper);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId.getUserId());

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }
}