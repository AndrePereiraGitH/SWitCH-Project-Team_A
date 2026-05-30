package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import TOPSECRET.services.CountryService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorRESTController}.
 *
 * <p>This class verifies the behaviour of the REST controller responsible
 * for managing Author resources, covering both successful operations and
 * error handling scenarios.</p>
 *
 * <p>The following scenarios are covered:</p>
 * <ul>
 *   <li>Successful author creation returning HTTP 201 Created</li>
 *   <li>HATEOAS links are correctly added to the response body</li>
 *   <li>HTTP 403 Forbidden when the user lacks permission</li>
 *   <li>HTTP 400 Bad Request when invalid arguments are provided</li>
 *   <li>HTTP 409 Conflict when the author already exists</li>
 *   <li>HTTP 500 Internal Server Error on unexpected failures</li>
 *   <li>Successful retrieval of all authors returning HTTP 200 OK</li>
 *   <li>Empty list returned when no authors exist</li>
 *   <li>Successful retrieval of a single author by ID returning HTTP 200 OK</li>
 *   <li>HTTP 400 Bad Request when author ID is not found</li>
 * </ul>
 *
 * <p>All dependencies are mocked using Mockito, isolating the controller
 * from the service, repository, and link provider layers.</p>
 */
@ExtendWith(MockitoExtension.class)
class AuthorRESTControllerTest {

    @Mock
    private AuthorService service;

    @Mock
    private AuthorLinkProvider provider;

    @Mock
    private CountryService countryService;

    @Mock
    private Author author;

    @Mock
    private AuthorId authorId;

    @Mock
    private Country country;

    @Mock
    private CountryId countryId;

    @Mock
    private UserId userId;

    private CreateAuthorDTO dto;

    @BeforeEach
    void setup() {
        dto = new CreateAuthorDTO(
                "José Saramago",
                "PORTUGAL",
                LocalDate.of(1922, 11, 16),
                "Portuguese Nobel Prize-winning author."
        );
    }

    private void setupAuthorMocks() {
        when(authorId.getId()).thenReturn("some-uuid");
        when(author.id()).thenReturn(authorId);
        when(author.getName()).thenReturn("José Saramago");
        when(author.getCountryID()).thenReturn(countryId);
        when(author.getDateOfBirth()).thenReturn(LocalDate.of(1922, 11, 16));
        when(author.getBiography()).thenReturn("Portuguese Nobel Prize-winning author.");
        when(country.getCountryName()).thenReturn("Portugal");
    }

    // =========================================================================
    // createAuthor — success
    // =========================================================================

    @Test
    void createAuthor_shouldReturnCreated_whenSuccess() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);
        setupAuthorMocks();

        when(service.createAndSave(any(), any(), any(), any(), any())).thenReturn(author);
        when(countryService.findById(any())).thenReturn(country);
        when(provider.getLinks(any(UserId.class))).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void createAuthor_shouldAddLinks_whenProviderReturnsLinks() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);
        setupAuthorMocks();

        Link link = Link.of("/authors");
        List<Link> linkList = new ArrayList<>();
        linkList.add(link);

        when(service.createAndSave(any(), any(), any(), any(), any())).thenReturn(author);
        when(countryService.findById(any())).thenReturn(country);
        when(provider.getLinks(any(UserId.class))).thenReturn(linkList);

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId);

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
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createAuthor_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createAuthor_shouldReturnConflict_whenInstanceAlreadyExists() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new InstanceAlreadyExistsException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createAuthor_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.createAndSave(any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findAll — success
    // =========================================================================

    @Test
    void findAll_shouldReturnOk_whenAuthorsExist() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);
        setupAuthorMocks();

        when(service.findAll()).thenReturn(List.of(author));
        when(countryService.findById(any())).thenReturn(country);

        // ACT
        ResponseEntity<List<AuthorDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoAuthorsExist() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<AuthorDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void findAll_shouldAddFindByIdLink_toEachAuthor() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);
        setupAuthorMocks();

        when(service.findAll()).thenReturn(List.of(author));
        when(countryService.findById(any())).thenReturn(country);

        // ACT
        ResponseEntity<List<AuthorDTO>> result = sut.findAll();

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().get(0).getLinks().toList().size());
        assertTrue(result.getBody().get(0).hasLink("find-by-id"));
    }

    // =========================================================================
    // findAll — exceptions
    // =========================================================================

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedException() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<AuthorDTO>> result = sut.findAll();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    // =========================================================================
    // findById — success
    // =========================================================================

    @Test
    void findById_shouldReturnOk_whenAuthorExists() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);
        setupAuthorMocks();

        when(service.findById(authorId)).thenReturn(author);
        when(countryService.findById(any())).thenReturn(country);

        // ACT
        ResponseEntity<AuthorDTO> result = sut.findById(authorId);

        // ASSERT
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void findById_shouldAddSelfAndFindByIdLinks() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);
        setupAuthorMocks();

        when(service.findById(authorId)).thenReturn(author);
        when(countryService.findById(any())).thenReturn(country);

        // ACT
        ResponseEntity<AuthorDTO> result = sut.findById(authorId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().getLinks().toList().size());
    }

    // =========================================================================
    // findById — exceptions
    // =========================================================================

    @Test
    void findById_shouldReturnBadRequest_whenIllegalArgument() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.findById(authorId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.findById(authorId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedException() {

        // ARRANGE
        AuthorRESTController sut = new AuthorRESTController(service, provider, countryService);

        when(service.findById(authorId)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<AuthorDTO> result = sut.findById(authorId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }
}