package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.AuthorLinkProvider;
import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.author.AuthorMapper;
import TOPSECRET.dto.author.request.CreateAuthorDTO;
import TOPSECRET.dto.author.response.AuthorDTO;
import TOPSECRET.services.AuthorService;
import TOPSECRET.services.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthorRESTControllerTest {

    @Mock private AuthorService service;
    @Mock private AuthorLinkProvider provider;
    @Mock private CountryService countryService;
    @Mock private Author author;
    @Mock private AuthorId authorId;
    @Mock private Country country;
    @Mock private CountryId countryId;
    @Mock private UserId userId;

    private static final String USER_ID_STR   = "user-uuid-123";
    private static final String AUTHOR_ID_STR = "author-uuid-456";

    private static final Link CREATE_LINK = Link.of("/authors").withRel("create-author");

    private CreateAuthorDTO dto;
    private AuthorRESTController sut;

    @BeforeEach
    void setup() {
        dto = new CreateAuthorDTO(
                "José Saramago",
                "PORTUGAL",
                LocalDate.of(1922, 11, 16),
                "Portuguese Nobel Prize-winning author."
        );
        sut = new AuthorRESTController(service, provider, countryService);
    }

    private AuthorDTO freshDTO() {
        return new AuthorDTO(AUTHOR_ID_STR, "José Saramago", "Portugal", "1922-11-16",
                "Portuguese Nobel Prize-winning author.");
    }

    /**
     * findAll and findById use linkTo(methodOn(...).findById(...)) which
     * re-executes the full findById body. All dependencies must be stubbed
     * so the proxy doesn't throw and get caught as a 500.
     */
    private void stubProxyFindById(MockedStatic<AuthorMapper> mapper) {
        mapper.when(() -> AuthorMapper.toAuthorId(any())).thenReturn(authorId);
        mapper.when(() -> AuthorMapper.toUserId(any())).thenReturn(userId);
        mapper.when(() -> AuthorMapper.toDTO(any(), any())).thenReturn(freshDTO());
        when(author.getCountryID()).thenReturn(countryId);
        when(author.id()).thenReturn(authorId);
        when(authorId.toString()).thenReturn(AUTHOR_ID_STR);
        when(countryService.findById(any())).thenReturn(country);
        when(provider.getLinks(any())).thenReturn(new ArrayList<>());
        // service.findById NOT stubbed here — each test controls it directly
    }

    // =========================================================================
    // createAuthor — success
    // createAuthor uses linkTo(AuthorRESTController.class).slash(...) — no proxy
    // =========================================================================

    @Test
    void createAuthor_shouldReturnCreated_whenSuccess() throws Exception {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            // ARRANGE
            mapper.when(() -> AuthorMapper.toUserId(USER_ID_STR)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toCountryId(dto)).thenReturn(countryId);
            mapper.when(() -> AuthorMapper.toDTO(author, country)).thenReturn(freshDTO());

            when(author.getCountryID()).thenReturn(countryId);
            when(author.id()).thenReturn(authorId);
            when(authorId.toString()).thenReturn(AUTHOR_ID_STR);
            when(service.createAndSave(any(), any(), any(), any(), any())).thenReturn(author);
            when(countryService.findById(any())).thenReturn(country);
            when(provider.getLinks(userId)).thenReturn(new ArrayList<>());

            // ACT
            ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, USER_ID_STR);

            // ASSERT
            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void createAuthor_shouldAddLinks_whenProviderReturnsLinks() throws Exception {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            // ARRANGE
            mapper.when(() -> AuthorMapper.toUserId(USER_ID_STR)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toCountryId(dto)).thenReturn(countryId);
            mapper.when(() -> AuthorMapper.toDTO(author, country)).thenReturn(freshDTO());

            when(author.getCountryID()).thenReturn(countryId);
            when(author.id()).thenReturn(authorId);
            when(authorId.toString()).thenReturn(AUTHOR_ID_STR);
            when(service.createAndSave(any(), any(), any(), any(), any())).thenReturn(author);
            when(countryService.findById(any())).thenReturn(country);
            // provider returns one withRel link; controller adds self → total 2
            when(provider.getLinks(userId)).thenReturn(new ArrayList<>(List.of(CREATE_LINK)));

            // ACT
            ResponseEntity<AuthorDTO> result = sut.createAuthor(dto, USER_ID_STR);

            // ASSERT
            assertNotNull(result.getBody());
            assertEquals(2, result.getBody().getLinks().toList().size());
        }
    }

    // =========================================================================
    // createAuthor — exceptions
    // =========================================================================

    @Test
    void createAuthor_shouldReturnForbidden_whenAccessDenied() throws Exception {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            mapper.when(() -> AuthorMapper.toUserId(USER_ID_STR)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toCountryId(dto)).thenReturn(countryId);

            when(service.createAndSave(any(), any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            assertEquals(HttpStatus.FORBIDDEN, sut.createAuthor(dto, USER_ID_STR).getStatusCode());
        }
    }

    @Test
    void createAuthor_shouldReturnBadRequest_whenIllegalArgument() throws Exception {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            mapper.when(() -> AuthorMapper.toUserId(USER_ID_STR)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toCountryId(dto)).thenReturn(countryId);

            when(service.createAndSave(any(), any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            assertEquals(HttpStatus.BAD_REQUEST, sut.createAuthor(dto, USER_ID_STR).getStatusCode());
        }
    }

    @Test
    void createAuthor_shouldReturnConflict_whenInstanceAlreadyExists() throws Exception {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            mapper.when(() -> AuthorMapper.toUserId(USER_ID_STR)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toCountryId(dto)).thenReturn(countryId);

            when(service.createAndSave(any(), any(), any(), any(), any()))
                    .thenThrow(new InstanceAlreadyExistsException());

            assertEquals(HttpStatus.CONFLICT, sut.createAuthor(dto, USER_ID_STR).getStatusCode());
        }
    }

    @Test
    void createAuthor_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            mapper.when(() -> AuthorMapper.toUserId(USER_ID_STR)).thenReturn(userId);
            mapper.when(() -> AuthorMapper.toCountryId(dto)).thenReturn(countryId);

            when(service.createAndSave(any(), any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, sut.createAuthor(dto, USER_ID_STR).getStatusCode());
        }
    }

    // =========================================================================
    // findAll — success
    // findAll uses methodOn — proxy re-enters findById, needs full stub
    // =========================================================================

    @Test
    void findAll_shouldReturnOk_whenAuthorsExist() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            // ARRANGE
            stubProxyFindById(mapper);
            when(author.id()).thenReturn(authorId);
            when(authorId.toString()).thenReturn(AUTHOR_ID_STR);
            when(service.findById(any())).thenReturn(author); // for the proxy
            when(service.findAll()).thenReturn(List.of(author));

            // ACT
            ResponseEntity<List<AuthorDTO>> result = sut.findAll(USER_ID_STR);

            // ASSERT
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals(1, result.getBody().size());
        }
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoAuthorsExist() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            stubProxyFindById(mapper);
            when(service.findAll()).thenReturn(List.of());

            ResponseEntity<List<AuthorDTO>> result = sut.findAll(USER_ID_STR);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
            assertTrue(result.getBody().isEmpty());
        }
    }

    @Test
    void findAll_shouldAddSelfLink_toEachAuthor() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            // ARRANGE
            stubProxyFindById(mapper);
            when(author.id()).thenReturn(authorId);
            when(authorId.toString()).thenReturn(AUTHOR_ID_STR);
            when(service.findById(any())).thenReturn(author);
            when(service.findAll()).thenReturn(List.of(author));

            // ACT
            ResponseEntity<List<AuthorDTO>> result = sut.findAll(USER_ID_STR);

            // ASSERT
            assertNotNull(result.getBody());
            assertTrue(result.getBody().get(0).hasLink("self"));
        }
    }

    // =========================================================================
    // findAll — exceptions
    // =========================================================================

    @Test
    void findAll_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            stubProxyFindById(mapper);

            when(service.findAll()).thenThrow(new IllegalArgumentException());

            assertEquals(HttpStatus.BAD_REQUEST, sut.findAll(USER_ID_STR).getStatusCode());
        }
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            stubProxyFindById(mapper);
            when(service.findAll()).thenThrow(new RuntimeException());

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, sut.findAll(USER_ID_STR).getStatusCode());
        }
    }

    // =========================================================================
    // findById — success
    // findById uses methodOn — proxy re-enters itself, needs full stub
    // =========================================================================

    @Test
    void findById_shouldReturnOk_whenAuthorExists() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            // ARRANGE
            stubProxyFindById(mapper);
            when(service.findById(any())).thenReturn(author);

            // ACT
            ResponseEntity<AuthorDTO> result = sut.findById(AUTHOR_ID_STR, USER_ID_STR);

            // ASSERT
            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }
    }

    @Test
    void findById_shouldAddSelfLink() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            // ARRANGE
            stubProxyFindById(mapper);
            when(service.findById(any())).thenReturn(author);

            // ACT
            ResponseEntity<AuthorDTO> result = sut.findById(AUTHOR_ID_STR, USER_ID_STR);

            // ASSERT
            assertNotNull(result.getBody());
            assertTrue(result.getBody().hasLink("self"));
        }
    }

    // =========================================================================
    // findById — exceptions
    // =========================================================================

    @Test
    void findById_shouldReturnBadRequest_whenIllegalArgument() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            stubProxyFindById(mapper);
            // override: first call (real) throws; proxy call also throws but
            // that's fine — exception is already caught before proxy runs
            when(service.findById(any()))
                    .thenThrow(new IllegalArgumentException())
                    .thenReturn(author); // proxy call if it gets that far

            assertEquals(HttpStatus.BAD_REQUEST, sut.findById(AUTHOR_ID_STR, USER_ID_STR).getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedException() {
        try (MockedStatic<AuthorMapper> mapper = mockStatic(AuthorMapper.class)) {
            stubProxyFindById(mapper);
            when(service.findById(any()))
                    .thenThrow(new RuntimeException())
                    .thenReturn(author);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, sut.findById(AUTHOR_ID_STR, USER_ID_STR).getStatusCode());
        }
    }
}